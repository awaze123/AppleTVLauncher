# Apple TV Style Android TV Launcher

A starter Android TV launcher UI for H96 Max RK3318 / Android 9. Includes a hero panel, trending cards, and a focusable app dock for YouTube, Fastway Live TV, NetMirror, and Hotstar.

## Build APK on GitHub

1. Create a **Public** repository on GitHub, e.g. `AppleTVLauncher`.
2. Upload the contents of this folder to the repository root (not the containing folder itself).
3. Open **Actions**. If prompted, allow GitHub Actions.
4. The workflow `Build Android APK` runs on pushes and manual dispatch.
5. Open the completed workflow run, then download the `AppleTVLauncher-debug-apk` artifact.

## Build locally

Requires Android Studio with JDK 17 and Android SDK Platform 35 installed. Open this folder as a project and run **Build > Build APK(s)**.

## Install to a TV box

Connect ADB to the TV box, then install the generated APK:

```cmd
adb connect TV_BOX_IP:5555
adb install -r app-debug.apk
```

Run it as a regular app first. Do not disable the stock launcher until the new launcher has been tested.

## Notes

- Package name: `com.awaze.appletvlauncher`
- Android 9 (API 28) compatible minimum target.
- App package names may vary by device/region. If an app does not open, verify its installed package name with `adb shell pm list packages`.
- This starter uses gradient placeholder cards rather than movie artwork and does not automatically set itself as the default HOME app.
