SUMMARY = "Yocto Chromebook desktop package baseline"
DESCRIPTION = "Packages currently resolved by the selected scarthgap desktop layer set."
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = "\
    labwc \
    xwayland \
    vlc \
    sddm \
    lxqt-session \
    lxqt-panel \
    lxqt-powermanagement \
    lxqt-config \
    pcmanfm-qt \
    qterminal \
    yocto-chromebook-lxqt-labwc-session \
    firefox-esr-bin \
"
