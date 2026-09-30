# Image Metrics

This document records currently qualified image-size evidence for `yocto-chromebook` images.

The values here are build-artifact metrics, not booted-system runtime metrics. The validation workflow also captures the built rootfs staging tree with `du --apparent-size --block-size=1`; that is a reproducible installed-content footprint before filesystem allocation overhead, not a claim about free/used blocks on a booted board. Hardware boot, runtime filesystem usage, RAM, and boot-time measurements remain pending until SNAPPY or VORTICON evidence bundles are collected.

## Current exact-head POC build evidence

GitHub Actions `Validate` run #246 completed successfully for exact `master` commit `200cad6db5aa2fb8e94fbc52652e4785607a25b4` on 2026-09-30. Both POC WIC image jobs completed their build, evidence-capture, and artifact-upload steps successfully. The evidence-capture steps recorded the exact WIC, compressed WIC, manifest sizes, and SHA-256 digests before upload.

| Target | Workflow run | Job | Uploaded artifact | Artifact upload size |
| --- | --- | --- | --- | ---: |
| SNAPPY POC | `36629893558` (#246) | `109616149515` | `snappy-poc-image-200cad6db5aa2fb8e94fbc52652e4785607a25b4` | `708,986,902 bytes` |
| VORTICON POC | `36629893558` (#246) | `109616149779` | `vorticon-poc-image-200cad6db5aa2fb8e94fbc52652e4785607a25b4` | `708,986,050 bytes` |

The artifact-upload sizes describe the GitHub Actions artifact archives. The exact payload measurements captured inside run #246 are:

| Target | Output | Bytes | Approximate size | SHA-256 |
| --- | --- | ---: | ---: | --- |
| SNAPPY POC | `yocto-chromebook-poc-snappy.rootfs-20260929210019.wic` | `2,711,919,616` | `2.53 GiB` | `b642017d412b7ead0bf80aad33d789ff1dcd61adbe38dd89a9b1a528122381e8` |
| SNAPPY POC | `yocto-chromebook-poc-snappy.rootfs-20260929210019.wic.gz` | `708,919,746` | `676.08 MiB` | `17d8843cdbe602c4ad061876f8a59df91f5af76902946d7b4e23e892b50539b0` |
| SNAPPY POC | `yocto-chromebook-poc-snappy.rootfs-20260929210019.manifest` | `64,029` | `62.53 KiB` | `33e3f6052ab0f5744cd8b6d403ddafca57c64b9224bba745c9c540a7d6b7f269` |
| VORTICON POC | `yocto-chromebook-poc-vorticon.rootfs-20260929210202.wic` | `2,711,919,616` | `2.53 GiB` | `d9b382f30513eb506097315bdcd80acb28726ac59421c0c526a86c7afd79628f` |
| VORTICON POC | `yocto-chromebook-poc-vorticon.rootfs-20260929210202.wic.gz` | `708,917,938` | `676.08 MiB` | `50401e6a3e8388de08ada83bd6027c166a3dfb91c64a903f1d951112726b10a7` |
| VORTICON POC | `yocto-chromebook-poc-vorticon.rootfs-20260929210202.manifest` | `64,929` | `63.41 KiB` | `166551c2a4cccf1143b2568fad428ccf892e61408937183a634c95ee9dceff86` |

The current qualified compressed POC payload sizes are therefore `708,919,746 bytes` for SNAPPY and `708,917,938 bytes` for VORTICON.

## Earlier qualified SNAPPY POC payload size

The earlier SNAPPY image-build gate merged by PR #30 provides a useful historical comparison point.

- Merged `master` commit: `1e2e06c14623964218898ae7b8222d50638d9436`
- Qualified PR head: `fb2506f963816fbf935a0fabcba23c87e5bc9788`
- GitHub Actions PR run: `36265813568` (`Validate` run #179)
- Image job: `108470140445` (`SNAPPY POC image build`)
- Uploaded artifact: `snappy-poc-image-af34af0782a44566725a0dcc9522728e4f197956`
- Artifact ID: `10918571968`
- Artifact upload size: `700,060,998 bytes`
- Image output timestamp in filenames: `20260926192542`

| Output | Bytes | Approximate size | SHA-256 |
| --- | ---: | ---: | --- |
| `yocto-chromebook-poc-snappy.rootfs-20260926192542.wic` | `2,656,201,728` | `2.47 GiB` | `fc884684d217bd7a8f1b21434f9f1f4beb68e563dc7af9e813c2602a0c495726` |
| `yocto-chromebook-poc-snappy.rootfs-20260926192542.wic.gz` | `699,995,369` | `667.57 MiB` | `6e80bac9927a812726b0a63d011cf8951ef02d4c7b6afe4a5d3045671f6af4ba` |
| `yocto-chromebook-poc-snappy.rootfs-20260926192542.manifest` | `62,502` | `61.04 KiB` | `5e80e03df5ff5d4ee10e15150805670d6b291f34f42d0fd2a850a48bdf647b0f` |

## Open metrics

The following measurements still require additional build, mounted-image, or booted-board evidence and must remain open in `docs/YOCTO_CHROMEBOOK_POC_TODO.md`:

- exact POC rootfs installed-content size from the new build-time rootfs capture (pending the first successful exact-head run containing that instrumentation),
- compressed and installed desktop image sizes,
- boot-to-console time,
- boot-to-LXQt time,
- idle RAM at console,
- idle RAM in LXQt,
- Firefox memory after launch,
- VLC playback CPU/RAM,
- hard size/RAM targets derived from the first complete build/runtime evidence set.

RAM and boot-time metrics remain pending because no SNAPPY or VORTICON boot evidence has been recorded yet.
