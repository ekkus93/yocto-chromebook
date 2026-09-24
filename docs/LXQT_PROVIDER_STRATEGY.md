# LXQt Provider Strategy

This document records the provider strategy for the M12 LXQt + Labwc desktop milestone.

The current desktop image has a validated software baseline for Labwc, XWayland, and VLC. It does **not** have a complete LXQt session stack yet.

## Current validated state

The current scarthgap layer set resolves:

- `labwc`
- `xwayland`
- `vlc`

The following package names were intentionally tested in dependency-graph validation and did **not** resolve with the current layer set:

- `lxqt-session`
- `lxqt-panel`
- `lxqt-powermanagement`
- `lxqt-config`
- `pcmanfm-qt`
- `qterminal`

Do not add these names back to `packagegroup-yocto-chromebook-desktop` until the provider layer or local recipes are present and CI proves the dependency graph resolves.

## Provider candidates

### 1. Maintained scarthgap-compatible LXQt layer

The preferred path is a maintained external layer that supports the same Yocto series as the rest of the project and provides the missing LXQt recipes.

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

### 3. Interim Labwc-only desktop

The current Labwc/XWayland/VLC packagegroup can continue to serve as an interim desktop scaffold and multimedia baseline. It does not complete the LXQt milestone and must not be used to check M12 LXQt package tasks.

## Rejected shortcuts

Do not use any of these shortcuts to close M12:

- adding unresolved package names to the packagegroup
- importing an old Yocto-series LXQt layer without scarthgap compatibility qualification
- marking package tasks complete from recipe names alone
- claiming desktop success from parse-only validation
- bypassing dependency-graph or image-build validation

## Acceptance path

A future LXQt provider PR should include the provider layer or local recipes, packagegroup updates, documentation, and validation together.

Minimum merge evidence:

1. `python3 scripts/validate_repo.py` passes.
2. `kas dump` passes for all kas files.
3. SNAPPY desktop parse passes.
4. SNAPPY desktop dependency graph resolves.
5. A desktop image build for at least one target passes before claiming the M12 build acceptance criterion.

Runtime acceptance still requires booted-system evidence for login/session launch, QTerminal, PCManFM-Qt, keyboard, touchpad, and graphics behavior.
