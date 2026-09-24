# AppImage Test Selection

This document records the initial M15 third-party x86-64 graphical AppImage candidate.

It selects a test artifact for future runtime validation only. It does not claim that the AppImage has launched on SNAPPY, VORTICON, or the Yocto Chromebook desktop image.

## Selected candidate

Initial candidate: GVim AppImage from `vim/vim-appimage`.

Rationale:

- provides a graphical application path, not only a command-line AppImage
- is distributed as an x86-64 AppImage release artifact
- uses GTK/X11 behavior, matching the current XWayland and common X11 library baseline
- is small enough for manual `/data/apps` bring-up compared with browser-scale applications
- provides a clear visual success condition: the GVim window opens and accepts keyboard input

Candidate URL pattern:

```text
https://github.com/vim/vim-appimage/releases/download/<tag>/GVim-<version>.glibc2.34-x86_64.AppImage
```

Pin the exact tag, filename, byte size, and SHA256 in a later evidence PR before checking the TODO selection item complete.

## Planned placement

On a booted image with the persistent-data layout available:

```bash
sudo mkdir -p /data/apps
sudo cp GVim-*.glibc2.34-x86_64.AppImage /data/apps/gvim.AppImage
sudo chmod 0755 /data/apps/gvim.AppImage
```

Run from the graphical session or an XWayland-capable terminal:

```bash
/data/apps/gvim.AppImage
```

## Evidence required before runtime closure

Capture and record:

- exact image or commit SHA booted
- exact AppImage URL, filename, size, and SHA256
- whether FUSE execution worked directly
- fallback output if `--appimage-extract-and-run` is needed
- terminal output from launch
- screenshot or log evidence that a GVim window opened
- keyboard input evidence inside the window
- relevant loader errors for missing shared libraries

## TODO boundary

This document establishes a candidate and test policy. It does not close:

- `Run AppImage from /data/apps`
- `Validate application appears in desktop environment or document manual launch path`
- `At least one x86-64 AppImage launches successfully`

Those remain open until a booted graphical image produces runtime evidence.
