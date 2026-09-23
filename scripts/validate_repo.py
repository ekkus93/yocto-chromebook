#!/usr/bin/env python3
"""Validate the bootstrap repository shape for yocto-chromebook."""

from __future__ import annotations

from pathlib import Path
import sys


ROOT = Path(__file__).resolve().parents[1]

REQUIRED_FILES = [
    ".gitignore",
    "README.md",
    "docs/HARDWARE_NOTES.md",
    "docs/POC_PACKAGE_BASELINE.md",
    "docs/STORAGE_AND_UPDATE_DESIGN.md",
    "docs/UEFI_DEPLOYMENT.md",
    "docs/YOCTO_CHROMEBOOK_SPEC.md",
    "docs/YOCTO_CHROMEBOOK_POC_TODO.md",
    "kas/snappy-poc.yml",
    "kas/vorticon-poc.yml",
    "kas/snappy-desktop.yml",
    "kas/vorticon-desktop.yml",
    "meta-yocto-chromebook/conf/layer.conf",
    "meta-yocto-chromebook/conf/distro/yocto-chromebook.conf",
    "meta-yocto-chromebook/conf/machine/snappy.conf",
    "meta-yocto-chromebook/conf/machine/vorticon.conf",
    "meta-yocto-chromebook/conf/machine/include/intel-apollolake-chromebook.inc",
    "meta-yocto-chromebook/conf/machine/include/intel-geminilake-chromebook.inc",
    "meta-yocto-chromebook/recipes-core/images/yocto-chromebook-poc.bb",
    "meta-yocto-chromebook/recipes-core/images/yocto-chromebook-desktop.bb",
    "meta-yocto-chromebook/recipes-core/packagegroups/packagegroup-yocto-chromebook-poc.bb",
    "meta-yocto-chromebook/recipes-support/ncdu/ncdu_1.19.bb",
    "scripts/validate_repo.py",
]

REQUIRED_DIRS = [
    "kas",
    "meta-yocto-chromebook",
    "meta-yocto-chromebook/conf",
    "meta-yocto-chromebook/conf/distro",
    "meta-yocto-chromebook/conf/machine",
    "meta-yocto-chromebook/conf/machine/include",
    "meta-yocto-chromebook/recipes-core",
    "meta-yocto-chromebook/recipes-core/images",
    "meta-yocto-chromebook/recipes-core/packagegroups",
    "meta-yocto-chromebook/recipes-desktop",
    "meta-yocto-chromebook/recipes-bsp",
    "meta-yocto-chromebook/recipes-kernel",
    "meta-yocto-chromebook/recipes-multimedia",
    "meta-yocto-chromebook/recipes-support",
    "meta-yocto-chromebook/recipes-support/ncdu",
    "scripts",
]

SPEC_REQUIRED_PHRASES = [
    "LXQt + Labwc",
    "AppImage",
    "MrChromebox UEFI",
    "`snappy`",
    "`vorticon`",
]

TODO_REQUIRED_PHRASES = [
    "## M0 — Repository bootstrap",
    "## M1 — Yocto layer skeleton",
    "## M2 — kas bootstrap",
    "## M5 — POC image package baseline",
    "## POC-1 release gate",
    "## Desktop release gate",
]

LAYER_REQUIRED_PHRASES = [
    "BBFILE_COLLECTIONS",
    "LAYERSERIES_COMPAT_yoctochromebook",
    "scarthgap",
]

DISTRO_REQUIRED_PHRASES = [
    "TCLIBC = \"glibc\"",
    "INIT_MANAGER = \"systemd\"",
    "wayland",
    "AppImage",
]

POC_PACKAGEGROUP_REQUIRED_PHRASES = [
    "bash",
    "coreutils",
    "util-linux",
    "curl",
    "wget",
    "ca-certificates",
    "tar",
    "gzip",
    "xz",
    "unzip",
    "iproute2",
    "ethtool",
    "pciutils",
    "usbutils",
    "procps",
    "less",
    "nano",
    "screen",
    "htop",
    "ncdu",
    "openssh-ssh",
    "packagegroup-core-boot",
    "networkmanager",
    "kernel-modules",
    "linux-firmware",
]

