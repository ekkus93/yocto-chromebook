# Hardware Qualification Matrix

This matrix is the persistent support record for the initial Yocto Chromebook boards.

A board is not considered supported merely because a machine config exists or an image parses. Support status requires recorded evidence from target hardware.

## Status vocabulary

| Status | Meaning |
| --- | --- |
| `unknown` | No direct evidence has been recorded yet. |
| `detected` | The component appears in logs or enumeration output, but functional behavior is not validated. |
| `works` | The component has passed the stated validation for the board. |
| `partial` | The component works with known limitations or incomplete coverage. |
| `blocked` | Validation cannot proceed due to a specific unresolved issue. |
| `unsafe-disabled` | The component is intentionally disabled or treated as unusable until safety review is complete. |

## Evidence rules

For any transition away from `unknown`, record the evidence source in the Notes column or in `docs/HARDWARE_NOTES.md`.

Useful evidence includes:

- image artifact name and build commit;
- boot logs;
- `dmesg` excerpts;
- `lsblk`, `blkid`, `lspci -nn`, and `lsusb` output;
- NetworkManager or Wi-Fi association logs;
- Bluetooth controller enumeration;
- graphics compositor logs;
- ALSA/PipeWire/WirePlumber enumeration;
- suspend/resume logs; and
- photos or screenshots of firmware and boot behavior when text logs are unavailable.

## SNAPPY

| Component | Status | Evidence / notes |
| --- | --- | --- |
| UEFI boot | unknown | Target is MrChromebox UEFI Full ROM; no board boot log recorded yet. |
| eMMC | unknown | Expected internal eMMC; Linux device name not confirmed. |
| keyboard | unknown | Console input not validated on hardware. |
| touchpad | unknown | Graphical session not available or validated yet. |
| Wi-Fi | unknown | Chipset and firmware package still require hardware evidence. |
| Bluetooth | unknown | Controller identity and firmware package still require hardware evidence. |
| graphics | unknown | Intel graphics path selected in design only; no panel/KMS logs recorded. |
| USB-A | unknown | No USB validation evidence recorded. |
| USB-C | unknown | No USB-C validation evidence recorded. |
| battery | unknown | Battery status not validated. |
| brightness | unknown | Brightness control not validated. |
| suspend/resume | unknown | Suspend/resume cycles not validated. |
| speakers | unsafe-disabled | Internal speaker path is not qualified; audio safety gate remains closed. |
| headphones | unknown | Safe output path not validated. |
| microphone | unknown | Capture path not validated. |
| webcam | unknown | Camera enumeration/function not validated. |
| Firefox | unknown | Desktop image/application stack not built or validated. |
| VLC | unknown | Desktop image/application stack not built or validated. |
| AppImage | unknown | AppImage runtime test not validated. |

## VORTICON

| Component | Status | Evidence / notes |
| --- | --- | --- |
| UEFI boot | unknown | Target is MrChromebox UEFI Full ROM; no board boot log recorded yet. |
| eMMC | unknown | Expected internal eMMC; Linux device name not confirmed. |
| keyboard | unknown | Console input not validated on hardware. |
| touchpad | unknown | Graphical session not available or validated yet. |
| Wi-Fi | unknown | Chipset and firmware package still require hardware evidence. |
| Bluetooth | unknown | Controller identity and firmware package still require hardware evidence. |
| graphics | unknown | Intel graphics path selected in design only; no panel/KMS logs recorded. |
| USB-A | unknown | No USB validation evidence recorded. |
| USB-C | unknown | No USB-C validation evidence recorded. |
| battery | unknown | Battery status not validated. |
| brightness | unknown | Brightness control not validated. |
| suspend/resume | unknown | Suspend/resume cycles not validated. |
| speakers | unsafe-disabled | Internal speaker path is not qualified; audio safety gate remains closed. |
| headphones | unknown | Safe output path not validated. |
| microphone | unknown | Capture path not validated. |
| webcam | unknown | Camera enumeration/function not validated. |
| Firefox | unknown | Desktop image/application stack not built or validated. |
| VLC | unknown | Desktop image/application stack not built or validated. |
| AppImage | unknown | AppImage runtime test not validated. |

## Update process

When new hardware evidence is collected:

1. update the relevant board row status;
2. add a concise evidence note;
3. link or summarize the log source in `docs/HARDWARE_NOTES.md` when the evidence is too large for this matrix;
4. avoid marking hardware as `works` unless the matching TODO acceptance criterion is satisfied; and
5. keep `unsafe-disabled` for speakers until the audio safety gate is explicitly resolved.
