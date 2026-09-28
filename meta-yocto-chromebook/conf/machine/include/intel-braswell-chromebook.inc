# Common policy for Intel Braswell Chromebook targets converted to MrChromebox UEFI.
#
# The first consumer is KEFKA, used by Dell Chromebook 11 3180/3189-class
# hardware. Keep board-specific assumptions in the machine .conf file and use
# this include only for shared Braswell/x86_64 bring-up defaults.

PREFERRED_PROVIDER_virtual/kernel ?= "linux-yocto"

# Braswell Chromebooks boot through the same UEFI-oriented x86_64 path used by
# the existing converted Chromebook targets in this project.
MACHINE_FEATURES:append = " efi keyboard pci screen serial usbhost wifi bluetooth alsa"

# Early POC images should produce UEFI-bootable disk images. The exact
# partitioning policy is refined in later storage and A/B-update milestones.
IMAGE_FSTYPES += "wic wic.gz"
WKS_FILE ?= "efi-bootdisk.wks.in"

# Keep broad firmware available for first boot while board evidence identifies
# the exact Wi-Fi, Bluetooth, audio, and other device firmware requirements.
MACHINE_EXTRA_RRECOMMENDS:append = " linux-firmware linux-firmware-iwlwifi"

# Keep the initial image generic x86-64 until hardware-specific kernel,
# firmware, topology, and UCM2 requirements are qualified per board.
SERIAL_CONSOLES ?= "115200;ttyS0"

# i915 is the expected Intel display stack. Runtime DRM/KMS evidence remains a
# hardware qualification item rather than a build-time claim.
KERNEL_MODULE_AUTOLOAD:append = " i915"

# Braswell Chromebook family baseline for the `kefka` machine.
INTEL_CHROMEBOOK_SOC_FAMILY = "braswell"
