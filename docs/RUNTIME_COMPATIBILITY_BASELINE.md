# Runtime Compatibility Baseline

This document records the software-side runtime packages added before board hardware validation.

It does not claim that any device has been hardware-qualified. Hardware evidence remains tracked in `docs/HARDWARE_MATRIX.md` and board-specific notes.

## Bluetooth baseline

The POC image includes `packagegroup-yocto-chromebook-bluetooth`, which currently pulls in:

- `bluez5`

This is enough to provide the standard BlueZ userspace tools and service path for adapter enumeration once board firmware and controller identity are known.

Hardware validation still requires:

- controller enumeration on SNAPPY and VORTICON
- required firmware identification
- `bluetoothctl list` output
- scan evidence
- optional pairing evidence when a test device is available

## Graphics and Wayland baseline

The POC image includes `packagegroup-yocto-chromebook-graphics`, which currently pulls in:

- `mesa`
- `libdrm`
- `weston`

This gives the software baseline for Intel DRM/KMS and a minimal Wayland compositor path before the LXQt + Labwc desktop milestone.

Hardware validation still requires:

- panel native-resolution evidence
- kernel modesetting evidence
- compositor startup logs
- confirmation that no software-only fallback is being treated as final acceleration

## AppImage runtime baseline

The POC image includes `packagegroup-yocto-chromebook-appimage`, which currently pulls in:

- `fuse`
- `fuse3`
- `xwayland`

The project policy remains that optional user applications live under `/data/apps`; the Yocto image provides compatibility libraries and launch support, not an end-user package manager.

AppImage validation still requires:

- selecting a known-good x86-64 graphical AppImage
- placing it under `/data/apps`
- launching it in the graphical environment
- documenting failures for unsupported AppImages

## Qualification policy

This package baseline is qualified when the SNAPPY POC dependency graph resolves and BitBake parsing succeeds. Runtime behavior is not considered qualified until the relevant hardware or desktop tests are captured in the hardware matrix and TODO.
