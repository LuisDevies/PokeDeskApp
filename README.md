# Pokédex - Modern Android Showcase

A professional-grade Android application demonstrating modern development practices, scalable architecture, and a robust offline-first strategy. Built using Jetpack Compose, Paging 3, Hilt, and Room, this project serves as a showcase of enterprise-standard Android development.

## 🚀 Features

- **Offline-First Strategy**: Uses **Paging 3 with RemoteMediator** to synchronize remote API data with a local Room database. The app is fully functional without an active network connection.
- **Infinite Scrolling**: Efficiently handles large datasets by loading data in chunks as the user scrolls.
- **Debounced Search**: Implemented reactive search logic with a 300ms debounce to optimize database performance and prevent UI flickering.
- **Modern UI**: 100% Jetpack Compose using Material 3 design principles, featuring a clean, responsive interface.
- **Type-Safe Navigation**: Uses the latest Jetpack Navigation (Compose) with Kotlin Serialization for type-safe route handling.

## 🏗️ Architecture

The project follows **Clean Architecture** principles and the **MVVM (Model-View-ViewModel)** pattern:

- **UI Layer**: Jetpack Compose screens that observe UI state from ViewModels using `collectAsStateWithLifecycle`.
- **Domain Layer**: Repository interfaces and entities that define the business logic.
- **Data Layer**: Implementation of repositories, Room DAOs, Retrofit API services, and the Paging 3 RemoteMediator.
- **Dependency Injection**: Powered by **Hilt** for a modular and testable codebase.

## 🛠️ Tech Stack

- **UI**: Jetpack Compose (Material 3)
- **Asynchronous Flow**: Kotlin Coroutines & Flows
- **Dependency Injection**: Hilt
- **Local Database**: Room (with Paging 3 support)
- **Networking**: Retrofit & OkHttp
- **Image Loading**: Coil
- **Pagination**: Paging 3 (RemoteMediator)
- **Build System**: Gradle Kotlin DSL & Version Catalogs

## 🧪 Testing

The project maintains a high standard of quality with a comprehensive test suite:

- **Unit Tests**:
    - `PokemonListViewModelTest`: Verified search debouncing and UI state transitions using **Turbine**.
    - `PokemonRemoteMediatorTest`: Validated network-to-database synchronization logic using **Robolectric**.
- **UI Tests**:
    - `PokemonListScreenTest`: Automated verification of Compose UI interactions, search filtering, and navigation triggers using a **Fake Repository** pattern.
- **Tools**: MockK, Turbine, JUnit 4, Espresso.

## 📋 Getting Started

1. Clone the repository.
2. Open in **Android Studio Ladybug (2024.2.1)** or newer.
3. Sync Gradle and run the `:app` module.
4. To run tests, execute `./gradlew test` for unit tests or `./gradlew connectedAndroidTest` for UI tests.

---

*Developed with ❤️ as a demonstration of modern Android excellence.*
