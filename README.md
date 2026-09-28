# Yocto Chromebook

`yocto-chromebook` is a Yocto/OpenEmbedded project for building compact, reproducible Linux images for Intel Chromebooks that have been converted to standard UEFI boot with MrChromebox firmware.

The initial proof of concept targets HP, Dell, and Acer education-class Intel Chromebooks and keeps the platform architecture explicit before hardware support is claimed.

## Current supported-target status

| Board | Representative hardware | Status |
| --- | --- | --- |
| `snappy` | HP Chromebook 11 G6 EE-family Apollo Lake devices | POC WIC image-build qualified; hardware not release-qualified |
| `vorticon` | HP Chromebook 11 G8 EE Intel / Gemini Lake devices | POC WIC image-build qualified; hardware not release-qualified |
| `kefka` | Dell Chromebook 11 3180 / 3189 Braswell devices | Build scaffold merged; hardware not release-qualified |
| `magolor` | Acer Chromebook Spin 511 R753T-C4XP / R753T-family Jasper Lake devices | Build scaffold pending exact-head qualification; hardware not release-qualified |

No board is release-qualified yet. Hardware support must be recorded in `docs/HARDWARE_MATRIX.md` before a board is described as supported.

## Project direction

The target system model is:

```text
System packages       -> Yocto image
Desktop/base apps     -> Yocto image
Optional user apps    -> AppImage in /data/apps
User/application data -> /data
OS updates            -> whole-image updates
```

The desktop direction is LXQt + Labwc on Wayland, with Firefox, VLC, QTerminal, PCManFM-Qt, nano, screen, htop, and ncdu in the image once the desktop milestone is implemented.

See `docs/YOCTO_CHROMEBOOK_SPEC.md` for the architecture source of truth.

## Build prerequisites

The repository uses `kas` to pin Poky/OE-Core and required layers.

On Ubuntu 24.04, install the host packages used by repository CI and install kas in a Python environment:

```bash
sudo apt-get update
sudo apt-get install -y chrpath diffstat python3 python3-pip
python3 -m pip install --upgrade kas
```

BitBake requires unprivileged user namespaces. Ubuntu 24.04 hosts with AppArmor user-namespace restrictions may need the same temporary setting used by CI:

```bash
sudo sysctl -w kernel.apparmor_restrict_unprivileged_userns=0
```

The first POC is pinned to the Yocto `scarthgap` branch. Later work can deliberately qualify another Yocto release series by updating `LAYERSERIES_COMPAT_yoctochromebook` and the kas files together.

## Quick start

From a clean checkout, reproduce the current qualified repository state in this order:

```bash
python3 scripts/validate_repo.py

kas dump kas/snappy-poc.yml >/tmp/snappy-poc.yml
kas dump kas/vorticon-poc.yml >/tmp/vorticon-poc.yml
kas dump kas/kefka-poc.yml >/tmp/kefka-poc.yml
kas dump kas/magolor-poc.yml >/tmp/magolor-poc.yml
kas dump kas/snappy-desktop.yml >/tmp/snappy-desktop.yml
kas dump kas/vorticon-desktop.yml >/tmp/vorticon-desktop.yml
kas dump kas/kefka-desktop.yml >/tmp/kefka-desktop.yml
kas dump kas/magolor-desktop.yml >/tmp/magolor-desktop.yml

kas shell kas/snappy-poc.yml -c 'bitbake -p'
kas shell kas/snappy-poc.yml -c 'bitbake -g yocto-chromebook-poc'
kas shell kas/vorticon-poc.yml -c 'bitbake -p'
kas shell kas/vorticon-poc.yml -c 'bitbake -g yocto-chromebook-poc'
kas shell kas/kefka-poc.yml -c 'bitbake -p'
kas shell kas/kefka-poc.yml -c 'bitbake -g yocto-chromebook-poc'
kas shell kas/magolor-poc.yml -c 'bitbake -p'
kas shell kas/magolor-poc.yml -c 'bitbake -g yocto-chromebook-poc'
kas shell kas/snappy-desktop.yml -c 'bitbake -p'
kas shell kas/snappy-desktop.yml -c 'bitbake -g yocto-chromebook-desktop'
kas shell kas/kefka-desktop.yml -c 'bitbake -p'
kas shell kas/kefka-desktop.yml -c 'bitbake -g yocto-chromebook-desktop'
kas shell kas/magolor-desktop.yml -c 'bitbake -p'
kas shell kas/magolor-desktop.yml -c 'bitbake -g yocto-chromebook-desktop'
```

These commands are the CI-backed reproduction gate as each target is qualified. They validate repository structure, kas expansions, POC parse/dependency graphs, and desktop parse/dependency graphs. They do **not** claim that hardware boot is qualified.

