# Yocto Chromebook

`yocto-chromebook` is a Yocto/OpenEmbedded project for building compact, reproducible Linux images for Intel Chromebooks that have been converted to standard UEFI boot with MrChromebox firmware.

The initial proof of concept targets HP Chromebook 11-family Intel devices and keeps the platform architecture explicit before heavy build integration begins.

## Current supported-target status

| Board | Representative hardware | Status |
| --- | --- | --- |
| `snappy` | HP Chromebook 11 G6 EE-family Apollo Lake devices | Planned; machine config not implemented yet |
| `vorticon` | HP Chromebook 11 G8 EE Intel / Gemini Lake devices | Planned; machine config not implemented yet |

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

## Quick-start placeholder

Buildable kas configurations are not implemented yet. The intended future commands are:

```bash
kas build kas/snappy-poc.yml
kas build kas/vorticon-poc.yml
kas build kas/snappy-desktop.yml
kas build kas/vorticon-desktop.yml
```

Until M1 and M2 in `docs/YOCTO_CHROMEBOOK_POC_TODO.md` are complete, the repository only contains the bootstrap specification, checklist, validation script, and tracked directory skeleton.

## Repository layout

```text
.
├── docs/
│   ├── YOCTO_CHROMEBOOK_SPEC.md
│   └── YOCTO_CHROMEBOOK_POC_TODO.md
├── kas/
├── meta-yocto-chromebook/
│   ├── conf/
│   │   ├── distro/
│   │   └── machine/
│   │       └── include/
│   ├── recipes-bsp/
│   ├── recipes-core/
│   ├── recipes-desktop/
│   ├── recipes-kernel/
│   ├── recipes-multimedia/
│   └── recipes-support/
└── scripts/
```

## Validation

Run the repository bootstrap validator locally with:

```bash
python3 scripts/validate_repo.py
```

The validator checks that the M0 layout and source-of-truth documents are present. Later milestones should extend validation with Yocto layer parsing, kas dumps, and build smoke tests.
