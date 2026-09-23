SUMMARY = "Yocto Chromebook Bluetooth baseline"
DESCRIPTION = "Bluetooth userspace packages for adapter enumeration and bring-up testing."
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = "\
    bluez5 \
"
