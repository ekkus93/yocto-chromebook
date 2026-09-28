# LXQt Provider Strategy

This document records the provider strategy for the M12 LXQt + Labwc desktop milestone.

The desktop package graph now has a validated build-time baseline for Labwc, XWayland, VLC, SDDM, and the required LXQt applications. It does **not** yet have a hardware-qualified LXQt-on-Labwc login session.

## Current validated state

The Scarthgap desktop configuration resolves the following packages with the pinned provider stack:

- `labwc`
- `xwayland`
- `vlc`
- `sddm`
- `lxqt-session`
- `lxqt-panel`
- `lxqt-powermanagement`
- `lxqt-config`
- `pcmanfm-qt`
- `qterminal`

The provider revisions qualified by exact-head dependency-graph CI are:

- `meta-qt5`: `916d37e38077850ff5a0ac4fce6f2a55ba5b2a86`
- `meta-qt5-extra`: `cb9132b18c1fc5cdfe07407a7c6737b039c02117`

This qualifies package inclusion only. The selected `meta-qt5-extra` snapshot is an older Qt5/LXQt line and does not by itself prove the final LXQt-on-Labwc Wayland session path. The default-session and runtime acceptance items remain open until a booted desktop demonstrates them.

## Provider candidates

### 1. Maintained scarthgap-compatible LXQt layer

The preferred path remains a maintained scarthgap-compatible LXQt layer. The current build-qualified fallback uses pinned `meta-qt5` plus `meta-qt5-extra` revisions because that combination resolves the required package graph on the project’s Scarthgap base.

Any candidate layer must be added to the relevant kas files with a pinned branch or commit policy and must pass:

```bash
kas shell kas/snappy-desktop.yml -c 'bitbake -g yocto-chromebook-desktop'
```

Before M12 is reconciled, it must also support at least these runtime targets or documented equivalents:

- session manager: `lxqt-session`
- panel: `lxqt-panel`
- power management: `lxqt-powermanagement`
- settings tools: `lxqt-config`
- file manager: `pcmanfm-qt`
- terminal: `qterminal`
- login/session path: `sddm` or an explicitly documented alternative

### 2. Local recipes in this layer

If no maintained scarthgap-compatible LXQt layer is available, the fallback is to add local recipes under `meta-yocto-chromebook/recipes-desktop/`.

Local recipes must include normal Yocto recipe hygiene:

- explicit `LICENSE` and `LIC_FILES_CHKSUM`
- pinned upstream source revisions or release tarballs
- declared build dependencies
- declared runtime dependencies
- patches kept minimal and documented
- dependency-graph validation before merging

Local recipes should be added in small vertical slices only when each slice can be dependency-qualified.

### 3. Interim build-qualified LXQt package baseline

The current packagegroup includes the required LXQt applications and SDDM in addition to Labwc/XWayland/VLC. This closes the package-inclusion tasks only. It does not complete the LXQt milestone because the final login/session path and runtime application behavior still require a booted desktop.

## Rejected shortcuts

Do not use any of these shortcuts to close M12:

- adding unresolved package names to the packagegroup
- importing an old Yocto-series LXQt layer without scarthgap compatibility qualification
- marking package tasks complete from recipe names alone
- claiming desktop success from parse-only validation
- bypassing dependency-graph or image-build validation when claiming the M12 desktop-image acceptance criterion

## Acceptance path

A future LXQt provider PR should include the provider layer or local recipes, packagegroup updates, documentation, and validation together.

Minimum merge evidence for package-inclusion and provider configuration changes:

1. `python3 scripts/validate_repo.py` passes.
2. `kas dump` passes for all kas files.
3. SNAPPY desktop parse passes.
4. SNAPPY desktop dependency graph resolves.
5. Full POC image qualification still passes for both configured target boards.

The SNAPPY desktop full WIC build currently exceeds the GitHub-hosted runner budget during pull-request validation, so the workflow keeps that job as an explicit `workflow_dispatch` gate. A desktop image build for at least one target must pass before claiming the M12 desktop-image build acceptance criterion.

Runtime acceptance still requires booted-system evidence for login/session launch, QTerminal, PCManFM-Qt, keyboard, touchpad, and graphics behavior.

## Repository-owned session integration

Because the pinned Qt5/LXQt provider line does not itself prove a modern upstream LXQt Wayland session, the image carries a small repository-owned integration package, `yocto-chromebook-lxqt-labwc-session`.

That package provides:

- an SDDM Wayland session entry named `LXQt (Labwc)`,
- a `start-lxqt-labwc` wrapper that establishes the LXQt/Labwc Wayland session environment and starts Labwc,
- a Labwc autostart file that launches the LXQt panel, PCManFM-Qt desktop, and LXQt power management after the compositor is running,
- a one-shot first-boot SDDM state seeder that selects `lxqt-labwc.desktop` only when no prior SDDM session state exists.

This closes the software configuration task for the default session. It does not close login/runtime acceptance: SDDM launch, compositor startup, QTerminal, PCManFM-Qt, keyboard, touchpad, and graphics behavior still require booted-system evidence.
