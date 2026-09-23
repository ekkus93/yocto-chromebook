# Yocto Chromebook

`yocto-chromebook` is a Yocto/OpenEmbedded project for building compact, reproducible Linux images for Intel Chromebooks that have been converted to standard UEFI boot with MrChromebox firmware.

The initial proof of concept targets HP Chromebook 11-family Intel devices and keeps the platform architecture explicit before heavy build integration begins.

## Current supported-target status

| Board | Representative hardware | Status |
| --- | --- | --- |
| `snappy` | HP Chromebook 11 G6 EE-family Apollo Lake devices | Matrix entry exists; hardware status currently unknown/unsafe-disabled until evidence is recorded |
| `vorticon` | HP Chromebook 11 G8 EE Intel / Gemini Lake devices | Matrix entry exists; hardware status currently unknown/unsafe-disabled until evidence is recorded |

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

Install kas in a Python environment:

```bash
python3 -m pip install --upgrade kas
```

The first POC is pinned to the Yocto `scarthgap` branch. Later work can deliberately qualify another Yocto release series by updating `LAYERSERIES_COMPAT_yoctochromebook` and the kas files together.

## Quick start

Validate the repository bootstrap files:

```bash
python3 scripts/validate_repo.py
```

Validate kas configuration expansion:

```bash
kas dump kas/snappy-poc.yml
kas dump kas/vorticon-poc.yml
kas dump kas/snappy-desktop.yml
kas dump kas/vorticon-desktop.yml
```

Run BitBake parse validation for the first POC target:

```bash
kas shell kas/snappy-poc.yml -c 'bitbake -p'
```

Build commands are expected to become:

```bash
kas build kas/snappy-poc.yml
kas build kas/vorticon-poc.yml
kas build kas/snappy-desktop.yml
kas build kas/vorticon-desktop.yml
```

The current image recipes are placeholders. M5 and later milestones add real package content, boot qualification, and hardware evidence.

## Deployment

Initial deployment targets MrChromebox UEFI Full ROM systems and uses external USB boot before any internal eMMC writes.

See `docs/UEFI_DEPLOYMENT.md` for the bootloader policy, WIC artifact expectations, USB writing workflow, internal eMMC gating, recovery notes, and evidence to capture.

## Storage and updates

The persistent data model uses `/data` as the boundary between replaceable OS images and user/application state. Optional AppImages are stored under `/data/apps`, and the future production direction is A/B rootfs updates with `/data` preserved across rootfs replacement.

See `docs/STORAGE_AND_UPDATE_DESIGN.md` for the POC partition policy, `/data` layout, manual-installer safety requirements, and future A/B update design.

## Hardware qualification

Hardware support status is tracked in `docs/HARDWARE_MATRIX.md`. SNAPPY and VORTICON start with explicit `unknown` component states, and internal speakers remain `unsafe-disabled` until the audio safety gate is resolved.

## Repository layout

```text
.
├── docs/
│   ├── HARDWARE_MATRIX.md
│   ├── HARDWARE_NOTES.md
│   ├── POC_PACKAGE_BASELINE.md
│   ├── STORAGE_AND_UPDATE_DESIGN.md
│   ├── UEFI_DEPLOYMENT.md
│   ├── YOCTO_CHROMEBOOK_SPEC.md
│   └── YOCTO_CHROMEBOOK_POC_TODO.md
├── kas/
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
│   │       │   └── intel-geminilake-chromebook.inc
│   │       ├── snappy.conf
│   │       └── vorticon.conf
│   ├── recipes-bsp/
│   ├── recipes-core/
│   │   └── images/
│   │       ├── yocto-chromebook-desktop.bb
│   │       └── yocto-chromebook-poc.bb
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

CI additionally runs `kas dump` on all kas configs, BitBake parse validation, and dependency graph validation for `kas/snappy-poc.yml`.
