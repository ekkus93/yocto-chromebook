SUMMARY = "Yocto Chromebook proof-of-concept image"
DESCRIPTION = "Minimal proof-of-concept image for Chromebook hardware bring-up."
LICENSE = "MIT"

inherit core-image

YOCTO_CHROMEBOOK_POC_PACKAGES ?= ""

IMAGE_INSTALL:append = " ${YOCTO_CHROMEBOOK_POC_PACKAGES}"
