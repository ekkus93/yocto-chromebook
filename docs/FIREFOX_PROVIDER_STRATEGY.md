# Firefox Provider Strategy

This document records the repository-side provider strategy for the M13 Firefox base application milestone.

Firefox is now represented by a repository-owned x86-64 binary ESR recipe, `firefox-esr-bin`. This closes the provider-selection portion only after the focused provider workflow passes for the exact commit. Runtime acceptance remains open until a booted desktop proves Firefox launches, loads HTTPS, and records startup/memory behavior.

## Selected provider

The selected provider is a pinned Mozilla Firefox ESR binary archive for Linux x86-64:

- recipe: `meta-yocto-chromebook/recipes-browser/firefox/firefox-esr-bin_153.3.0esr.bb`
- version: `153.3.0esr`
- locale: `en-US`
- architecture: `linux-x86_64`
- SHA-256: `8c36ca21beddcf09261661a74236b75a24a39c9a7c3193f812ca519e77c7c6d8`

The recipe is intentionally scoped to x86-64 Linux hosts through `COMPATIBLE_HOST`; ARM, AMD, and non-Linux targets remain out of scope for POC-1.

## Packaging policy

The Firefox archive is treated as a prebuilt upstream browser bundle. The recipe:

- installs the upstream bundle under `${libdir}/firefox`,
- installs `/usr/bin/firefox` through a small wrapper,
- sets `MOZ_ENABLE_WAYLAND=1` by default while preserving caller overrides,
- installs a desktop entry for LXQt menus,
- disables Firefox self-update through enterprise policies so browser updates remain image-owned, and
- includes `ca-certificates` as a runtime dependency for HTTPS validation.

Because the upstream archive contains prebuilt binaries and bundled shared objects, the recipe skips QA checks that are not meaningful for this binary repackaging path. Native Yocto-built browser recipes may tighten those checks later.

## Validation workflow

The focused provider workflow is `.github/workflows/firefox-provider.yml`.

Minimum repository-side evidence before marking provider/build work complete:

1. `python3 scripts/validate_repo.py` passes.
2. `bitbake firefox-esr-bin -c fetch` verifies the upstream archive checksum.
3. `bitbake firefox-esr-bin -c install` succeeds.
4. `bitbake firefox-esr-bin -c package` succeeds.
5. The SNAPPY desktop parse/dependency graph includes `firefox-esr-bin` through `packagegroup-yocto-chromebook-desktop`.

## Remaining runtime acceptance

The following M13 tasks are not closed by provider packaging alone:

- launching Firefox from LXQt,
- loading an HTTPS page,
- confirming Wayland-native operation or documenting XWayland fallback,
- recording memory and startup time, and
- validating browser behavior after suspend/resume if relevant.

Runtime evidence must come from a booted desktop image and should be recorded in the canonical TODO and `docs/HARDWARE_MATRIX.md` before Firefox is described as runtime-qualified.

## Rejected shortcuts

Do not close M13 by:

- treating provider packaging as launch evidence,
- enabling Firefox self-update outside the image update model,
- using an AppImage candidate to satisfy the Yocto-native Firefox package task without documenting the substitution,
- claiming HTTPS support without certificate-store and runtime evidence, or
- carrying a browser version without an explicit update policy.
