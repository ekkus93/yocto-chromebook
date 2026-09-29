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

## CI qualification evidence

- `Validate KEFKA` run `36461497534` on commit `9c3e64357be4a2ca93d42d0f57abb2884998cc3f` completed successfully and included the KEFKA POC WIC image build gate.
- Exact-head `Validate KEFKA` run `36512544176` on commit `e35580f7e8fa2a683fb74edcad5e11e327452be3` completed successfully, including kas expansion, POC and desktop parse/dependency validation, and the KEFKA POC WIC image build.
- The exact-head run uploaded artifact `kefka-poc-image-e35580f7e8fa2a683fb74edcad5e11e327452be3` (artifact ID `11020632426`, 708,985,171 bytes), providing retained build evidence for the qualified WIC output.
- Exact-head `Validate KEFKA` run `36546613296` on commit `d77754d6661167cd20966ceaf79799fbadfc2f5e` completed successfully, including the KEFKA POC WIC image build.
- That run uploaded artifact `kefka-poc-image-d77754d6661167cd20966ceaf79799fbadfc2f5e` (artifact ID `11040053785`, 708,986,581 bytes), confirming the WIC output on the documentation commit itself.
- This qualifies repository-side KEFKA image construction only; runtime hardware qualification remains separate.

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
