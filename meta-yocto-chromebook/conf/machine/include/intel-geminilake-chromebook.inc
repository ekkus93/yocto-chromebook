# Shared Intel Chromebook policy for MrChromebox UEFI-converted devices.

PREFERRED_PROVIDER_virtual/kernel ?= "linux-yocto"

MACHINE_FEATURES:append = " efi keyboard pci screen serial usbhost wifi bluetooth alsa"

# Early POC images should produce UEFI-bootable disk images. The exact
# partitioning policy is refined in later storage and A/B-update milestones.
IMAGE_FSTYPES += "wic wic.gz"
WKS_FILE ?= "efi-bootdisk.wks.in"

# Keep the initial image generic x86-64 until hardware-specific kernel,
# firmware, topology, and UCM2 requirements are qualified per board.
SERIAL_CONSOLES ?= "115200;ttyS0"

# Gemini Lake Chromebook family baseline for the `vorticon` machine.
INTEL_CHROMEBOOK_SOC_FAMILY = "geminilake"
