SUMMARY = "Yocto Chromebook proof-of-concept image"
DESCRIPTION = "Minimal proof-of-concept image for Chromebook hardware bring-up."
LICENSE = "MIT"

inherit core-image

YOCTO_CHROMEBOOK_POC_PACKAGES ?= "\
    packagegroup-yocto-chromebook-poc \
"

IMAGE_INSTALL:append = " ${YOCTO_CHROMEBOOK_POC_PACKAGES}"

# Keep the POC image useful for first-boot diagnosis without enabling an SSH
# server by default. Remote-login policy can be added deliberately when the
# installer/deployment milestone defines credentials and exposure rules.
IMAGE_FEATURES:append = " read-only-rootfs"
