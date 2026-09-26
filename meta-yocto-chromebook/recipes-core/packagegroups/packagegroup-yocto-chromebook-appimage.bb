SUMMARY = "Yocto Chromebook AppImage runtime baseline"
DESCRIPTION = "Runtime packages needed for the POC AppImage policy and manual compatibility testing."
LICENSE = "MIT"

# packagegroup.bbclass conditionally inherits allarch at parse time. Set this
# before inheriting packagegroup because these runtime dependencies include
# packages whose IPK names are dynamically renamed.
PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

RDEPENDS:${PN} = "\
    fuse \
    fuse3 \
    xwayland \
    libx11 \
    libxext \
    libxrender \
    libxrandr \
    libxi \
    libxfixes \
    libxcb \
    libxkbcommon \
    fontconfig \
    freetype \
"
