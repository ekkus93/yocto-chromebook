# Multimedia Baseline

The desktop image includes the `vlc` recipe from the scarthgap `meta-multimedia` layer already present in both desktop kas configurations.

This establishes the software-side VLC dependency for M14 without claiming hardware playback or audio qualification. The project intentionally relies on the upstream recipe dependency set rather than adding an unreviewed codec bundle to the image.

## Qualification boundary

Repository CI must continue to parse the SNAPPY desktop image and resolve its dependency graph with `vlc` installed through `packagegroup-yocto-chromebook-desktop`.

Runtime qualification remains separate and requires a real graphical session and test media. Video playback, CPU/RAM measurements, and audio output remain unchecked until observed on hardware.

Audio must follow the M17 safety gate. Internal speakers must not be enabled merely to qualify VLC. Initial playback testing should use a known-safe output path such as headphones, HDMI, USB-C, or USB audio after the relevant device is confirmed safe.
