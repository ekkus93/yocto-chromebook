# Hardware Evidence Collection

This document defines the minimum evidence bundle for closing hardware-dependent TODO items in `docs/YOCTO_CHROMEBOOK_POC_TODO.md` and updating `docs/HARDWARE_MATRIX.md`.

The repository-side package and build checks can prove that images parse and dependencies resolve, but they cannot prove board behavior. Hardware checkboxes require logs from a booted SNAPPY or VORTICON system.

## Collection script

Run the evidence collector from a booted Yocto Chromebook image or equivalent rescue environment:

```bash
sudo scripts/collect_chromebook_evidence.sh snappy /tmp/yocto-chromebook-snappy-evidence
sudo scripts/collect_chromebook_evidence.sh vorticon /tmp/yocto-chromebook-vorticon-evidence
```

The first argument must be the board identifier: `snappy` or `vorticon`. The second argument is the output directory.

The script writes plain-text command output only. It does not upload anything automatically and it should be reviewed before being committed or attached to an issue.

## Evidence categories

The bundle is intended to support these TODO areas:

- firmware package and blob identity for Wi-Fi, Bluetooth, graphics, and audio
- MrChromebox UEFI boot behavior and kernel boot logs
- eMMC discovery and read/write planning
- keyboard, touchpad, USB, battery, brightness, suspend/resume, and webcam detection
- graphics and Wayland baseline diagnostics
- audio path identification before any internal speaker enablement
- image size, filesystem, and memory baseline measurements

## Safety policy

Internal speaker tests are deliberately excluded from the script. Audio evidence collection is limited to device identification and logs. Speaker playback remains blocked until codec, amplifier, topology, UCM2, mixer defaults, and safe output policy are reviewed.

Disk-destructive install actions are deliberately excluded from the script. It collects block-device identity and mount information, but it does not partition, format, write, or install images.

## Minimum artifacts before closing hardware TODO items

A board-specific evidence update should include:

1. The collector output directory or a reviewed subset of its logs.
2. The exact image or commit SHA booted.
3. The board identifier and representative hardware model.
4. The observed status for each affected row in `docs/HARDWARE_MATRIX.md`.
5. Any remaining unknowns or unsafe-disabled items.

Do not mark a hardware item `works` from expected chipset names alone. Use `detected`, `partial`, `blocked`, or `unsafe-disabled` unless booted-system evidence demonstrates functional behavior.
