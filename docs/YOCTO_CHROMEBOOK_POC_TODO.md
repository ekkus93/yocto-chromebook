# Yocto Chromebook POC TODO

This is the authoritative implementation checklist for the initial `yocto-chromebook` proof of concept.

The TODO is intentionally milestone-oriented so future autonomous work can advance the project without rediscovering the architecture.

## Rules of engagement

- Use `docs/YOCTO_CHROMEBOOK_SPEC.md` as the architecture source of truth.
- Keep SNAPPY and VORTICON support in the same project architecture, not separate OS forks.
- Prefer shared includes and common recipes; put only hardware-specific requirements in machine configs.
- Do not enable unsafe internal speaker output just to claim audio support.
- Do not add an end-user package manager as the application strategy.
- Keep generated Yocto build output out of Git.
- Record evidence for each hardware qualification item.
- Treat unknown hardware behavior as an explicit investigation item, not as a reason to block repo scaffolding.

## M0 — Repository bootstrap

### Goals

Create the initial project structure and documentation.

### Tasks

- [x] Create Yocto-specific `.gitignore`.
- [x] Create `docs/YOCTO_CHROMEBOOK_SPEC.md`.
- [x] Create `docs/YOCTO_CHROMEBOOK_POC_TODO.md`.
- [x] Create `README.md` with project purpose, supported-target status, and quick-start placeholder.
- [x] Create initial directory skeleton:
  - [x] `kas/`
  - [x] `meta-yocto-chromebook/`
  - [x] `meta-yocto-chromebook/conf/`
  - [x] `meta-yocto-chromebook/conf/distro/`
  - [x] `meta-yocto-chromebook/conf/machine/`
  - [x] `meta-yocto-chromebook/conf/machine/include/`
  - [x] `meta-yocto-chromebook/recipes-core/`
  - [x] `meta-yocto-chromebook/recipes-desktop/`
  - [x] `meta-yocto-chromebook/recipes-bsp/`
  - [x] `meta-yocto-chromebook/recipes-kernel/`
  - [x] `meta-yocto-chromebook/recipes-multimedia/`
  - [x] `meta-yocto-chromebook/recipes-support/`
  - [x] `scripts/`

### Acceptance criteria

- [x] Fresh clone contains the spec and TODO.
- [x] Git ignores Yocto generated output.
- [x] Repository layout supports adding kas configs and a real Yocto layer.

## M1 — Yocto layer skeleton

### Goals

Create a valid `meta-yocto-chromebook` layer that can be added to a Yocto build.

### Tasks

- [x] Add `meta-yocto-chromebook/conf/layer.conf`.
- [x] Define layer compatibility policy.
- [x] Add initial distro config:
  - [x] `meta-yocto-chromebook/conf/distro/yocto-chromebook.conf`
- [x] Configure distro-level defaults:
  - [x] glibc
  - [x] systemd
  - [x] Wayland-first graphics policy
  - [x] package features needed for AppImage/FUSE compatibility
  - [x] no unnecessary debug/development packages in production images
- [x] Add image recipe placeholders:
  - [x] `yocto-chromebook-poc`
  - [x] `yocto-chromebook-desktop`

### Acceptance criteria

- [x] Layer parses in a minimal Yocto environment.
- [x] Distro config can be selected without parse failure.
- [x] Empty or placeholder image recipes are structured for later package additions.

## M2 — kas bootstrap

### Goals

Make builds reproducible through kas.

### Tasks

- [x] Add `kas/snappy-poc.yml`.
- [x] Add `kas/vorticon-poc.yml`.
- [x] Add `kas/snappy-desktop.yml`.
- [x] Add `kas/vorticon-desktop.yml`.
- [x] Pin Poky/OE-Core branch or revision.
- [x] Add required upstream layers.
- [x] Add this repository layer.
- [x] Select appropriate `MACHINE`, `DISTRO`, and image target per kas file.
- [x] Document kas installation and invocation in `README.md`.

### Acceptance criteria

- [x] `kas dump` succeeds for each file.
- [x] `kas build` reaches BitBake parsing for at least one POC target.
- [x] Missing upstream layer or branch assumptions are documented.

## M3 — Machine config: SNAPPY

### Goals

Add first-board support for SNAPPY / Apollo Lake Chromebook hardware.

### Tasks

