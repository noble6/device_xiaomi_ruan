# Device tree for Redmi Pad Pro 5G / POCO Pad 5G (ruan)

| Device       | Redmi Pad Pro 5G / POCO Pad 5G                  |
|-------------:|:------------------------------------------------|
| Codename     | ruan                                            |
| SoC          | Qualcomm Snapdragon 7s Gen 2 (SM7435, parrot)   |
| CPU          | 4x Cortex-A78 @ 2.4 GHz + 4x Cortex-A55 @ 1.95 GHz |
| GPU          | Adreno 710                                      |
| Display      | 12.1" 2560x1600 LCD, 120 Hz                     |
| Battery      | 10000 mAh, 33 W                                 |
| Kernel       | Linux 5.10 GKI (android12-5.10)                 |
| Shipped with | Android 14, HyperOS                             |

Also boots on the Redmi Pad Pro / POCO Pad (dizi).

## Branches

- `axion`: AxionOS (LineageOS 23.2 based)
- `lunaris`: Lunaris AOSP 16.2

## Building

Put `manifest/ruan.xml` in `.repo/local_manifests/` and sync. Then apply
the patches in `patches/` to their projects:

```
for d in build/make frameworks/base frameworks/native vendor/lineage; do
  (cd $d && git am -3 $ANDROID_BUILD_TOP/device/xiaomi/ruan/patches/$d/*.patch)
done
```

AxionOS:

```
. build/envsetup.sh
axion ruan user gms core
ax -br -j$(nproc)
```

Lunaris (use the `lunaris` branch's manifest):

```
. build/envsetup.sh
lunch lineage_ruan-bp4a-user
m bacon
```

## Notes

- The kernel is built from source (`kernel/xiaomi/sm7435`, Xiaomi's
  ruan-u-oss updated to the latest 5.10 LTS). Its ABI matches the stock
  OS3.0 vendor modules, which are shipped prebuilt. Set
  `RUAN_PREBUILT_KERNEL=true` to use the prebuilt Image instead.
- Vendor blobs come from HyperOS OS3.0.303.0.WFSMIXM (Global).
- Redmi Smart Pen and POCO Smart Pen work with pressure. See `pen/`.

## Credits

- [g8row](https://github.com/g8row) for the dizi device tree research
  this tree started from
- The garnet (SM7435) community for the shared platform sources
