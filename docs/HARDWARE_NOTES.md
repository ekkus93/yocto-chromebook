# Hardware Notes

This file records board-specific assumptions, open questions, and qualification evidence for the initial Yocto Chromebook proof of concept.

The project targets Chromebooks already converted to MrChromebox UEFI Full ROM firmware. ChromeOS firmware conversion is out of scope for this repository, but boot qualification depends on a standard UEFI boot path.

## Shared POC baseline

- Kernel baseline: `linux-yocto` through Poky's generic x86-64 UEFI machine baseline.
- Boot baseline: GPT + EFI System Partition through Yocto `.wic` images.
- Audio policy: internal speakers remain unqualified until board-specific codec, amplifier, topology, UCM2, mixer limits, mute behavior, and suspend/resume behavior are reviewed.
- Firmware policy: required firmware is not considered identified until captured from hardware evidence such as `lspci -nn`, `lsusb`, kernel logs, ALSA cards, and Bluetooth controller enumeration.
- Storage expectation: internal storage is expected to be eMMC on the first targets, but device names and write behavior must be confirmed on hardware.

## SNAPPY / Apollo Lake Chromebook

- Machine config: `meta-yocto-chromebook/conf/machine/snappy.conf`
- Include: `meta-yocto-chromebook/conf/machine/include/intel-apollolake-chromebook.inc`
- Kernel baseline: `linux-yocto` generic x86-64 UEFI baseline for the first parse/build POC.
- Wi-Fi expectation: onboard Chromebook Wi-Fi is expected, but chipset and firmware package names remain an M3 investigation item.
- Bluetooth expectation: onboard Bluetooth is expected, but controller identity and firmware package names remain an M3 investigation item.
- Audio expectation: Intel Chromebook audio is board-specific; codec, amplifier, topology, and UCM2 details remain an M3/M17 investigation item. Internal speakers stay disabled/unqualified until the safety gate is satisfied.
- Internal storage expectation: eMMC is expected, with actual Linux device naming to be recorded during hardware boot validation.
- Firmware assumption: MrChromebox UEFI Full ROM.
- Parse note: the required machine ID `snappy` collides with meta-oe's `snappy` compression recipe PN through BitBake `OVERRIDES`; the SNAPPY machine config masks that recipe for now because the POC image does not use it.

## VORTICON / Gemini Lake Chromebook

- Machine config: `meta-yocto-chromebook/conf/machine/vorticon.conf`
- Include: `meta-yocto-chromebook/conf/machine/include/intel-geminilake-chromebook.inc`
- Kernel baseline: `linux-yocto` generic x86-64 UEFI baseline for the first parse/build POC.
- Wi-Fi expectation: onboard Chromebook Wi-Fi is expected, but chipset and firmware package names remain an M4 investigation item.
- Bluetooth expectation: onboard Bluetooth is expected, but controller identity and firmware package names remain an M4 investigation item.
- Audio expectation: Intel Gemini Lake Chromebook audio is board-specific and may require careful SOF/AVS, topology, and UCM2 selection. Internal speakers stay disabled/unqualified until the safety gate is satisfied.
- Internal storage expectation: eMMC is expected, with actual Linux device naming to be recorded during hardware boot validation.
- Firmware assumption: MrChromebox UEFI Full ROM.
