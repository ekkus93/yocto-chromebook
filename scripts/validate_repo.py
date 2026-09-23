#!/usr/bin/env python3
"""Validate the bootstrap repository shape for yocto-chromebook."""

from __future__ import annotations

from pathlib import Path
import sys


ROOT = Path(__file__).resolve().parents[1]

REQUIRED_FILES = [
    ".gitignore",
    "README.md",
    "docs/YOCTO_CHROMEBOOK_SPEC.md",
    "docs/YOCTO_CHROMEBOOK_POC_TODO.md",
]

REQUIRED_DIRS = [
    "kas",
    "meta-yocto-chromebook",
    "meta-yocto-chromebook/conf",
    "meta-yocto-chromebook/conf/distro",
    "meta-yocto-chromebook/conf/machine",
    "meta-yocto-chromebook/conf/machine/include",
    "meta-yocto-chromebook/recipes-core",
    "meta-yocto-chromebook/recipes-desktop",
    "meta-yocto-chromebook/recipes-bsp",
    "meta-yocto-chromebook/recipes-kernel",
    "meta-yocto-chromebook/recipes-multimedia",
    "meta-yocto-chromebook/recipes-support",
    "scripts",
]

SPEC_REQUIRED_PHRASES = [
    "LXQt + Labwc",
    "AppImage",
    "MrChromebox UEFI",
    "SNAPPY",
    "VORTICON",
]

TODO_REQUIRED_PHRASES = [
    "## M0 — Repository bootstrap",
    "## M1 — Yocto layer skeleton",
    "## POC-1 release gate",
    "## Desktop release gate",
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


def main() -> int:
    for required_file in REQUIRED_FILES:
        assert_path_exists(required_file)

    for required_dir in REQUIRED_DIRS:
        assert_path_exists(required_dir, want_dir=True)

    assert_contains("docs/YOCTO_CHROMEBOOK_SPEC.md", SPEC_REQUIRED_PHRASES)
    assert_contains("docs/YOCTO_CHROMEBOOK_POC_TODO.md", TODO_REQUIRED_PHRASES)

    print("yocto-chromebook bootstrap validation passed")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
