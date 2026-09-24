# Audio Safety Policy

This document defines the conservative initial audio policy for SNAPPY and VORTICON bring-up.

The policy is intentionally safety-first: software support for audio identity and diagnostics may be present before audio output is qualified, but internal speakers must remain unqualified and unsafe-disabled until board-specific evidence proves the codec, amplifier path, topology, UCM2 profile, and mixer defaults are safe.

## Initial mixer policy

The initial mixer policy is mute-first:

- do not auto-enable internal speakers during boot
- do not treat codec detection as speaker qualification
- keep `Master`, `Speaker`, `PCM`, and similar playback controls muted or at zero until the board-specific audio path is reviewed
- prefer headphones, HDMI, USB-C, or USB audio for first playback validation when those paths are detected and safe
- document every mixer change used during validation

The repository ships `yocto-chromebook-audio-safety-policy`, a small noarch policy package that installs:

- `/etc/yocto-chromebook/audio-safety-policy.conf`
- `/usr/bin/yocto-chromebook-audio-safe-startup`

The helper is intentionally not enabled as a boot service yet. It can be invoked manually during early hardware bring-up to apply a mute-first baseline when `amixer` is present, but automatic boot-time audio mutation remains deferred until real hardware evidence confirms that the chosen controls are correct for each board.

## Qualification boundary

This policy closes only the TODO item for adding a conservative initial mixer policy. It does not close any of these hardware-dependent tasks:

- identifying whether SNAPPY or VORTICON uses AVS or SOF
- identifying codec or amplifier devices
- identifying topology, firmware, or UCM2 requirements
- validating PipeWire or WirePlumber enumeration
- validating headphones, microphones, HDMI, USB-C, USB audio, or speakers
- marking any board audio-qualified

## Speaker safety gate

Internal speakers remain `unsafe-disabled` until all of the following are true for a board:

1. The exact codec and amplifier path are identified from booted-system evidence.
2. Required firmware, topology, and UCM2 files are identified.
3. The expected mixer controls are known and documented.
4. Headphones or another safe external output path has been validated first where possible.
5. Initial internal-speaker testing is done at controlled low volume.
6. The hardware matrix records the evidence and remaining risk.

Do not mark internal speakers as working merely because a sound card appears in `aplay -l`, `pactl`, PipeWire, or WirePlumber output.
