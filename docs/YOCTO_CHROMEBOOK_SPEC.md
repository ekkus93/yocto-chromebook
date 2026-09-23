# Yocto Chromebook Specification

## 1. Purpose

`yocto-chromebook` is a Yocto/OpenEmbedded project for building compact, reproducible Linux images for Intel Chromebooks that have been converted to standard UEFI boot using MrChromebox firmware.

The immediate goal is a proof-of-concept image for two HP Chromebook 11 variants, followed by a reusable architecture for additional Intel Chromebooks.

The project is intentionally not a general-purpose Debian replacement with an end-user package manager. It is an appliance-style Chromebook Linux platform:

```text
System packages       -> Yocto image
Desktop/base apps     -> Yocto image
Optional user apps    -> AppImage in /data/apps
User/application data -> /data
OS updates            -> whole-image updates
```

## 2. Initial hardware targets

The first board targets are:

| Board | Representative device | Intel generation | Notes |
| --- | --- | --- | --- |
| `snappy` | HP Chromebook 11 G6 EE-family devices | Apollo Lake | Cr50, legacy/pre-Groot recovery UI, known Chromebook audio caveats |
| `vorticon` | HP Chromebook 11 G8 EE Intel | Gemini Lake | Cr50, target device for the published Debian/SuzyQ conversion guide |

The exact retail device strings vary by model. Board identity and MrChromebox compatibility must be verified before firmware conversion or image qualification.

## 3. Expansion model

The project should support multiple Intel Chromebooks through a shared distro/image definition plus per-board BSP configuration:

```text
common Yocto distro
        +
shared Intel Chromebook BSP layer
        +
per-board machine configuration
```

A single shared desktop image recipe should be buildable for multiple machines:

```bash
MACHINE=snappy bitbake yocto-chromebook-desktop
MACHINE=vorticon bitbake yocto-chromebook-desktop
```

The resulting binaries are machine-specific because the kernel, firmware, audio topology, and machine configuration differ, but the userspace policy and desktop stack remain shared.

Do not attempt a giant universal Chromebook image in v1. Universal hardware support would pull in unnecessary modules, firmware, and testing complexity. The v1 architecture favors small, qualified machine-specific builds.

## 4. Repository layout

Target layout:

```text
yocto-chromebook/
├── docs/
│   ├── YOCTO_CHROMEBOOK_SPEC.md
│   └── YOCTO_CHROMEBOOK_POC_TODO.md
├── kas/
│   ├── snappy-poc.yml
│   ├── snappy-desktop.yml
│   ├── vorticon-poc.yml
│   └── vorticon-desktop.yml
├── meta-yocto-chromebook/
│   ├── conf/
│   │   ├── layer.conf
│   │   ├── distro/
│   │   │   └── yocto-chromebook.conf
│   │   └── machine/
│   │       ├── include/
│   │       │   ├── intel-apollolake-chromebook.inc
│   │       │   └── intel-geminilake-chromebook.inc
│   │       ├── snappy.conf
│   │       └── vorticon.conf
│   ├── recipes-core/
│   ├── recipes-desktop/
│   ├── recipes-bsp/
│   ├── recipes-kernel/
│   ├── recipes-multimedia/
│   └── recipes-support/
├── scripts/
└── README.md
```

The repository should avoid committing generated Yocto build output. Build workdirs, downloads, sstate, rootfs artifacts, and images are ignored by `.gitignore`.

## 5. Build reproducibility

Use `kas` as the primary build entrypoint. A new developer or CI worker should not need to manually clone Poky and layers.

Each kas file should pin:

- Poky/OE-Core branch or revision
- meta-openembedded layers
- desktop/multimedia layers needed for LXQt, VLC, PipeWire, and supporting software
- browser layer or Firefox recipe source
- this repository layer
- target `MACHINE`
- target image recipe

Initial expected invocations:

```bash
kas build kas/snappy-poc.yml
kas build kas/vorticon-poc.yml
kas build kas/snappy-desktop.yml
kas build kas/vorticon-desktop.yml
```

The POC may start with fewer kas files if needed, but the final repository structure should support the four commands above.

## 6. Toolchain and C library policy

Use:

- `glibc`
- `systemd`
- standard Linux userspace compatibility assumptions

Do not use `musl` for the primary images. AppImage compatibility is a major project goal, and most third-party x86-64 AppImages assume a conventional glibc-based environment.

## 7. Image targets

Define at least two image recipes.

### 7.1 `yocto-chromebook-poc`

Purpose: hardware bring-up and validation.

Expected scope:

- bootable kernel/rootfs
- systemd
- shell/console access
- networking enough for validation
- SSH client; SSH server may be present but disabled by default
- essential diagnostics
- eMMC visibility
- Wi-Fi/Bluetooth bring-up support as soon as practical
- audio stack may be present but internal speakers should not be enabled until board-safe
- no full desktop required

