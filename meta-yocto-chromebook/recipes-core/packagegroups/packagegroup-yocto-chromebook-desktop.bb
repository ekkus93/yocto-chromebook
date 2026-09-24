SUMMARY = "Yocto Chromebook desktop compositor baseline"
DESCRIPTION = "Wayland compositor, XWayland compatibility, and desktop application packages."
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = "\
    labwc \
    xwayland \
    vlc \
"
