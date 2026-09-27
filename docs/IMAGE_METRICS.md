# Image Metrics

This document records currently qualified image-size evidence for `yocto-chromebook` images.

The values here are build-artifact metrics, not booted-system runtime metrics. Hardware boot, installed rootfs usage from a running board, RAM, and boot-time measurements remain pending until SNAPPY or VORTICON evidence bundles are collected.

## Source evidence

The current qualified SNAPPY POC image evidence comes from the image-build gate merged by PR #30.

- Merged `master` commit: `1e2e06c14623964218898ae7b8222d50638d9436`
- Qualified PR head: `fb2506f963816fbf935a0fabcba23c87e5bc9788`
- GitHub Actions PR run: `36265813568` (`Validate` run #179)
- Image job: `108470140445` (`SNAPPY POC image build`)
- Uploaded artifact: `snappy-poc-image-af34af0782a44566725a0dcc9522728e4f197956`
- Artifact ID: `10918571968`
- Artifact upload size: `700,060,998 bytes`
- Image output timestamp in filenames: `20260926192542`

## SNAPPY POC image

| Output | Bytes | Approximate size | SHA-256 |
| --- | ---: | ---: | --- |
| `yocto-chromebook-poc-snappy.rootfs-20260926192542.wic` | `2,656,201,728` | `2.47 GiB` | `fc884684d217bd7a8f1b21434f9f1f4beb68e563dc7af9e813c2602a0c495726` |
| `yocto-chromebook-poc-snappy.rootfs-20260926192542.wic.gz` | `699,995,369` | `667.57 MiB` | `6e80bac9927a812726b0a63d011cf8951ef02d4c7b6afe4a5d3045671f6af4ba` |
| `yocto-chromebook-poc-snappy.rootfs-20260926192542.manifest` | `62,502` | `61.04 KiB` | `5e80e03df5ff5d4ee10e15150805670d6b291f34f42d0fd2a850a48bdf647b0f` |

The compressed POC image size is currently `699,995,369 bytes` for the qualified SNAPPY artifact.

## Open metrics

The following measurements still require additional build or booted-board evidence and must remain open in `docs/YOCTO_CHROMEBOOK_POC_TODO.md`:

- installed POC rootfs size from a mounted or booted target image,
- compressed and installed desktop image sizes,
- boot-to-console time,
- boot-to-LXQt time,
- idle RAM at console,
- idle RAM in LXQt,
- Firefox memory after launch,
- VLC playback CPU/RAM.

RAM and boot-time metrics remain pending because no SNAPPY or VORTICON boot evidence has been recorded yet.
