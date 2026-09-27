SUMMARY = "Mozilla Firefox ESR prebuilt binary"
DESCRIPTION = "Pinned Mozilla Firefox ESR x86-64 binary for the Yocto Chromebook desktop."
HOMEPAGE = "https://www.mozilla.org/firefox/enterprise/"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MPL-2.0;md5=815ca599c9df247a0c7f619bab123dad"

SRC_URI = "\
    https://archive.mozilla.org/pub/firefox/releases/${PV}/linux-x86_64/en-US/firefox-${PV}.tar.xz \
    file://firefox-wrapper \
    file://firefox.desktop \
    file://policies.json \
"
SRC_URI[sha256sum] = "8c36ca21beddcf09261661a74236b75a24a39c9a7c3193f812ca519e77c7c6d8"

S = "${WORKDIR}/firefox"

COMPATIBLE_HOST = "x86_64.*-linux"
PACKAGE_ARCH = "${TUNE_PKGARCH}"

RDEPENDS:${PN} += "\
    gtk+3 \
    dbus-glib \
    ca-certificates \
    fontconfig \
"

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INSANE_SKIP:${PN} += "already-stripped ldflags"

do_install() {
    install -d ${D}${libdir}/firefox
    cp -a ${S}/. ${D}${libdir}/firefox/

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/firefox-wrapper ${D}${bindir}/firefox
    sed -i -e 's|@LIBDIR@|${libdir}|g' ${D}${bindir}/firefox

    install -d ${D}${datadir}/applications
    install -m 0644 ${WORKDIR}/firefox.desktop ${D}${datadir}/applications/firefox.desktop

    install -d ${D}${libdir}/firefox/distribution
    install -m 0644 ${WORKDIR}/policies.json ${D}${libdir}/firefox/distribution/policies.json

    if [ -f ${S}/browser/chrome/icons/default/default128.png ]; then
        install -d ${D}${datadir}/icons/hicolor/128x128/apps
        install -m 0644 ${S}/browser/chrome/icons/default/default128.png \
            ${D}${datadir}/icons/hicolor/128x128/apps/firefox.png
    fi
}

FILES:${PN} += "\
    ${libdir}/firefox \
    ${datadir}/applications/firefox.desktop \
    ${datadir}/icons/hicolor/128x128/apps/firefox.png \
"
