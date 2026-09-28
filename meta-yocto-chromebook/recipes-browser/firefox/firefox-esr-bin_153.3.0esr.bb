SUMMARY = "Firefox ESR binary browser for Yocto Chromebook desktop images"
DESCRIPTION = "Mozilla Firefox ESR x86-64 binary repackaged for the Yocto Chromebook desktop image."
HOMEPAGE = "https://www.mozilla.org/firefox/enterprise/"
LICENSE = "CLOSED"

SRC_URI = "\
    https://ftp.mozilla.org/pub/firefox/releases/${PV}/linux-x86_64/en-US/firefox-${PV}.tar.xz;name=firefox \
    file://firefox-wrapper \
    file://firefox.desktop \
    file://policies.json \
"
SRC_URI[firefox.sha256sum] = "8c36ca21beddcf09261661a74236b75a24a39c9a7c3193f812ca519e77c7c6d8"

S = "${WORKDIR}/firefox"

COMPATIBLE_HOST = "x86_64.*-linux"
PACKAGE_ARCH = "${TUNE_PKGARCH}"

# The upstream archive is a prebuilt browser bundle. Keep QA checks that are
# meaningful for local recipes elsewhere, but do not fail this package because
# Mozilla ships pre-stripped binaries and bundled shared objects.
INSANE_SKIP:${PN} += "already-stripped file-rdeps ldflags textrel dev-so"

RDEPENDS:${PN} += "ca-certificates"

 do_install() {
    install -d ${D}${libdir}
    cp -a ${S} ${D}${libdir}/firefox

    install -d ${D}${bindir}
    sed -e 's|@LIBDIR@|${libdir}|g' ${WORKDIR}/firefox-wrapper > ${D}${bindir}/firefox
    chmod 0755 ${D}${bindir}/firefox

    install -d ${D}${datadir}/applications
    install -m 0644 ${WORKDIR}/firefox.desktop ${D}${datadir}/applications/firefox.desktop

    install -d ${D}${libdir}/firefox/distribution
    install -m 0644 ${WORKDIR}/policies.json ${D}${libdir}/firefox/distribution/policies.json
}

FILES:${PN} += "\
    ${bindir}/firefox \
    ${libdir}/firefox \
    ${datadir}/applications/firefox.desktop \
"
