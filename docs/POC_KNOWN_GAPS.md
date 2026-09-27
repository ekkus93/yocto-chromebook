# POC Known Gaps

This document summarizes known gaps for the current Yocto Chromebook proof of concept.

It is intentionally conservative: a feature is not marked supported until there is build, boot, hardware, or runtime evidence recorded in the canonical TODO and hardware matrix.

## Build and image status

- The repository has parse and dependency-graph validation for the SNAPPY POC image.
- The SNAPPY POC WIC image-build gate has been merged into `master` through PR #30.
- The qualified SNAPPY POC image artifact is recorded in `docs/IMAGE_METRICS.md`.
- Current qualified SNAPPY POC image sizes are:
  - raw `.wic`: `2,656,201,728 bytes`
  - compressed `.wic.gz`: `699,995,369 bytes`
  - manifest: `62,502 bytes`
- VORTICON full-image build evidence is still open.
- Installed rootfs size, boot-time, and RAM metrics still require booted or mounted-image evidence.

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

The repository includes a software-side AppImage runtime path (`/data/apps`, FUSE, FUSE3, XWayland, common X11/XCB libraries, and font libraries), a minimal Wayland compositor baseline through Weston, and a desktop packagegroup containing Labwc, XWayland, VLC, SDDM, and the required LXQt applications.

Pinned `meta-qt5` and `meta-qt5-extra` revisions now resolve the LXQt package graph in exact-head Scarthgap CI. M12 remains open for the full desktop image build and for the actual LXQt-on-Labwc login/session and application runtime checks.

The LXQt provider strategy, pinned revisions, and remaining runtime caveat are recorded in `docs/LXQT_PROVIDER_STRATEGY.md`.

The initial AppImage candidate and evidence policy are recorded in `docs/APPIMAGE_TEST_SELECTION.md`, but runtime launch evidence remains open.

The Firefox provider strategy is recorded in `docs/FIREFOX_PROVIDER_STRATEGY.md`. Firefox remains out of the image until a provider layer or recipe passes build, maintenance, security, and runtime review.

Desktop runtime launch evidence also remains open.

## Next actionable milestones

The next hardware milestone is booting at least one board from MrChromebox UEFI and capturing logs for the hardware matrix. The next non-hardware metrics milestone is recording mounted-image rootfs size and desktop image sizes. The desktop milestone is qualifying the SNAPPY desktop WIC and then validating the LXQt-on-Labwc session path; the browser milestone needs a maintained Firefox provider decision.
