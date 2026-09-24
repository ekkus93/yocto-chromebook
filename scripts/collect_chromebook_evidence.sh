#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat >&2 <<'USAGE'
Usage: collect_chromebook_evidence.sh <snappy|vorticon> <output-dir>

Collect non-destructive hardware evidence from a booted Yocto Chromebook image
or equivalent Linux rescue environment. The script writes plain-text logs only;
it does not upload data, partition disks, format disks, or play audio.
USAGE
}

if [[ $# -ne 2 ]]; then
  usage
  exit 2
fi

board="$1"
out_dir="$2"

case "$board" in
  snappy|vorticon) ;;
  *)
    echo "ERROR: board must be 'snappy' or 'vorticon'" >&2
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
EOF

run_log uname uname -a
run_log os-release cat /etc/os-release
run_log cmdline cat /proc/cmdline
run_log cpuinfo cat /proc/cpuinfo
run_log meminfo cat /proc/meminfo
run_log mounts findmnt -a
run_log block lsblk -a -o NAME,PATH,TYPE,SIZE,MODEL,SERIAL,TRAN,RO,RM,MOUNTPOINTS,FSTYPE,LABEL,UUID
run_shell_log disk-by-id 'ls -l /dev/disk/by-id 2>/dev/null || true'
run_log pci lspci -nnvv
run_log usb lsusb -tv
run_shell_log input 'for d in /proc/bus/input/devices /sys/class/input/*/name; do echo "## $d"; cat "$d" 2>/dev/null || true; done'
run_shell_log drm 'for d in /sys/class/drm/*; do echo "## $d"; cat "$d/status" 2>/dev/null || true; cat "$d/modes" 2>/dev/null || true; done'
run_shell_log backlight 'for d in /sys/class/backlight/*; do echo "## $d"; cat "$d/actual_brightness" 2>/dev/null || true; cat "$d/max_brightness" 2>/dev/null || true; done'
run_shell_log power-supply 'for d in /sys/class/power_supply/*; do echo "## $d"; for f in type status capacity voltage_now current_now model_name manufacturer; do printf "%s=" "$f"; cat "$d/$f" 2>/dev/null || true; done; done'
run_shell_log modules 'lsmod 2>/dev/null || true'
run_shell_log firmware 'dmesg 2>/dev/null | grep -Ei "firmware|iwlwifi|rtl|ath|brcm|bluetooth|btusb|sof|avs|snd|hda|ucm|topology|drm|i915|mmc|sdhci|touchpad|i2c|hid|usb|battery|backlight" || true'
run_shell_log journal-boot 'journalctl -b --no-pager 2>/dev/null || true'
run_shell_log network 'ip addr show 2>/dev/null; ip route show 2>/dev/null; nmcli general status 2>/dev/null || true; nmcli device status 2>/dev/null || true'
run_shell_log bluetooth 'bluetoothctl list 2>/dev/null || true; rfkill list 2>/dev/null || true'
run_shell_log graphics 'loginctl seat-status seat0 2>/dev/null || true; weston --version 2>/dev/null || true; labwc --version 2>/dev/null || true'
run_shell_log audio 'aplay -l 2>/dev/null || true; arecord -l 2>/dev/null || true; pactl info 2>/dev/null || true; wpctl status 2>/dev/null || true'
run_shell_log image-metrics 'df -h 2>/dev/null; du -sh / /boot /data 2>/dev/null || true; free -h 2>/dev/null'

cat >"$out_dir/README.txt" <<EOF
Hardware evidence bundle for $board

Review logs before sharing. The bundle may contain serial numbers, MAC addresses,
network names, disk identifiers, and other environment-specific details.

Use this bundle to update docs/HARDWARE_MATRIX.md and the hardware-dependent
items in docs/YOCTO_CHROMEBOOK_POC_TODO.md. Do not mark a component as works
unless the captured evidence demonstrates functional behavior, not just device
presence.
EOF

echo "Evidence written to $out_dir"
