# Firefox Provider Strategy

This document records the repository-side provider strategy for the M13 Firefox base application milestone.

It does not add Firefox to the desktop image and it does not close any Firefox runtime checklist items. The goal is to prevent an unsafe or stale browser recipe from being pulled into the image without an explicit build, security, and runtime qualification path.

## Current provider finding

The OpenEmbedded Layer Index lists a `meta-firefox` layer with a scarthgap Firefox recipe, but the visible recipe version is Firefox 68.9.0 ESR.

That is useful as evidence that a scarthgap provider path exists, but it is not enough to add Firefox to `yocto-chromebook-desktop` because browser age and maintenance status are release-critical security concerns.

## Provider options

### 1. Use `meta-firefox` only after maintenance review

This is the most direct Yocto-native path if the project accepts the recipe version and maintenance state.

Before adding it to kas or any packagegroup, a future PR must document:

- exact layer URL, branch, and revision policy,
- exact Firefox recipe version,
- required layer dependencies,
- build dependency closure,
- security/update posture for the selected browser version,
- whether Wayland support is available or whether XWayland fallback is required,
- desktop launch path and certificate store behavior.

### 2. Use a different maintained browser provider

If Firefox 68.9.0 ESR is not acceptable, the project should evaluate a newer maintained browser provider rather than force the stale recipe into the image.

A future browser provider PR may choose:

- a maintained Firefox recipe from a different compatible layer,
- Chromium or another browser if Firefox is not viable for POC-1,
- a browser AppImage or externally supplied test artifact for manual desktop validation, if that better matches the `/data/apps` application model.

### 3. Defer browser inclusion until after the desktop image is build-qualified

The desktop stack still has unresolved LXQt provider work and runtime launch validation. It is acceptable to keep Firefox out of the image until the desktop image itself builds and boots, provided M13 stays open and the limitation remains visible.

## Rejected shortcuts

Do not close M13 by:

- adding a browser recipe name that has not passed dependency-graph validation,
- importing a browser layer without pinning a branch or revision policy,
- treating an old ESR recipe as safe without explicit maintenance review,
- claiming HTTPS support without certificate-store and runtime launch evidence,
- using an AppImage candidate to close the Yocto-native Firefox package task without documenting that substitution.

## Acceptance path

A future Firefox implementation PR should include the provider layer or recipe, packagegroup/image changes, documentation, and validation in one coherent slice.

Minimum merge evidence before marking package/build work complete:

1. `python3 scripts/validate_repo.py` passes.
2. `kas dump` passes for all affected kas files.
3. SNAPPY desktop parse passes.
4. SNAPPY desktop dependency graph resolves with the browser included.
5. A desktop image build for at least one target passes before claiming desktop-image acceptance.

Runtime acceptance still requires booted-system evidence that Firefox launches, loads an HTTPS page, and records startup and memory behavior.
