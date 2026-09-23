# POC Package Baseline

The proof-of-concept image includes `packagegroup-yocto-chromebook-poc`, which is the source of truth for the M5 base package baseline.

## Base utilities

The packagegroup pulls in:

- `bash`
- `coreutils`
- `util-linux`
- `curl`
- `wget`
- `ca-certificates`
- `tar`
- `gzip`
- `xz`
- `unzip`
- `iproute2`
- `ethtool`
- `pciutils`
- `usbutils`
- `procps`
- `less`
- `nano`
- `screen`
- `htop`
- `ncdu`
- `openssh-ssh`

Yocto's OpenSSH client binary package is represented as `openssh-ssh`, which satisfies the checklist's `openssh-client` intent without enabling an SSH server by default.

The selected scarthgap layer set does not provide `ncdu`, so this repository carries `meta-yocto-chromebook/recipes-support/ncdu/ncdu_1.19.bb` to keep the POC diagnostic baseline self-contained.

## Boot, network, modules, and firmware

The packagegroup also pulls in:

- `packagegroup-core-boot` for the systemd-oriented boot baseline
- `networkmanager` for the selected POC network stack
- `kernel-modules` for broad first-boot module availability during bring-up
- `linux-firmware` for initial firmware loading support while board-specific firmware identities remain under M3/M4 investigation

## Runtime hardware support packagegroups

The POC packagegroup also includes modular runtime packagegroups:

- `packagegroup-yocto-chromebook-bluetooth`
- `packagegroup-yocto-chromebook-graphics`
- `packagegroup-yocto-chromebook-appimage`

These groups add the software-side Bluetooth, graphics/Wayland, and AppImage compatibility baseline. Their contents and remaining runtime validation requirements are documented in `docs/RUNTIME_COMPATIBILITY_BASELINE.md`.

Board-specific Wi-Fi, Bluetooth, and audio firmware identities still need hardware evidence before the M3/M4 firmware checkboxes can be closed.
