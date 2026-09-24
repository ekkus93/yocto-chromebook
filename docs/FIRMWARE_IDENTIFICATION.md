# Firmware Identification Notes

This document captures repository-side firmware identification work for SNAPPY and VORTICON without marking hardware-dependent TODO items complete before booted-device evidence exists.

`docs/YOCTO_CHROMEBOOK_POC_TODO.md` keeps M3 and M4 firmware tasks open until the required package names and blob paths are confirmed from a real board using `scripts/collect_chromebook_evidence.sh` or equivalent logs.

## Completion rule

A firmware item can be closed only when a board-specific evidence update records all of the following:

1. exact board and product identifier,
2. exact image or commit SHA booted,
3. `lspci -nnvv` and `lsusb -tv` identities for Wi-Fi and Bluetooth,
4. kernel/journal firmware request lines,
5. resolved Yocto package names and installed firmware file paths,
6. any firmware that must stay excluded because it is unsafe, proprietary-only, unavailable, or not needed.

Expected chipset names from vendor literature are useful triage inputs, but they are not enough to mark the checklist items complete.

## SNAPPY / Apollo Lake triage baseline

SNAPPY is the ChromeOS board used by HP Chromebook 11 G6 EE class devices. External product literature for that class lists Intel HD Graphics 500, HD audio, eMMC storage, and Intel Dual Band Wireless-AC 7265 Wi-Fi/Bluetooth 4.2. Treat this as a candidate bill of materials, not as final hardware evidence for every SNAPPY unit.

Initial Yocto firmware triage:

- keep `linux-firmware` in the POC image until board evidence allows narrower package selection,
- expect Intel Wi-Fi firmware to be loaded by `iwlwifi` if the tested board has Intel Wireless-AC 7265,
- capture Bluetooth USB/UART identity before deciding whether a separate Bluetooth firmware package or userspace helper is required,
- keep audio firmware/topology/UCM2 unresolved until ALSA cards, kernel logs, topology requests, and codec/amplifier identifiers are captured,
- keep internal speakers unsafe-disabled until the audio safety gate is satisfied.

## VORTICON / Gemini Lake triage baseline

VORTICON is the ChromeOS device code name for HP Chromebook 11 G8 EE class devices. External product literature lists Intel UHD Graphics 600, HD audio, eMMC storage, and either Intel Wireless-AC 9560 or Realtek 802.11ac Wi-Fi/Bluetooth depending on configuration. Treat these as candidate variants until the actual unit is probed.

Initial Yocto firmware triage:

- keep `linux-firmware` in the POC image until board evidence allows narrower package selection,
- expect Intel CNVi/`iwlwifi` firmware if the tested unit has Intel Wireless-AC 9560,
- expect Realtek Wi-Fi/Bluetooth firmware investigation if the tested unit has the documented Realtek variant,
- keep audio firmware/topology/UCM2 unresolved until ALSA cards, kernel logs, topology requests, and codec/amplifier identifiers are captured,
- keep internal speakers unsafe-disabled until the audio safety gate is satisfied.

## Source references for triage only

These references are used to seed investigation. They do not replace booted-device evidence:

- ChromiumOS developer device table: `https://www.chromium.org/chromium-os/developer-information-for-chrome-os-devices/`
- HP Chromebook 11 G6 EE QuickSpecs: `https://media.flixcar.com/f360cdn/HP-4263866599-4aa7-1710enuc.pdf`
- HP Chromebook 11 G8 EE specifications: `https://support.hp.com/sk-en/document/ish_1869429-1551299-16`
- HP Chromebook 11 G8 EE QuickSpecs: `https://h20195.www2.hp.com/v2/getpdf.aspx/4AA7-6544ENUC.pdf`

## Evidence handoff

When a collector bundle is available, update these files in the same PR:

- `docs/HARDWARE_NOTES.md` with confirmed package/blob names,
- `docs/HARDWARE_MATRIX.md` with observed component statuses,
- `docs/YOCTO_CHROMEBOOK_POC_TODO.md` with checked M3/M4 firmware items only if the evidence supports closure,
- machine includes or packagegroups if the image needs board-specific firmware narrowing beyond the current broad `linux-firmware` baseline.
