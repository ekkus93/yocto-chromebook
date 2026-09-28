# MAGOLOR / Acer Chromebook Spin 511 R753T-C4XP Hardware Notes

This document tracks the initial Yocto bring-up assumptions for Acer Chromebook Spin 511 R753T-class hardware, including the requested R753T-C4XP SKU.

## Identity

- Retail model family: Acer Chromebook Spin 511 R753T / R753TN
- Requested representative SKU: Acer Chromebook Spin 511 R753T-C4XP
- ChromeOS board / device name: `MAGOLOR` / `magolor`
- Baseboard / platform lineage: `dedede` / Intel Jasper Lake (`JSL`)
- Yocto `MACHINE`: `magolor`

## Firmware assumption

The project assumes the same firmware model used by the existing supported-target work: the Chromebook has been converted to MrChromebox UEFI Full ROM, or an equivalent documented UEFI boot path, before Yocto boot testing.

## Initial build target

The initial repository target is a build- and parse-qualified Yocto machine configuration, not a claim of runtime hardware qualification.

The new kas entry points are:

```bash
kas dump kas/magolor-poc.yml >/tmp/magolor-poc.yml
kas shell kas/magolor-poc.yml -c 'bitbake -p'
kas shell kas/magolor-poc.yml -c 'bitbake -g yocto-chromebook-poc'
kas build kas/magolor-poc.yml

kas dump kas/magolor-desktop.yml >/tmp/magolor-desktop.yml
kas shell kas/magolor-desktop.yml -c 'bitbake -p'
kas shell kas/magolor-desktop.yml -c 'bitbake -g yocto-chromebook-desktop'
kas build kas/magolor-desktop.yml
```

## Hardware qualification still required

All runtime items remain unknown until tested on the actual Acer Chromebook Spin 511 R753T-C4XP hardware:

- UEFI boot from external USB
- shell login
- eMMC visibility and write safety
- keyboard and top-row behavior
- touchpad enumeration and pointer behavior
- Wi-Fi chipset identity, firmware, scan, WPA2 association, DHCP, DNS, and HTTPS
- Bluetooth controller identity, firmware, enumeration, and scan
- Intel graphics DRM/KMS path and panel native resolution
- battery, charger, brightness, lid, suspend, and resume behavior
- audio path identity, topology/UCM2 requirements, and safe-output policy
- webcam and microphone enumeration
- touchscreen and convertible-mode behavior
- desktop session launch
- Firefox, VLC, and AppImage runtime evidence

## Safety policy

Do not qualify internal speakers on MAGOLOR until the audio path, amplifier behavior, topology, UCM2 files, mixer state, and controlled-volume safety gate are understood. Unknown audio behavior must remain documented as unqualified.