- [x] Create `meta-yocto-chromebook/conf/machine/snappy.conf`.
- [x] Create or use `include/intel-apollolake-chromebook.inc`.
- [x] Identify kernel baseline suitable for SNAPPY.
- [ ] Identify required firmware packages/blobs.
- [x] Document Wi-Fi chipset expectation.
- [x] Document Bluetooth expectation.
- [x] Document audio codec/amplifier expectation.
- [x] Document internal storage device expectations.
- [x] Document known recovery/firmware assumptions: MrChromebox UEFI Full ROM.

### Acceptance criteria

- [x] `MACHINE=snappy` parses.
- [x] SNAPPY image build starts without unresolved machine include errors.
- [x] Board-specific unknowns are tracked in this TODO or a hardware notes file.

## M4 — Machine config: VORTICON

### Goals

Add second-board support for VORTICON / Gemini Lake Chromebook hardware.

### Tasks

- [x] Create `meta-yocto-chromebook/conf/machine/vorticon.conf`.
- [x] Create or use `include/intel-geminilake-chromebook.inc`.
- [x] Identify kernel baseline suitable for VORTICON.
- [ ] Identify required firmware packages/blobs.
- [x] Document Wi-Fi chipset expectation.
- [x] Document Bluetooth expectation.
- [x] Document audio codec/amplifier expectation.
- [x] Document internal storage device expectations.
- [x] Document known recovery/firmware assumptions: MrChromebox UEFI Full ROM.

### Acceptance criteria

- [x] `MACHINE=vorticon` parses.
- [x] VORTICON image build starts without unresolved machine include errors.
- [x] Board-specific unknowns are tracked in this TODO or a hardware notes file.

## M5 — POC image package baseline

### Goals

Define the minimal useful POC image.

### Tasks

- [x] Add base packages:
  - [x] bash
  - [x] coreutils
  - [x] util-linux
  - [x] curl
  - [x] wget
  - [x] ca-certificates
  - [x] tar
  - [x] gzip
  - [x] xz
  - [x] unzip
  - [x] iproute2
  - [x] ethtool
  - [x] pciutils
  - [x] usbutils
  - [x] procps
  - [x] less
  - [x] nano
  - [x] screen
  - [x] htop
  - [x] ncdu
  - [x] openssh-client
- [x] Include systemd services needed for boot.
- [x] Include NetworkManager or selected network stack.
- [x] Include basic kernel module loading support.
- [x] Include firmware loading support.

### Acceptance criteria

- [ ] Image builds for at least one machine.
- [x] Rootfs package manifest is configured to contain required base utilities.
- [x] `nano`, `screen`, `htop`, and `ncdu` are present in the package baseline.

## M6 — Boot from MrChromebox UEFI

### Goals

Get the POC image booting from MrChromebox UEFI.

### Tasks

- [x] Select bootloader approach.
- [x] Ensure EFI System Partition contents are generated correctly.
- [ ] Generate bootable `.wic` image.
- [x] Document flashing/writing the image to USB or eMMC.
- [ ] Boot SNAPPY from external USB first.
- [ ] Boot VORTICON from external USB first.
- [ ] Record UEFI boot menu behavior.
- [ ] Record boot logs.

### Acceptance criteria

- [ ] SNAPPY reaches kernel boot log from MrChromebox UEFI.
- [ ] VORTICON reaches kernel boot log from MrChromebox UEFI.
- [ ] At least one board reaches a shell login.

## M7 — Internal storage and partitioning

### Goals

Validate eMMC visibility and define install layout.

### Tasks

- [ ] Confirm internal eMMC device name on SNAPPY.
- [ ] Confirm internal eMMC device name on VORTICON.
- [ ] Verify read/write access from POC image.
- [x] Define initial POC partition layout.
- [x] Define future A/B partition layout.
- [x] Add installer/deployment notes.

### Acceptance criteria

- [ ] Internal eMMC visible on SNAPPY.
- [ ] Internal eMMC visible on VORTICON.
- [ ] POC image can be installed or written in a documented way.
- [x] Production A/B layout remains feasible.

## M8 — Keyboard, touchpad, and input

### Goals

Validate laptop input devices.

### Tasks

