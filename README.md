# DevTube 📺

DevTube is a high-performance, modular media player for Android, designed to provide a seamless audio and video streaming experience for YouTube and YouTube Music content. Built with the latest Android technologies, it features an energetic Material 3 interface and a robust background playback engine.

## ✨ Key Features

- **Adaptive URL Playback**: Intelligent parsing and resolution of YouTube and YouTube Music links.
- **Immersive Video Surface**: Custom-built video renderer using AndroidX Media3 with a fully decoupled Compose UI.
- **Background Media Session**: Full headless playback support via `MediaSessionService`, integrated with system media controls.
- **Expressive Material 3 UI**: A vibrant, energetic design following Material Design 3 guidelines, with support for **Dynamic Color** (Android 12+).
- **Navigation 3**: Utilizes the latest state-driven **Jetpack Navigation 3** for type-safe screen management.
- **Adaptive Layouts**: Designed to be responsive across phones, tablets, and foldables.
- **Mock-First Strategy**: Implements a modular architecture that allows for rapid development using mock stream extractors before integrating real-world APIs.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/compose)
- **Media Engine**: [AndroidX Media3](https://developer.android.com/guide/topics/media/media3) (ExoPlayer & MediaSession)
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
- **Navigation**: [Jetpack Navigation 3](https://developer.android.com/guide/navigation/navigation-3)
- **Architecture**: Clean Architecture (MVI/MVVM pattern)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Asynchronous Logic**: Kotlin Coroutines & Flow

## 🏗 Architecture Overview

The project follows **Clean Architecture** principles to ensure maintainability and testability:

1.  **Domain Layer**: Pure Kotlin layer containing business models, repository interfaces, and the `PlaybackCoordinator`.
2.  **Data Layer**: Implementation of repositories, URL parsers, and the `MockStreamExtractor`.
3.  **Playback Layer**: Handles the Media3 `ExoPlayer` instance, the `MediaSessionService`, and provides a reactive `IMediaController`.
4.  **UI (Presentation) Layer**: Jetpack Compose screens, ViewModels, and the custom `PlaybackControlsOverlay`.

### Decoupling Strategy
The UI layer is decoupled from the specific playback implementation via the `PlayerHandleProvider` interface. This allows the `PlayerViewModel` to provide the media player to the video surface without having a direct dependency on the Media3 internal classes.

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- Android SDK 35+
- A device/emulator running Android 7.0 (API 24) or higher

### Build & Run
1.  Clone the repository:
    ```bash
    git clone https://github.com/dev-shady/DevTube.git
    ```
2.  Open the project in Android Studio.
3.  Sync Gradle files.
4.  Run the `:app` module on your device.

## 📝 Usage
- Paste any YouTube or YouTube Music URL into the input field.
- Tap **Play** to start the stream.
- Control playback using the custom overlay or the system media notification.

---
Developed with ❤️ by [DevShady](https://github.com/dev-shady)
