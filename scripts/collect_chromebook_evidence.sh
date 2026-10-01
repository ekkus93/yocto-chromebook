#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat >&2 <<'USAGE'
Usage: collect_chromebook_evidence.sh <snappy|vorticon|kefka|magolor> <output-dir>

Collect non-destructive hardware evidence from a booted Yocto Chromebook image
or equivalent Linux rescue environment. The script writes plain-text logs only;
it does not upload data, partition disks, format disks, or play audio.

Optional active checks are disabled by default:
  YOCTO_CHROMEBOOK_ENABLE_NETWORK_TESTS=1 enables Wi-Fi rescan and HTTPS probe.
  YOCTO_CHROMEBOOK_HTTPS_TEST_URL overrides the HTTPS probe URL.
  YOCTO_CHROMEBOOK_ENABLE_BLUETOOTH_SCAN=1 enables a bounded Bluetooth scan.
USAGE
}

if [[ $# -ne 2 ]]; then
  usage
  exit 2
fi

board="$1"
out_dir="$2"

case "$board" in
  snappy|vorticon|kefka|magolor) ;;
  *)
    echo "ERROR: board must be one of: snappy, vorticon, kefka, magolor" >&2
    usage
    exit 2
    ;;
esac

mkdir -p "$out_dir"

run_log() {
  local name="$1"
  shift
  {
    echo "# $name"
    echo "# command: $*"
    echo "# captured_at_utc: $(date -u +%Y-%m-%dT%H:%M:%SZ)"
    echo
    "$@"
  } >"$out_dir/$name.txt" 2>&1 || {
    local status=$?
    {
      echo
      echo "# command exited with status $status"
    } >>"$out_dir/$name.txt"
    return 0
  }
}

run_shell_log() {
  local name="$1"
  local command="$2"
  {
    echo "# $name"
    echo "# command: $command"
    echo "# captured_at_utc: $(date -u +%Y-%m-%dT%H:%M:%SZ)"
    echo
    sh -c "$command"
  } >"$out_dir/$name.txt" 2>&1 || {
    local status=$?
    {
      echo
      echo "# command exited with status $status"
    } >>"$out_dir/$name.txt"
    return 0
  }
}

cat >"$out_dir/manifest.txt" <<EOF
board=$board
captured_at_utc=$(date -u +%Y-%m-%dT%H:%M:%SZ)
kernel=$(uname -a 2>/dev/null || true)
network_tests_enabled=${YOCTO_CHROMEBOOK_ENABLE_NETWORK_TESTS:-0}
bluetooth_scan_enabled=${YOCTO_CHROMEBOOK_ENABLE_BLUETOOTH_SCAN:-0}
EOF