- [ ] Confirm internal keyboard device appears.
- [ ] Confirm Chromebook top-row keys mapping state.
- [ ] Confirm touchpad device appears.
- [ ] Validate pointer movement.
- [ ] Validate tap/click behavior.
- [ ] Validate Ctrl/Alt/F-key access where relevant.
- [ ] Identify input quirks per board.

### Acceptance criteria

- [ ] Keyboard works at console on SNAPPY.
- [ ] Keyboard works at console on VORTICON.
- [ ] Touchpad works in graphical session once desktop is available.
- [ ] Required quirks are captured in machine config or documentation.

## M9 — Wi-Fi and networking

### Goals

Bring up networking reliably.

### Tasks

- [ ] Identify Wi-Fi chipset on SNAPPY.
- [ ] Identify Wi-Fi chipset on VORTICON.
- [ ] Include required firmware.
- [ ] Include NetworkManager configuration.
- [ ] Validate scan.
- [ ] Validate WPA2 network association.
- [ ] Validate DHCP.
- [ ] Validate DNS.
- [ ] Validate HTTPS with `curl`.

### Acceptance criteria

- [ ] SNAPPY connects to Wi-Fi and resolves DNS.
- [ ] VORTICON connects to Wi-Fi and resolves DNS.
- [ ] `curl -I https://example.com/` succeeds on both boards or documented equivalent.

## M10 — Bluetooth

### Goals

Initialize Bluetooth hardware.

### Tasks

- [ ] Include BlueZ.
- [ ] Include required firmware.
- [ ] Verify adapter enumeration.
- [ ] Verify `bluetoothctl list` output.
- [ ] Attempt scan.
- [ ] Document pairing test as optional if no devices are available.

### Acceptance criteria

- [ ] Bluetooth controller initializes on SNAPPY or limitation documented.
- [ ] Bluetooth controller initializes on VORTICON or limitation documented.

## M11 — Graphics and Wayland baseline

### Goals

Enable Intel graphics and a Wayland compositor path.

### Tasks

- [ ] Include Mesa.
- [ ] Include DRM/KMS support.
- [ ] Validate panel native resolution.
- [ ] Validate kernel modesetting.
- [ ] Add minimal Wayland compositor test if desktop is not ready.
- [ ] Record graphics logs.

### Acceptance criteria

- [ ] SNAPPY starts a Wayland compositor or documented minimal equivalent.
- [ ] VORTICON starts a Wayland compositor or documented minimal equivalent.
- [ ] No software-only graphics fallback unless explicitly documented as temporary.

## M12 — LXQt + Labwc desktop

### Goals

Build the user-facing desktop image.

### Tasks

- [ ] Add LXQt packages.
- [ ] Add Labwc.
- [ ] Add SDDM.
- [ ] Add PCManFM-Qt.
- [ ] Add QTerminal.
- [ ] Add LXQt panel.
- [ ] Add LXQt power management.
- [ ] Add LXQt configuration tools.
- [ ] Add XWayland.
- [ ] Configure default session to LXQt on Labwc.
- [ ] Validate login session.

### Acceptance criteria

- [ ] Desktop image builds for at least one target.
- [ ] SDDM or configured login path reaches LXQt session.
- [ ] QTerminal opens.
- [ ] PCManFM-Qt opens.
- [ ] Touchpad and keyboard work in session.

## M13 — Firefox base application

### Goals

Include Firefox in the desktop image.

### Tasks

- [ ] Select Firefox recipe/layer.
- [ ] Resolve build dependencies.
- [ ] Configure Wayland support.
- [ ] Ensure certificates are available.
- [ ] Launch Firefox in LXQt session.
- [ ] Load an HTTPS page.
- [ ] Record memory and startup time.

### Acceptance criteria

- [ ] Firefox launches from menu or terminal.
- [ ] HTTPS page loads.
- [ ] Wayland-native operation or fallback path is documented.

## M14 — VLC and multimedia baseline

### Goals

Include VLC in the desktop image and prove local media playback.

### Tasks

- [ ] Add VLC recipe/layer dependency.
- [ ] Include required multimedia plugins/codecs allowed by project policy.
- [ ] Validate video playback.
- [ ] Validate audio output only through safe output path initially.
- [ ] Record CPU usage during playback.

### Acceptance criteria

- [ ] VLC launches.
- [ ] Local video file plays.
- [ ] Audio path behavior is documented and does not bypass speaker-safety policy.

## M15 — AppImage compatibility

### Goals

