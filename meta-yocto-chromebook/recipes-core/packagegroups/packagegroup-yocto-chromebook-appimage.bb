SUMMARY = "Yocto Chromebook AppImage runtime baseline"
DESCRIPTION = "Runtime packages needed for the POC AppImage policy and manual compatibility testing."
LICENSE = "MIT"

inherit packagegroup

# This packagegroup intentionally depends on runtime packages whose IPK output
# names are dynamically renamed. Scope the emitted package away from allarch so
# package_write_ipk accepts those dependencies.
PACKAGE_ARCH = "${TUNE_PKGARCH}"
PACKAGE_ARCH:${PN} = "${TUNE_PKGARCH}"

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