### 7.2 `yocto-chromebook-desktop`

Purpose: usable lightweight Chromebook desktop.

Expected scope:

- everything from `yocto-chromebook-poc`
- Wayland-first graphical stack
- LXQt desktop environment
- Labwc compositor
- SDDM login manager
- XWayland compatibility
- PCManFM-Qt file manager
- QTerminal
- Firefox
- VLC
- nano
- AppImage/FUSE compatibility

## 8. Desktop decision

The default desktop is locked as:

```text
LXQt + Labwc on Wayland
```

Rationale:

- LXQt is lightweight and familiar.
- Labwc is a lightweight Wayland compositor suitable for LXQt's Wayland session.
- Wayland-first keeps the platform modern.
- XWayland remains available for legacy applications and AppImages.

Required desktop components:

- LXQt session packages
- Labwc
- SDDM
- PCManFM-Qt
- QTerminal
- LXQt panel
- LXQt power management
- LXQt configuration tools
- LXQt archiver or equivalent lightweight archive support
- XWayland

## 9. Base applications

The desktop image must include:

- Firefox
- VLC
- QTerminal
- PCManFM-Qt
- nano
- screen
- htop
- ncdu

Firefox and VLC are base-image applications, not user-installed AppImages. They are important enough that the default desktop should be usable immediately after flashing.

Nano is the default text editor. The image should set:

```bash
EDITOR=nano
VISUAL=nano
```

## 10. Base utilities

The base utility set should include at least:

```text
bash
coreutils
util-linux
curl
wget
ca-certificates
tar
gzip
xz
unzip
iproute2
ethtool
pciutils
usbutils
procps
less
nano
screen
htop
ncdu
openssh-client
```

These utilities should be present in both POC and desktop images unless a build constraint forces a temporary POC exception.

## 11. Package-manager policy

Do not design this as a miniature Debian.

End users should not rely on `apt`, RPM, IPK, or a public package feed for normal application installation. Yocto package management may be useful internally or during development, but the production model is whole-image OS updates plus AppImage for optional user applications.

System-owned components:

- kernel
- firmware
- boot components
- system libraries
- system services
- desktop environment
- Firefox
- VLC
- hardware enablement

User-owned optional applications:

- AppImages stored under `/data/apps`
- user files under `/data/home` or equivalent persistent structure

## 12. AppImage policy

AppImage compatibility is a first-class requirement.

The base image should include the compatibility pieces expected by common x86-64 AppImages:

```text
glibc
libstdc++
FUSE support
libfuse compatibility as required
Wayland
XWayland
D-Bus
fontconfig
common fonts
GTK runtime support where practical
common graphics libraries
Mesa / DRM / EGL / OpenGL
```

Target user application layout:

```text
/data/
├── apps/
├── home/
├── downloads/
├── config/
└── logs/
```

A later milestone may add a lightweight AppImage installer/registrar such as:

```bash
appinstall ~/Downloads/Example-x86_64.AppImage
```

Expected behavior:

1. move/copy AppImage to `/data/apps`
2. mark executable
3. extract icon and `.desktop` metadata if available
4. add to the application launcher
5. preserve user-installed app state across OS updates

## 13. Storage model

The project is optimized for 16 GB eMMC Chromebooks.

The intended production layout is:

```text
EFI System Partition     128 MB target
rootfs-A                 512 MB to 1024 MB target
rootfs-B                 512 MB to 1024 MB target
/data                    remainder
```

For early POC work, a simpler single-rootfs layout is acceptable:

```text
EFI System Partition
rootfs
/data or normal writable rootfs
```

The production direction is immutable or effectively immutable rootfs plus persistent `/data`.

## 14. Update model

The intended production update design is A/B whole-image updates:

```text
currently booted rootfs-A
        -> write new image to rootfs-B
        -> verify hash/signature
        -> mark B bootable
        -> reboot
        -> fallback to A if boot fails
```

A/B updates are not required for the first boot POC, but the filesystem layout and repository design should not block them.

## 15. Graphics policy

Graphics stack:

- Intel DRM/KMS
- Mesa
- Wayland
- Labwc
- XWayland
- LXQt session

Acceptance requirements:

- native panel resolution detected
- graphical session starts reliably
- keyboard/touchpad usable in session
- Firefox launches under Wayland or acceptable fallback
- VLC launches and displays video
- XWayland applications can run
- suspend/resume does not permanently break graphics

## 16. Networking policy

Base networking stack:

- NetworkManager
- wpa_supplicant or iwd, with NetworkManager integration
- BlueZ for Bluetooth
- ca-certificates
- DNS and DHCP support via the chosen NetworkManager configuration

Acceptance requirements:

- Wi-Fi device detected
- scan works
- WPA2/WPA3 network association works where supported by hardware
- DHCP obtains an address
- DNS works
- HTTPS works
- Bluetooth controller initializes
- Bluetooth pairing validation is a later milestone if not available during POC