Prove third-party x86-64 AppImage execution.

### Tasks

- [ ] Include FUSE support.
- [ ] Include libfuse compatibility if required.
- [ ] Include XWayland support.
- [ ] Include common runtime libraries needed by test AppImage.
- [ ] Create `/data/apps` convention.
- [ ] Select small known-good x86-64 graphical AppImage for testing.
- [ ] Run AppImage from `/data/apps`.
- [ ] Validate application appears in desktop environment or document manual launch path.

### Acceptance criteria

- [ ] At least one x86-64 AppImage launches successfully.
- [ ] Failure output for unsupported AppImages is documented.
- [ ] `/data/apps` policy is implemented or documented.

## M16 — Persistent `/data` design

### Goals

Separate OS image from persistent user/application data.

### Tasks

- [x] Define `/data` mount point.
- [x] Decide whether `/home` is symlinked/bind-mounted into `/data/home`.
- [x] Define `/data/apps`.
- [x] Define `/data/downloads`.
- [x] Define `/data/config` if needed.
- [x] Ensure `/data` survives rootfs replacement in design.
- [x] Document backup implications.

### Acceptance criteria

- [x] `/data` layout documented.
- [x] User files can survive rootfs replacement in planned layout.
- [x] AppImage storage path defined.

## M17 — Audio investigation and safety gate

### Goals

Identify audio hardware and define safe enablement per board.

### Tasks

- [ ] SNAPPY: identify Intel audio path: AVS or SOF.
- [ ] SNAPPY: identify codec/amplifier devices.
- [ ] SNAPPY: identify required kernel options/modules.
- [ ] SNAPPY: identify firmware/topology/UCM2 requirements.
- [ ] VORTICON: identify Intel audio path: AVS or SOF.
- [ ] VORTICON: identify codec/amplifier devices.
- [ ] VORTICON: identify required kernel options/modules.
- [ ] VORTICON: identify firmware/topology/UCM2 requirements.
- [ ] Determine whether either board uses MAX98357A or another speaker-risk amp path.
- [ ] Add conservative initial mixer policy.
- [ ] Validate PipeWire/WirePlumber enumeration.
- [ ] Validate headphones before internal speakers if possible.
- [ ] Validate microphone path.
- [ ] Validate HDMI/USB-C/USB audio where available.
- [ ] Validate internal speakers only at controlled low volume after topology review.
- [ ] Validate volume limits.
- [ ] Validate mute/unmute.
- [ ] Validate suspend/resume audio behavior.

### Acceptance criteria

- [ ] Audio hardware identity documented for SNAPPY.
- [ ] Audio hardware identity documented for VORTICON.
- [ ] Internal speakers are not enabled unsafely.
- [ ] No board is marked audio-qualified until speaker-safety risk is resolved.
- [ ] At least one safe audio output path works or limitations are explicitly documented.

## M18 — Suspend/resume and power management

### Goals

Make the machines usable as laptops.

### Tasks

- [ ] Validate battery status.
- [ ] Validate charger/AC status.
- [ ] Validate screen brightness control.
- [ ] Validate lid close behavior.
- [ ] Validate lid open wake behavior.
- [ ] Validate suspend from desktop.
- [ ] Validate resume from suspend.
- [ ] Run repeated suspend/resume cycles.
- [ ] Check Wi-Fi after resume.
- [ ] Check graphics after resume.
- [ ] Check input after resume.
- [ ] Check audio after resume only within safety policy.

### Acceptance criteria

- [ ] Basic suspend/resume works on SNAPPY or limitation documented.
- [ ] Basic suspend/resume works on VORTICON or limitation documented.
- [ ] No data-loss or hardware-risk behavior observed.

## M19 — Image size, RAM, and boot-time metrics

### Goals

Measure actual footprint before setting hard limits.

### Tasks

- [ ] Measure compressed POC image size.
- [ ] Measure installed POC rootfs size.
- [ ] Measure compressed desktop image size.
- [ ] Measure installed desktop rootfs size.
- [ ] Measure boot-to-console time.
- [ ] Measure boot-to-LXQt time.
- [ ] Measure idle RAM at console.
- [ ] Measure idle RAM in LXQt.
- [ ] Measure Firefox RAM after launch.
- [ ] Measure VLC playback CPU/RAM.
- [ ] Decide hard size/RAM targets after first successful builds.