run_log uname uname -a
run_log os-release cat /etc/os-release
run_log cmdline cat /proc/cmdline
run_log cpuinfo cat /proc/cpuinfo
run_log meminfo cat /proc/meminfo
run_shell_log boot-timing 'systemd-analyze time 2>/dev/null || true; systemd-analyze blame 2>/dev/null | head -200 || true; systemd-analyze critical-chain 2>/dev/null || true; printf "uptime_seconds="; cut -d" " -f1 /proc/uptime 2>/dev/null || true; printf "boot_time_epoch="; awk "/^btime /{print \$2}" /proc/stat 2>/dev/null || true'
run_log mounts findmnt -a
run_log block lsblk -a -o NAME,PATH,TYPE,SIZE,MODEL,SERIAL,TRAN,RO,RM,MOUNTPOINTS,FSTYPE,LABEL,UUID
run_shell_log disk-by-id 'ls -l /dev/disk/by-id 2>/dev/null || true'
run_log pci lspci -nnvv
run_log usb lsusb -tv
run_shell_log driver-bindings 'for bus in pci usb i2c platform; do echo "## bus=$bus"; for d in /sys/bus/$bus/devices/*; do [ -e "$d" ] || continue; echo "### $d"; printf "driver="; readlink "$d/driver" 2>/dev/null || true; for f in modalias vendor device class uevent; do [ -e "$d/$f" ] && { echo "--- $f"; cat "$d/$f" 2>/dev/null || true; }; done; done; done'
run_shell_log input 'for d in /proc/bus/input/devices /sys/class/input/*/name; do echo "## $d"; cat "$d" 2>/dev/null || true; done'
run_shell_log drm 'for d in /sys/class/drm/*; do echo "## $d"; cat "$d/status" 2>/dev/null || true; cat "$d/modes" 2>/dev/null || true; done'
run_shell_log backlight 'for d in /sys/class/backlight/*; do echo "## $d"; cat "$d/actual_brightness" 2>/dev/null || true; cat "$d/max_brightness" 2>/dev/null || true; done'
run_shell_log power-supply 'for d in /sys/class/power_supply/*; do echo "## $d"; for f in type status capacity voltage_now current_now model_name manufacturer; do printf "%s=" "$f"; cat "$d/$f" 2>/dev/null || true; done; done'
run_shell_log modules 'lsmod 2>/dev/null || true'
run_shell_log module-firmware 'command -v modinfo >/dev/null || exit 0; lsmod 2>/dev/null | awk "NR>1{print \$1}" | sort | while read -r module; do echo "## $module"; modinfo -F firmware "$module" 2>/dev/null || true; modinfo -F filename "$module" 2>/dev/null || true; done'
run_shell_log installed-firmware-packages 'opkg list-installed 2>/dev/null | grep -Ei "firmware|iwlwifi|rtl|realtek|brcm|ath|bluez|sof|alsa-ucm" || true; rpm -qa 2>/dev/null | grep -Ei "firmware|iwlwifi|rtl|realtek|brcm|ath|bluez|sof|alsa-ucm" || true; dpkg-query -W 2>/dev/null | grep -Ei "firmware|iwlwifi|rtl|realtek|brcm|ath|bluez|sof|alsa-ucm" || true'
run_shell_log installed-firmware-files 'for d in /lib/firmware /usr/lib/firmware; do echo "## $d"; find "$d" -maxdepth 4 -type f 2>/dev/null | sort || true; done'
run_shell_log firmware 'dmesg 2>/dev/null | grep -Ei "firmware|iwlwifi|rtl|ath|brcm|bluetooth|btusb|sof|avs|snd|hda|ucm|topology|tplg|rt5682|rt1015|max983|da7219|cs42l42|drm|i915|mmc|sdhci|touchpad|i2c|hid|usb|battery|backlight" || true'
run_shell_log journal-boot 'journalctl -b --no-pager 2>/dev/null || true'
run_shell_log network 'ip addr show 2>/dev/null; ip route show 2>/dev/null; resolvectl status 2>/dev/null || true; iw dev 2>/dev/null || true; nmcli general status 2>/dev/null || true; nmcli device status 2>/dev/null || true; nmcli -f IN-USE,BSSID,SSID,MODE,CHAN,RATE,SIGNAL,SECURITY device wifi list --rescan no 2>/dev/null || true'
run_shell_log bluetooth 'bluetoothctl list 2>/dev/null || true; bluetoothctl show 2>/dev/null || true; rfkill list 2>/dev/null || true'
run_shell_log graphics 'loginctl seat-status seat0 2>/dev/null || true; weston --version 2>/dev/null || true; labwc --version 2>/dev/null || true; ps -eo pid,comm,args 2>/dev/null | grep -Ei "weston|labwc|sddm|lxqt|pcmanfm|qterminal" || true'
run_shell_log audio-cards 'aplay -l 2>/dev/null || true; arecord -l 2>/dev/null || true; cat /proc/asound/cards 2>/dev/null || true; cat /proc/asound/devices 2>/dev/null || true'
run_shell_log audio-ucm 'alsaucm listcards 2>/dev/null || true; for d in /usr/share/alsa/ucm2 /usr/share/alsa/ucm2/conf.d; do echo "## $d"; find "$d" -maxdepth 3 -type f 2>/dev/null | sort || true; done'
run_shell_log audio-topology 'for d in /lib/firmware /usr/lib/firmware; do echo "## $d"; find "$d" -type f \( -iname "*sof*" -o -iname "*tplg*" -o -iname "*ucm*" -o -iname "*rt5682*" -o -iname "*rt1015*" -o -iname "*max983*" \) 2>/dev/null | sort || true; done'
run_shell_log audio-mixer-state 'amixer -c 0 contents 2>/dev/null || true; amixer -c 0 scontents 2>/dev/null || true; amixer -c 1 contents 2>/dev/null || true; amixer -c 1 scontents 2>/dev/null || true'
run_shell_log audio-session 'pactl info 2>/dev/null || true; pactl list cards 2>/dev/null || true; pactl list sinks 2>/dev/null || true; wpctl status 2>/dev/null || true; pw-dump 2>/dev/null || true'
run_shell_log image-metrics 'df -h 2>/dev/null; df -B1 2>/dev/null; du -sh / /boot /data 2>/dev/null || true; du -sxB1 / /boot /data 2>/dev/null || true; free -h 2>/dev/null; free -b 2>/dev/null'

