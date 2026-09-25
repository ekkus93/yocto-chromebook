SUMMARY = "Yocto Chromebook conservative audio safety policy"
DESCRIPTION = "Installs the mute-first audio safety policy and manual helper for early Chromebook audio bring-up."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://audio-safety-policy.conf \
    file://yocto-chromebook-audio-safe-startup \
"

S = "${WORKDIR}"

inherit allarch

do_install() {
    install -d ${D}${sysconfdir}/yocto-chromebook
    install -m 0644 ${WORKDIR}/audio-safety-policy.conf ${D}${sysconfdir}/yocto-chromebook/audio-safety-policy.conf

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/yocto-chromebook-audio-safe-startup ${D}${bindir}/yocto-chromebook-audio-safe-startup
}