### Acceptance criteria

- [ ] Metrics recorded for at least one board.
- [ ] Size/RAM targets updated in spec or TODO after evidence exists.

## M20 — Installer/deployment workflow

### Goals

Define how users get the Yocto image onto converted Chromebooks.

### Tasks

- [x] Document external USB boot workflow.
- [x] Document internal eMMC installation workflow.
- [x] Decide whether to provide installer script.
- [x] Add safety checks for target disk selection.
- [x] Preserve `/data` where applicable.
- [x] Document recovery procedure.

### Acceptance criteria

- [x] A tester can boot from USB using documented steps.
- [x] A tester can install to internal eMMC using documented steps or explicit manual process.
- [x] Disk-destructive actions require clear confirmation.

## M21 — A/B update design

### Goals

Design production-safe updates without requiring implementation in POC-1.

### Tasks

- [x] Define rootfs-A/rootfs-B partition scheme.
- [x] Select boot-state mechanism.
- [x] Define update artifact format.
- [x] Define verification mechanism.
- [x] Define rollback behavior.
- [x] Decide whether existing Yocto update frameworks are appropriate.
- [x] Document not-required-for-POC status.

### Acceptance criteria

- [x] A/B update design documented.
- [x] POC partition choices do not block future A/B updates.

## M22 — Hardware qualification matrix

### Goals

Create a persistent support matrix for each board.

### Tasks

- [ ] Add `docs/HARDWARE_MATRIX.md`.
- [ ] Include rows for SNAPPY and VORTICON.
- [ ] Track status values:
  - [ ] unknown
  - [ ] detected
  - [ ] works
  - [ ] partial
  - [ ] blocked
  - [ ] unsafe-disabled
- [ ] Track components:
  - [ ] UEFI boot
  - [ ] eMMC
  - [ ] keyboard
  - [ ] touchpad
  - [ ] Wi-Fi
  - [ ] Bluetooth
  - [ ] graphics
  - [ ] USB-A
  - [ ] USB-C
  - [ ] battery
  - [ ] brightness
  - [ ] suspend/resume
  - [ ] speakers
  - [ ] headphones
  - [ ] microphone
  - [ ] webcam
  - [ ] Firefox
  - [ ] VLC
  - [ ] AppImage

### Acceptance criteria

- [ ] Matrix exists.
- [ ] Every supported board has explicit component statuses.
- [ ] Unknowns are visible rather than implied supported.

## M23 — Documentation closeout for POC-1

### Goals

Make the first proof of concept repeatable.

### Tasks

- [ ] Update README quick-start.
- [x] Document supported hardware and limitations.
- [x] Document build prerequisites.
- [x] Document kas build commands.
- [x] Document writing image to USB.
- [x] Document booting from MrChromebox UEFI.
- [ ] Document known failures.
- [x] Document audio safety status.
- [x] Document AppImage workflow.
- [ ] Document current image size and RAM metrics.

### Acceptance criteria

- [ ] A technically competent tester can reproduce the current best build from a clean checkout.
- [ ] Known risks and unsupported features are explicitly documented.
- [ ] The next implementation milestone is obvious from this TODO.

## POC-1 release gate

The first POC release is acceptable when at least one initial target board satisfies:

- [ ] boots from MrChromebox UEFI
- [ ] reaches shell login
- [ ] internal eMMC visible
- [ ] keyboard works
- [ ] Wi-Fi connects and reaches HTTPS
- [ ] base utilities present: `nano`, `screen`, `htop`, `ncdu`
- [ ] hardware matrix created
- [ ] image size and RAM metrics recorded
- [ ] audio hardware identified, even if internal speakers remain disabled for safety
- [ ] known gaps documented

## Desktop release gate

The first desktop milestone is acceptable when at least one initial target board satisfies:

- [ ] boots desktop image from MrChromebox UEFI
- [ ] reaches LXQt + Labwc session
- [ ] QTerminal opens
- [ ] PCManFM-Qt opens
- [ ] Firefox launches and loads HTTPS
- [ ] VLC launches and plays local media
- [ ] one x86-64 AppImage launches successfully
- [ ] touchpad works in desktop
- [ ] graphics acceleration path is documented
- [ ] audio output is safe or explicitly disabled
- [x] `/data` design documented
- [ ] known gaps documented