NCDU_RECIPE_REQUIRED_PHRASES = [
    "SRC_URI",
    "f4452faa69887dfe4203691a8334b069defdd522a9ce6ddda6458aba89fa4765",
    "inherit autotools pkgconfig",
]

UEFI_DEPLOYMENT_REQUIRED_PHRASES = [
    "MrChromebox UEFI Full ROM",
    "External USB boot workflow",
    "Internal eMMC deployment workflow",
    "GPT disk image",
    "EFI System Partition",
    "dd if=yocto-chromebook-poc-snappy.wic",
    "Evidence to capture",
]

STORAGE_UPDATE_REQUIRED_PHRASES = [
    "Initial POC partition layout",
    "Future persistent layout",
    "/data/apps",
    "/data/home",
    "Manual installer decision",
    "Future A/B update design",
    "rootfs-A",
    "rootfs-B",
    "rollback",
]


def fail(message: str) -> None:
    print(f"ERROR: {message}", file=sys.stderr)
    raise SystemExit(1)


def assert_path_exists(relative_path: str, *, want_dir: bool = False) -> None:
    path = ROOT / relative_path
    if not path.exists():
        fail(f"missing required path: {relative_path}")
    if want_dir and not path.is_dir():
        fail(f"required path is not a directory: {relative_path}")
    if not want_dir and not path.is_file():
        fail(f"required path is not a file: {relative_path}")


def assert_contains(relative_path: str, phrases: list[str]) -> None:
    text = (ROOT / relative_path).read_text(encoding="utf-8")
    for phrase in phrases:
        if phrase not in text:
            fail(f"{relative_path} does not contain required phrase: {phrase!r}")


def assert_kas_file(relative_path: str, machine: str, target: str) -> None:
    text = (ROOT / relative_path).read_text(encoding="utf-8")
    for phrase in [
        "version: 14",
        f"machine: {machine}",
        "distro: yocto-chromebook",
        f"- {target}",
        "branch: \"scarthgap\"",
        "meta-yocto-chromebook:",
    ]:
        if phrase not in text:
            fail(f"{relative_path} does not contain required phrase: {phrase!r}")


def main() -> int:
    for required_file in REQUIRED_FILES:
        assert_path_exists(required_file)

    for required_dir in REQUIRED_DIRS:
        assert_path_exists(required_dir, want_dir=True)

    assert_contains("docs/YOCTO_CHROMEBOOK_SPEC.md", SPEC_REQUIRED_PHRASES)
    assert_contains("docs/YOCTO_CHROMEBOOK_POC_TODO.md", TODO_REQUIRED_PHRASES)
    assert_contains("meta-yocto-chromebook/conf/layer.conf", LAYER_REQUIRED_PHRASES)
    assert_contains("meta-yocto-chromebook/conf/distro/yocto-chromebook.conf", DISTRO_REQUIRED_PHRASES)
    assert_contains(
        "meta-yocto-chromebook/recipes-core/packagegroups/packagegroup-yocto-chromebook-poc.bb",
        POC_PACKAGEGROUP_REQUIRED_PHRASES,
    )
    assert_contains("meta-yocto-chromebook/recipes-core/images/yocto-chromebook-poc.bb", ["packagegroup-yocto-chromebook-poc"])
    assert_contains("meta-yocto-chromebook/recipes-support/ncdu/ncdu_1.19.bb", NCDU_RECIPE_REQUIRED_PHRASES)
    assert_contains("docs/POC_PACKAGE_BASELINE.md", POC_PACKAGEGROUP_REQUIRED_PHRASES)
    assert_contains("docs/UEFI_DEPLOYMENT.md", UEFI_DEPLOYMENT_REQUIRED_PHRASES)
    assert_contains("docs/STORAGE_AND_UPDATE_DESIGN.md", STORAGE_UPDATE_REQUIRED_PHRASES)

    assert_kas_file("kas/snappy-poc.yml", "snappy", "yocto-chromebook-poc")
    assert_kas_file("kas/vorticon-poc.yml", "vorticon", "yocto-chromebook-poc")
    assert_kas_file("kas/snappy-desktop.yml", "snappy", "yocto-chromebook-desktop")
    assert_kas_file("kas/vorticon-desktop.yml", "vorticon", "yocto-chromebook-desktop")

    print("yocto-chromebook repository validation passed")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