Full image build commands are:

```bash
kas build kas/snappy-poc.yml
kas build kas/vorticon-poc.yml
kas build kas/kefka-poc.yml
kas build kas/magolor-poc.yml
kas build kas/snappy-desktop.yml
kas build kas/vorticon-desktop.yml
kas build kas/kefka-desktop.yml
kas build kas/magolor-desktop.yml
```

The current image recipes include the POC package baseline plus Bluetooth, graphics/Wayland, AppImage runtime support, and GParted for partition inspection/maintenance. The SNAPPY and VORTICON POC WIC image-build gates are qualified; KEFKA and MAGOLOR target acceptance, desktop images, and all hardware boot evidence remain tracked in `docs/YOCTO_CHROMEBOOK_POC_TODO.md`.

Current SNAPPY POC image metrics are recorded in `docs/IMAGE_METRICS.md`.

## Deployment

Initial deployment targets MrChromebox UEFI Full ROM systems and uses external USB boot before any internal eMMC writes.

See `docs/UEFI_DEPLOYMENT.md` for the bootloader policy, WIC artifact expectations, USB writing workflow, internal eMMC gating, recovery notes, and evidence to capture.

## Storage and updates

The persistent data model uses `/data` as the boundary between replaceable OS images and user/application state. Optional AppImages are stored under `/data/apps`, and the future production direction is A/B rootfs updates with `/data` preserved across rootfs replacement.

See `docs/STORAGE_AND_UPDATE_DESIGN.md` for the POC partition policy, `/data` layout, manual-installer safety requirements, and future A/B update design.

## Runtime compatibility

The software-side Bluetooth, graphics/Wayland, and AppImage runtime package baseline is documented in `docs/RUNTIME_COMPATIBILITY_BASELINE.md`. Hardware and desktop runtime behavior remains unqualified until evidence is recorded in `docs/HARDWARE_MATRIX.md` and the canonical TODO.

## Known gaps

Current build, hardware, firmware, audio, desktop, and AppImage limitations are summarized in `docs/POC_KNOWN_GAPS.md`.

## Repository layout

```text
.
├── docs/
│   ├── HARDWARE_MATRIX.md
│   ├── HARDWARE_NOTES.md
│   ├── IMAGE_METRICS.md
│   ├── KEFKA_HARDWARE_NOTES.md
│   ├── MAGOLOR_HARDWARE_NOTES.md
│   ├── POC_KNOWN_GAPS.md
│   ├── POC_PACKAGE_BASELINE.md
│   ├── RUNTIME_COMPATIBILITY_BASELINE.md
│   ├── STORAGE_AND_UPDATE_DESIGN.md
│   ├── UEFI_DEPLOYMENT.md
│   ├── YOCTO_CHROMEBOOK_SPEC.md
│   └── YOCTO_CHROMEBOOK_POC_TODO.md
├── kas/
│   ├── kefka-desktop.yml
│   ├── kefka-poc.yml
│   ├── magolor-desktop.yml
│   ├── magolor-poc.yml
│   ├── snappy-desktop.yml
│   ├── snappy-poc.yml
│   ├── vorticon-desktop.yml
│   └── vorticon-poc.yml
├── meta-yocto-chromebook/
│   ├── conf/
│   │   ├── distro/
│   │   │   └── yocto-chromebook.conf
│   │   ├── layer.conf
│   │   └── machine/
│   │       ├── include/
│   │       │   ├── intel-apollolake-chromebook.inc
│   │       │   ├── intel-braswell-chromebook.inc
│   │       │   ├── intel-geminilake-chromebook.inc
│   │       │   └── intel-jasperlake-chromebook.inc
│   │       ├── kefka.conf
│   │       ├── magolor.conf
│   │       ├── snappy.conf
│   │       └── vorticon.conf
│   ├── recipes-bsp/
│   ├── recipes-core/
│   │   ├── images/
│   │   │   ├── yocto-chromebook-desktop.bb
│   │   │   └── yocto-chromebook-poc.bb
│   │   └── packagegroups/
│   ├── recipes-desktop/
│   ├── recipes-kernel/
│   ├── recipes-multimedia/
│   └── recipes-support/
└── scripts/
```

## Validation

Run the repository validator locally with:

```bash
python3 scripts/validate_repo.py
```

CI additionally runs `kas dump` on all kas configs plus BitBake parse and dependency-graph validation for SNAPPY POC, VORTICON POC, target-specific POC scaffolds, and desktop scaffolds. Full POC WIC jobs run in parallel for pull-request qualification, master pushes, or explicit manual workflow dispatch.