if [[ "${YOCTO_CHROMEBOOK_ENABLE_NETWORK_TESTS:-0}" == "1" ]]; then
  run_shell_log network-active-test 'nmcli device wifi rescan 2>/dev/null || true; nmcli -f IN-USE,BSSID,SSID,MODE,CHAN,RATE,SIGNAL,SECURITY device wifi list 2>/dev/null || true; curl -I --max-time 15 "${YOCTO_CHROMEBOOK_HTTPS_TEST_URL:-https://example.com}" 2>/dev/null || true'
else
  printf '%s\n' 'Set YOCTO_CHROMEBOOK_ENABLE_NETWORK_TESTS=1 to run bounded Wi-Fi rescan and HTTPS probe.' >"$out_dir/network-active-test-skipped.txt"
fi

if [[ "${YOCTO_CHROMEBOOK_ENABLE_BLUETOOTH_SCAN:-0}" == "1" ]]; then
  run_shell_log bluetooth-active-scan 'timeout 20 bluetoothctl scan on 2>/dev/null || true; bluetoothctl devices 2>/dev/null || true; bluetoothctl scan off 2>/dev/null || true'
else
  printf '%s\n' 'Set YOCTO_CHROMEBOOK_ENABLE_BLUETOOTH_SCAN=1 to run a bounded Bluetooth scan.' >"$out_dir/bluetooth-active-scan-skipped.txt"
fi

cat >"$out_dir/README.txt" <<EOF
Hardware evidence bundle for $board

Review logs before sharing. The bundle may contain serial numbers, MAC addresses,
network names, disk identifiers, and other environment-specific details.

Use this bundle to update docs/HARDWARE_MATRIX.md and the hardware-dependent
items in docs/YOCTO_CHROMEBOOK_POC_TODO.md. Do not mark a component as works
unless the captured evidence demonstrates functional behavior, not just device
presence.

Audio logs intentionally identify cards, codecs, amplifiers, SOF/AVS firmware,
topology files, UCM2 profiles, PipeWire/WirePlumber state, and mixer controls
without playing audio. Internal speaker playback remains excluded until the
speaker-safety gate is reviewed for this board.

Default collection is passive. Active Wi-Fi HTTPS probing and Bluetooth scanning
are opt-in with YOCTO_CHROMEBOOK_ENABLE_NETWORK_TESTS=1 and
YOCTO_CHROMEBOOK_ENABLE_BLUETOOTH_SCAN=1.
EOF

echo "Evidence written to $out_dir"
