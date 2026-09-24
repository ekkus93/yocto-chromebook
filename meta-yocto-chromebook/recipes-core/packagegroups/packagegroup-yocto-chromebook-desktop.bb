SUMMARY = "Yocto Chromebook desktop package baseline"
DESCRIPTION = "Packages currently resolved by the selected scarthgap desktop layer set."
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = "\
    labwc \
    xwayland \
    vlc \
"
