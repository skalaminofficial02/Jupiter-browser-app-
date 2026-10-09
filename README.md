# Jupiter Browser

A lightweight, privacy-minded Android web browser built with Kotlin and the system WebView. Jupiter is designed to stay small and fast, and it integrates with its own search engine, **Jupiter Search**.



![Platform](https://img.shields.io/badge/platform-Android%208%2B-green)




![Language](https://img.shields.io/badge/language-Kotlin-purple)




![Build](https://img.shields.io/badge/build-GitHub%20Actions-blue)



## Features

- Address bar with smart input (URL or search query)
- Multiple tabs with a tab switcher
- Bookmarks and browsing history
- Built-in ad and tracker host blocking
- File downloads via the system DownloadManager
- Back, forward, reload, and home controls with a loading progress bar
- Can be set as the default browser (handles `http` and `https` links)
- Adaptive vector app icon

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Android Views, ViewBinding |
| Web engine | Android System WebView |
| Storage | SharedPreferences (JSON) |
| Build | Gradle (Kotlin DSL) |
| CI/CD | GitHub Actions |
| Min / Target SDK | 26 / 34 |

## Architecture

```
Jupiter/
├── .github/workflows/build.yml     # CI: builds a debug APK on every push
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/jupiter/app/
│       │   ├── ui/
│       │   │   └── MainActivity.kt        # Screen, buttons, dialogs
│       │   ├── browser/
│       │   │   ├── Tab.kt                 # Single tab model
│       │   │   ├── TabManager.kt          # Tab lifecycle and switching
│       │   │   ├── BrowserWebViewClient.kt
│       │   │   ├── BrowserChromeClient.kt
│       │   │   ├── AdBlocker.kt           # Host-based blocking
│       │   │   └── DownloadHandler.kt
│       │   ├── data/
│       │   │   └── EntryStore.kt          # Bookmarks and history storage
│       │   └── util/
│       │       └── UrlUtils.kt            # URL / search resolution
│       └── res/
│           ├── layout/activity_main.xml
│           ├── drawable/                  # Adaptive icon layers
│           ├── mipmap-anydpi-v26/         # Launcher icons
│           └── values/                    # Strings and theme
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

### Design notes

- **ui/** holds screens only. **browser/** holds web logic. **data/** holds persistence. **util/** holds helpers.
- `TabManager` owns all `WebView` instances and swaps them inside one container.
- `UrlUtils.resolve()` decides whether input is a URL or a search query and routes queries to Jupiter Search.

## Getting Started

### Build with GitHub Actions (no local setup)

1. Push to the `main` branch.
2. Open the **Actions** tab and wait for the build to finish.
3. Download the **Jupiter-debug** artifact, extract it, and install the `.apk`.

### Build locally

Requirements: JDK 17 and Android SDK 34.

```bash
gradle assembleDebug
```

The APK is generated at `app/build/outputs/apk/debug/`.

## Configuration

Search and home URLs are defined in `util/UrlUtils.kt`:

```kotlin
const val HOME = "https://jupiter-search-7cs.pages.dev"
private const val SEARCH = "https://jupiter-search-7cs.pages.dev/search?q="
```

## Roadmap

- [ ] Private (incognito) tabs
- [ ] Persistent tabs across restarts
- [ ] Dark mode
- [ ] Improved downloads manager
- [ ] Larger, updatable ad-block lists
- [ ] Release signing and Play Store build

## Related Projects

- [Jupiter-Search-web](https://github.com/YOUR_USERNAME/Jupiter-Search-web): search frontend
- [xjupiter-search](https://github.com/YOUR_USERNAME/xjupiter-search): search backend (SearXNG)

## License

Choose a license (for example MIT) and add a `LICENSE` file.
