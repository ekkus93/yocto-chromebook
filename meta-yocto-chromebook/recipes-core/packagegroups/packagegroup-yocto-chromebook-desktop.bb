SUMMARY = "Yocto Chromebook desktop compositor baseline"
DESCRIPTION = "Wayland compositor and XWayland compatibility packages for the desktop image."
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = "\
    labwc \
    xwayland \
"
