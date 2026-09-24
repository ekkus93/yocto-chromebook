# POC Known Gaps

This document summarizes known gaps for the current Yocto Chromebook proof of concept.

It is intentionally conservative: a feature is not marked supported until there is build, boot, hardware, or runtime evidence recorded in the canonical TODO and hardware matrix.

## Build and image status

- The repository has parse and dependency-graph validation for the SNAPPY POC image.
- A full SNAPPY POC image-build validation PR exists, but the full image-build gate is not yet merged into `master`.
- Because the image-build gate is not yet merged, compressed image size, installed rootfs size, and WIC artifact names are not release evidence yet.

## Hardware validation status

The current repository state does not include board-run evidence for:

- SNAPPY boot from external USB
- VORTICON boot from external USB
- internal eMMC visibility or write safety
- keyboard and touchpad behavior
- Wi-Fi chipset identity or network association
- Bluetooth controller identity or scan behavior
- panel native resolution and DRM/KMS runtime logs
- battery, charger, brightness, lid, suspend, or resume behavior

All of these remain hardware-test tasks and should be recorded in `docs/HARDWARE_MATRIX.md` when evidence exists.

## Firmware gaps

Board-specific firmware packages/blobs remain unidentified for SNAPPY and VORTICON. The POC image includes `linux-firmware` as a broad bring-up baseline, but the M3/M4 firmware checkboxes remain open until hardware evidence identifies the exact Wi-Fi, Bluetooth, audio, and other board-specific firmware requirements.

## Audio safety gaps

Internal speakers are deliberately not qualified. Audio hardware identity, amplifier safety, topology/UCM2 requirements, mixer policy, and controlled-volume tests remain open. No board should be marked audio-qualified until the M17 safety gate is satisfied.

## Desktop and AppImage gaps

The repository includes a software-side AppImage runtime path (`/data/apps`, FUSE, FUSE3, and XWayland) and a minimal Wayland compositor baseline through Weston. It does not yet include the full LXQt + Labwc desktop package set, Firefox, VLC, or a selected AppImage test artifact.

## Next actionable milestones

The next non-hardware milestone is to complete and merge a full POC image-build validation gate, then record image artifacts and size metrics. After that, the next hardware milestone is booting at least one board from MrChromebox UEFI and capturing logs for the hardware matrix.
