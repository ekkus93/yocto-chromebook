SUMMARY = "Yocto Chromebook desktop image"
DESCRIPTION = "Desktop image placeholder for LXQt + Labwc Chromebook builds."
LICENSE = "MIT"

inherit core-image

YOCTO_CHROMEBOOK_POC_PACKAGES ?= ""
YOCTO_CHROMEBOOK_DESKTOP_PACKAGES ?= ""

IMAGE_INSTALL:append = " ${YOCTO_CHROMEBOOK_POC_PACKAGES} ${YOCTO_CHROMEBOOK_DESKTOP_PACKAGES}"
