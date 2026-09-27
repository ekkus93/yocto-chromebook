SUMMARY = "Yocto Chromebook graphics and Wayland baseline"
DESCRIPTION = "Intel graphics, DRM/KMS, and minimal Wayland compositor packages for hardware bring-up."
LICENSE = "MIT"

# packagegroup.bbclass conditionally inherits allarch at parse time. Set this
# before inheriting packagegroup because libdrm's IPK output is dynamically
# renamed.
PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

RDEPENDS:${PN} = "\
    mesa \
    libdrm \
    weston \
"
