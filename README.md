# DevTube 📺

DevTube is a high-performance, modular media player for Android, designed to provide a seamless audio and video streaming experience for YouTube and SoundCloud content. Built with the latest Android technologies, it features an energetic Material 3 interface and a robust background playback engine.

## ✨ Key Features

- **Multi-Platform Support**: Robust extraction and playback of **YouTube** and **SoundCloud** links via the native `NewPipeExtractor` engine.
- **Intelligent Stream Resolution**: Automatically selects the highest quality audio or video streams based on the content type.
- **Persistent Metadata & Artwork**: Seamlessly fetches and renders high-resolution artwork, titles, and artist info, even during transient network buffering.
- **Immersive Video Surface**: Custom-built video renderer using AndroidX Media3 with a fully decoupled Compose UI.
- **Background Media Session**: Full headless playback support via `MediaSessionService`, integrated with system media controls and notifications.
- **Expressive Material 3 UI**: A vibrant design following Material Design 3 guidelines, with support for **Dynamic Color** (Android 12+).
- **Navigation 3**: Utilizes the latest state-driven **Jetpack Navigation 3** for type-safe screen management.
- **Optimized Network Stack**: Custom `OkHttp` bridge for extraction, featuring modern headers and reliable `POST` request handling.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/compose)
- **Media Engine**: [AndroidX Media3](https://developer.android.com/guide/topics/media/media3) (ExoPlayer & MediaSession)
- **Extraction Engine**: [NewPipeExtractor](https://github.com/TeamNewPipe/NewPipeExtractor)
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
- **Navigation**: [Jetpack Navigation 3](https://developer.android.com/guide/navigation/navigation-3)
- **Architecture**: Clean Architecture (Domain, Data, Playback, UI)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Compatibility**: Java 11+ with Core Library Desugaring (NIO support)

## 🏗 Architecture Overview

The project follows **Clean Architecture** principles to ensure modularity and scalability:

1.  **Domain Layer**: Pure Kotlin layer containing the `DomainMediaItem`, `PlaybackSessionState`, and the `PlaybackCoordinator` which orchestrates the flow.
2.  **Data Layer**: Contains the `MediaRepository` implementation, URL parsers (YouTube/SoundCloud), and the `NewPipe`-powered extractors.
3.  **Playback Layer**: Encapsulates the Media3 logic. Implements `IMediaController` for reactive state updates and manages the `MediaSessionService` lifecycle.
4.  **UI (Presentation) Layer**: Modern Compose UI. Features a **Lazy Engine Connection** strategy to optimize app startup by initializing Media3 IPC only when required.

### Decoupling Strategy
The UI layer is completely decoupled from the media engine via the `PlayerHandleProvider` provided through `CompositionLocal`. This allows the UI to stay stateless while the media engine runs independently in the background.

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- Android SDK 37
- A device/emulator running Android 7.0 (API 24) or higher

### Build & Run
1.  Clone the repository:
    ```bash
    git clone https://github.com/dev-shady/DevTube.git
    ```
2.  Open the project in Android Studio.
3.  Sync Gradle files (Ensure you have internet for JitPack dependencies).
4.  Run the `:app` module on your device.

## 📝 Usage
- Paste any **YouTube** or **SoundCloud** URL into the input field.
- Tap **Play** to start streaming.
- Use the custom overlay to seek, skip, or toggle playback.

---
Developed with ❤️ by [DevShady](https://github.com/dev-shady)
