SUMMARY = "Yocto Chromebook graphics and Wayland baseline"
DESCRIPTION = "Intel graphics, DRM/KMS, and minimal Wayland compositor packages for hardware bring-up."
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = "\
    mesa \
    libdrm \
    weston \
"
