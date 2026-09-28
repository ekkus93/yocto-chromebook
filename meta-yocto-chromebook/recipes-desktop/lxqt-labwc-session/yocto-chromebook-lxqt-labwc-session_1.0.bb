SUMMARY = "Yocto Chromebook LXQt + Labwc session integration"
DESCRIPTION = "SDDM Wayland session and first-boot defaults for the LXQt-on-Labwc desktop."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://lxqt-labwc.desktop \
    file://labwc-autostart \
    file://start-lxqt-labwc \
    file://yocto-chromebook-seed-sddm-session \
    file://yocto-chromebook-sddm-default.service \
"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "yocto-chromebook-sddm-default.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

RDEPENDS:${PN} = "\
    labwc \
    sddm \
    xwayland \
    qtwayland \
    lxqt-panel \
    lxqt-powermanagement \
    pcmanfm-qt \
"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/start-lxqt-labwc ${D}${bindir}/start-lxqt-labwc

    install -d ${D}${datadir}/wayland-sessions
    install -m 0644 ${WORKDIR}/lxqt-labwc.desktop ${D}${datadir}/wayland-sessions/lxqt-labwc.desktop

    install -d ${D}${sysconfdir}/xdg/labwc
    install -m 0755 ${WORKDIR}/labwc-autostart ${D}${sysconfdir}/xdg/labwc/autostart

    install -d ${D}${libexecdir}
    install -m 0755 ${WORKDIR}/yocto-chromebook-seed-sddm-session ${D}${libexecdir}/yocto-chromebook-seed-sddm-session

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/yocto-chromebook-sddm-default.service ${D}${systemd_system_unitdir}/yocto-chromebook-sddm-default.service
}

FILES:${PN} += "\
    ${datadir}/wayland-sessions/lxqt-labwc.desktop \
    ${sysconfdir}/xdg/labwc/autostart \
    ${libexecdir}/yocto-chromebook-seed-sddm-session \
"
