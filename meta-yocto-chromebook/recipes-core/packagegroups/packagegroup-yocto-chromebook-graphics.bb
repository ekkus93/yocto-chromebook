SUMMARY = "Yocto Chromebook graphics and Wayland baseline"
DESCRIPTION = "Intel graphics, DRM/KMS, and minimal Wayland compositor packages for hardware bring-up."
LICENSE = "MIT"

inherit packagegroup

# This packagegroup intentionally depends on runtime packages whose IPK output
# names are dynamically renamed. Scope the emitted package away from allarch so
# package_write_ipk accepts those dependencies.
PACKAGE_ARCH = "${TUNE_PKGARCH}"
PACKAGE_ARCH:${PN} = "${TUNE_PKGARCH}"

RDEPENDS:${PN} = "\
    mesa \
    libdrm \
    weston \
"
