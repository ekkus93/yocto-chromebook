# KEFKA / Dell Chromebook 11 3180 Hardware Notes

This document tracks the initial Yocto bring-up assumptions for Dell Chromebook 11 3180-class hardware.

## Identity

- Retail model: Dell Chromebook 11 3180
- Closely related convertible model: Dell Chromebook 11 3189
- ChromeOS board / device name: `KEFKA` / `kefka`
- Baseboard / platform lineage: `strago` / Intel Braswell
- Yocto `MACHINE`: `kefka`

## Firmware assumption

The project assumes the same firmware model used by the existing supported-target work: the Chromebook has been converted to MrChromebox UEFI Full ROM before Yocto boot testing.

## Initial build target

The initial repository target is a build- and parse-qualified Yocto machine configuration, not a claim of runtime hardware qualification.

The new kas entry points are:

```bash
kas dump kas/kefka-poc.yml >/tmp/kefka-poc.yml
kas shell kas/kefka-poc.yml -c 'bitbake -p'
kas shell kas/kefka-poc.yml -c 'bitbake -g yocto-chromebook-poc'
kas build kas/kefka-poc.yml

kas dump kas/kefka-desktop.yml >/tmp/kefka-desktop.yml
kas shell kas/kefka-desktop.yml -c 'bitbake -p'
kas shell kas/kefka-desktop.yml -c 'bitbake -g yocto-chromebook-desktop'
kas build kas/kefka-desktop.yml
```

## Hardware qualification still required

All runtime items remain unknown until tested on the actual Dell Chromebook 11 3180 hardware:

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
- desktop session launch
- Firefox, VLC, and AppImage runtime evidence

## Safety policy

Do not qualify internal speakers on KEFKA until the audio path, amplifier behavior, topology, UCM2 files, mixer state, and controlled-volume safety gate are understood. Unknown audio behavior must remain documented as unqualified.
