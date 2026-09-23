SUMMARY = "Yocto Chromebook AppImage runtime baseline"
DESCRIPTION = "Runtime packages needed for the POC AppImage policy and manual compatibility testing."
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = "\
    fuse \
    fuse3 \
    xwayland \
"
