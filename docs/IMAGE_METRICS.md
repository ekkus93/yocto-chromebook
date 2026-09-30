# Image Metrics

This document records currently qualified image-size evidence for `yocto-chromebook` images.

The values here are build-artifact metrics, not booted-system runtime metrics. Hardware boot, installed rootfs usage from a running board, RAM, and boot-time measurements remain pending until SNAPPY or VORTICON evidence bundles are collected.

## Current exact-head POC build evidence

GitHub Actions `Validate` run #246 completed successfully for exact `master` commit `200cad6db5aa2fb8e94fbc52652e4785607a25b4` on 2026-09-30. Both POC WIC image jobs completed their build, evidence-capture, and artifact-upload steps successfully.

| Target | Workflow run | Job | Uploaded artifact | Artifact upload size |
| --- | --- | --- | --- | ---: |
| SNAPPY POC | `36629893558` (#246) | `109616149515` | `snappy-poc-image-200cad6db5aa2fb8e94fbc52652e4785607a25b4` | `708,986,902 bytes` |
| VORTICON POC | `36629893558` (#246) | `109616149779` | `vorticon-poc-image-200cad6db5aa2fb8e94fbc52652e4785607a25b4` | `708,986,050 bytes` |

These artifact-upload sizes describe the GitHub Actions artifact archives. They are not substitutes for the exact `.wic.gz` payload sizes inside those archives. The exact-head run establishes that both current POC WIC images build and upload successfully from the same `master` commit.

## Qualified SNAPPY POC payload size

The exact compressed payload measurement below comes from the earlier SNAPPY image-build gate merged by PR #30.

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

The qualified compressed SNAPPY POC payload measurement is `699,995,369 bytes`.

## Open metrics

The following measurements still require additional build, mounted-image, or booted-board evidence and must remain open in `docs/YOCTO_CHROMEBOOK_POC_TODO.md`:

- installed POC rootfs size from a mounted or booted target image,
- exact current VORTICON `.wic.gz` payload size,
- compressed and installed desktop image sizes,
- boot-to-console time,
- boot-to-LXQt time,
- idle RAM at console,
- idle RAM in LXQt,
- Firefox memory after launch,
- VLC playback CPU/RAM.

RAM and boot-time metrics remain pending because no SNAPPY or VORTICON boot evidence has been recorded yet.
