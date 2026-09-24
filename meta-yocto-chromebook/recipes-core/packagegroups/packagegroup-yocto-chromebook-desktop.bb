SUMMARY = "Yocto Chromebook LXQt desktop package baseline"
DESCRIPTION = "LXQt, Labwc, login manager, terminal, file manager, and desktop utility packages."
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = "\
    labwc \
    xwayland \
    sddm \
    lxqt-session \
    lxqt-panel \
    lxqt-powermanagement \
    lxqt-config \
    pcmanfm-qt \
    qterminal \
    vlc \
"