## 17. Audio policy and speaker safety

Chromebook audio is board-specific BSP work, not a generic desktop package checkbox.

The shared stack is:

```text
machine config
    -> AVS or SOF selection
    -> codec / amplifier drivers
    -> firmware + topology
    -> ALSA UCM2
    -> PipeWire + WirePlumber
    -> LXQt volume controls
```

The project must treat internal speaker enablement conservatively.

### 17.1 Known risk class

Some Intel Chromebooks use amplifier/topology combinations such as `MAX98357A` that can be unsafe if routed or limited incorrectly. A wrong topology or unrestricted volume path can risk damaging internal speakers.

### 17.2 Safety policy

For any newly supported board:

- internal speakers are considered unqualified until tested
- headphone, HDMI, USB-C, or USB audio may be validated separately
- internal speaker output must start at low volume only
- volume limits must be validated
- mute/unmute must be validated
- suspend/resume must not produce pops, blasts, or stuck high volume
- no board may be marked audio-qualified while a known speaker-damage risk remains

### 17.3 Board-specific audio requirements

For each board, document:

- Intel audio path: AVS or SOF
- codec and amplifier devices
- required kernel options/modules
- DSP firmware files
- topology files
- ALSA UCM2 profile
- PipeWire/WirePlumber quirks
- safe initial mixer state
- maximum safe volume policy if known

## 18. Power management policy

Required areas:

- battery status
- AC/charger detection
- lid close/open behavior
- screen brightness keys
- suspend/resume
- wake from keyboard/power/lid as appropriate
- no boot-blocking waits on missing hardware

Early POC may accept known power-management gaps if they are documented and do not risk hardware damage or data loss.

## 19. Firmware and boot assumptions

The images target Chromebooks already converted to MrChromebox UEFI Full ROM firmware.

The project does not own the ChromeOS-to-UEFI conversion process. However, project documentation may link or refer to the separate conversion guide.

Boot assumptions:

- standard UEFI boot
- GPT partitioning
- normal EFI System Partition
- bootloader selected during Yocto integration
- Debian/ChromeOS dual-boot is not a v1 goal

## 20. Security posture

Initial POC security is pragmatic, but production design should favor:

- minimal exposed services
- SSH server disabled by default unless specifically needed
- immutable or mostly immutable rootfs
- signed update path in a later milestone
- no end-user package manager by default
- persistent user data isolated under `/data`

## 21. Size and performance goals

Because Firefox, VLC, Qt/LXQt, Mesa, PipeWire, and XWayland are included, the desktop image is expected to be larger than a minimal embedded image.

Initial measurement targets rather than hard gates:

| Metric | POC target | Desktop target |
| --- | ---: | ---: |
| Compressed image size | Measure first | Measure first |
| Installed rootfs size | < 512 MB desired | < 1.5 GB desired initially |
| Idle RAM, console | Measure first | N/A |
| Idle RAM, desktop | N/A | Measure first |
| Boot to console | Measure first | N/A |
| Boot to LXQt | N/A | Measure first |

Hard limits should be set after the first successful image builds and boots on target hardware.

## 22. Qualification principle

Every board must have a hardware qualification record before being called supported.

Minimum qualification areas:

- boots from MrChromebox UEFI
- reaches console login
- reaches graphical login for desktop image
- internal eMMC visible
- keyboard works
- touchpad works
- Wi-Fi works
- Bluetooth initializes
- Intel graphics works
- USB-A works
- USB-C works at least for basic devices/charging expectations where applicable
- battery status works
- suspend/resume tested
- audio path identified
- internal speaker safety assessed before enabling speakers
- Firefox launches and loads HTTPS
- VLC launches and plays local media
- x86-64 AppImage runs
- base utilities present

## 23. Non-goals for v1

The following are not v1 goals:

- universal one-image-fits-all Chromebook support
- ARM Chromebook support
- AMD Chromebook support
- ChromeOS dual-boot support
- a public package repository for arbitrary end-user packages
- full developer workstation image with compilers and headers by default
- solving every Chromebook audio quirk before first boot POC
- supporting machines without MrChromebox UEFI unless explicitly added later

## 24. Decision log

Locked initial decisions:

- Repo: `ekkus93/yocto-chromebook`
- Build system: Yocto/OpenEmbedded
- Build orchestrator: kas
- C library: glibc
- Init/service manager: systemd
- Desktop: LXQt
- Wayland compositor: Labwc
- Display manager: SDDM
- File manager: PCManFM-Qt
- Terminal: QTerminal
- Browser: Firefox in base desktop image
- Media player: VLC in base desktop image
- CLI editor: nano
- Diagnostics: screen, htop, ncdu
- Optional application model: AppImage under `/data/apps`
- Persistent data model: `/data`
- Update direction: A/B whole-image updates
- Audio: board-specific BSP qualification with speaker-safety gate
