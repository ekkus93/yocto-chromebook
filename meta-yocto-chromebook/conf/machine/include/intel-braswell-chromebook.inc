# Common policy for Intel Braswell Chromebook targets converted to MrChromebox UEFI.
#
# The first consumer is KEFKA, used by Dell Chromebook 11 3180/3189-class
# hardware. Keep board-specific assumptions in the machine .conf file and use
# this include only for shared Braswell/x86_64 bring-up defaults.

require conf/machine/genericx86-64.conf

MACHINEOVERRIDES =. "intel-braswell-chromebook:"

# Braswell Chromebooks boot through the same UEFI-oriented x86_64 path used by
# the existing converted Chromebook targets in this project.
MACHINE_FEATURES:append = " efi keyboard pci usbhost wifi bluetooth"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-yocto"
PREFERRED_VERSION_linux-yocto ?= "6.6%"

# Keep broad firmware available for first boot while board evidence identifies
# the exact Wi-Fi, Bluetooth, audio, and other device firmware requirements.
MACHINE_EXTRA_RRECOMMENDS:append = " linux-firmware linux-firmware-iwlwifi"

# i915 is the expected Intel display stack. Runtime DRM/KMS evidence remains a
# hardware qualification item rather than a build-time claim.
KERNEL_MODULE_AUTOLOAD:append = " i915"
