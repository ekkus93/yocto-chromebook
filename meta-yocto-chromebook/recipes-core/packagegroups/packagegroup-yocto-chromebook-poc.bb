SUMMARY = "Yocto Chromebook POC package baseline"
DESCRIPTION = "Base utilities and bring-up packages for the Yocto Chromebook proof-of-concept image."
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = "\
    bash \
    coreutils \
    util-linux \
    curl \
    wget \
    ca-certificates \
    tar \
    gzip \
    xz \
    unzip \
    iproute2 \
    ethtool \
    pciutils \
    usbutils \
    procps \
    less \
    nano \
    screen \
    htop \
    ncdu \
    openssh-ssh \
    packagegroup-core-boot \
    kernel-modules \
    linux-firmware \
    networkmanager \
    packagegroup-yocto-chromebook-bluetooth \
    packagegroup-yocto-chromebook-graphics \
    packagegroup-yocto-chromebook-appimage \
"
