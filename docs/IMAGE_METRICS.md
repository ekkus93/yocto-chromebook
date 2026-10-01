# Image Metrics

This document records currently qualified image-size evidence for `yocto-chromebook` images.

The values here are build-artifact metrics, not booted-system runtime metrics. The validation workflow also captures the built rootfs staging tree with `du --apparent-size --block-size=1`; that is a reproducible installed-content footprint before filesystem allocation overhead, not a claim about free/used blocks on a booted board. Hardware boot, runtime filesystem usage, RAM, and boot-time measurements remain pending until SNAPPY or VORTICON evidence bundles are collected.

## Current exact-head POC build evidence

GitHub Actions `Validate` run #250 completed successfully for exact `master` commit `ab5c622d8bc901f05c80657923ddf50c60931dee` on 2026-10-01. Both POC WIC image jobs completed their build, rootfs/image evidence-capture, and artifact-upload steps successfully.

| Target | Workflow run | Job | Uploaded artifact | Artifact upload size |
| --- | --- | --- | --- | ---: |
| SNAPPY POC | `36783554240` (#250) | `110119441607` | `snappy-poc-image-ab5c622d8bc901f05c80657923ddf50c60931dee` | `708,984,949 bytes` |
| VORTICON POC | `36783554240` (#250) | `110119441495` | `vorticon-poc-image-ab5c622d8bc901f05c80657923ddf50c60931dee` | `708,986,306 bytes` |

The artifact-upload sizes describe the GitHub Actions artifact archives. The exact payload and installed-content measurements captured inside run #250 are:

| Target | Output | Bytes | Approximate size | SHA-256 |
| --- | --- | ---: | ---: | --- |
| SNAPPY POC | installed rootfs staging tree | `1,523,100,176` | `1.42 GiB` | n/a |
| SNAPPY POC | `yocto-chromebook-poc-snappy.rootfs-20260930221014.wic` | `2,711,919,616` | `2.53 GiB` | `4c32883b6818e89a5519928829d7bf12adb00d8b8fa7260c33ed540a487d3c0d` |
| SNAPPY POC | `yocto-chromebook-poc-snappy.rootfs-20260930221014.wic.gz` | `708,917,405` | `676.08 MiB` | `0032ce52a224c0008f6fd2a567bbdbe764aaf5fdc8f543051b81ebd7d84c149f` |
| SNAPPY POC | `yocto-chromebook-poc-snappy.rootfs-20260930221014.manifest` | `64,029` | `62.53 KiB` | `33e3f6052ab0f5744cd8b6d403ddafca57c64b9224bba745c9c540a7d6b7f269` |
| VORTICON POC | installed rootfs staging tree | `1,523,100,180` | `1.42 GiB` | n/a |
| VORTICON POC | `yocto-chromebook-poc-vorticon.rootfs-20260930220907.wic` | `2,711,919,616` | `2.53 GiB` | `bbd80d3f3725664a9190ad75389740f9274e5e533769ffb42ce806966c7c6633` |
| VORTICON POC | `yocto-chromebook-poc-vorticon.rootfs-20260930220907.wic.gz` | `708,917,800` | `676.08 MiB` | `f0c24e849cea6205b34a918822e659b6e9577366ae6227419fe8d1f3bd8837f2` |
| VORTICON POC | `yocto-chromebook-poc-vorticon.rootfs-20260930220907.manifest` | `64,929` | `63.41 KiB` | `166551c2a4cccf1143b2568fad428ccf892e61408937183a634c95ee9dceff86` |

The installed-content measurement is the apparent byte count of BitBake's completed rootfs staging tree. It is reproducible build evidence for M19's installed POC rootfs-size task; booted-filesystem allocation and free-space measurements remain runtime evidence.

## Earlier qualified POC payload evidence

`Validate` run #246 passed at commit `200cad6db5aa2fb8e94fbc52652e4785607a25b4` on 2026-09-30. Before rootfs staging-tree capture was added, it recorded compressed POC payload sizes of `708,919,746 bytes` for SNAPPY and `708,917,938 bytes` for VORTICON. The near-identical run #250 values show the current POC payload remains approximately 676 MiB compressed.

The earlier SNAPPY image-build gate merged by PR #30 is also a useful historical comparison point: its compressed WIC was `699,995,369 bytes` (`667.57 MiB`) at qualified PR head `fb2506f963816fbf935a0fabcba23c87e5bc9788`.

## Open metrics

The following measurements still require additional build or booted-board evidence and must remain open in `docs/YOCTO_CHROMEBOOK_POC_TODO.md`:

- compressed and installed desktop image sizes,
- boot-to-console time,
- boot-to-LXQt time,
- idle RAM at console,
- idle RAM in LXQt,
- Firefox memory after launch,
- VLC playback CPU/RAM,
- hard size/RAM targets derived from the first complete build/runtime evidence set.

RAM and boot-time metrics remain pending because no SNAPPY or VORTICON boot evidence has been recorded yet.
