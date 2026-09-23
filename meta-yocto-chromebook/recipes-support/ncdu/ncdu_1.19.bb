SUMMARY = "NCurses disk usage viewer"
DESCRIPTION = "ncdu is an ncurses-based disk usage viewer used by the Yocto Chromebook POC diagnostics baseline."
HOMEPAGE = "https://dev.yorhel.nl/ncdu"
SECTION = "console/utils"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "https://dev.yorhel.nl/download/ncdu-${PV}.tar.gz"
SRC_URI[sha256sum] = "f4452faa69887dfe4203691a8334b069defdd522a9ce6ddda6458aba89fa4765"

DEPENDS = "ncurses"

inherit autotools pkgconfig
