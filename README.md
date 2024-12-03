# TEST ASSIGNMENT

## Overview
This project is an Android application built with Kotlin, Jetpack Compose, Koin for Dependency Injection, and various other modern libraries. It follows best practices for Android development to create a scalable and maintainable application.

## Project Flow
- Kotlin
- Jetpack Compose
- Material3
- Coroutines and Flow
- Dependency Injection using Koin
- Firebase for crash reporting
- Media and Media3 libraries for media playback
- Coil for image loading

### Gradle

The project is configured using Gradle and includes several key plugins and settings:
- **Android Configuration:**
    - `namespace` - The namespace for the application (`ua.kuk.books`).
    - `compileSdk` - The compile SDK version (35).
    - `minSdk` - The minimum SDK version (23).
    - `targetSdk` - The target SDK version (35).

## Project Modules

- *`:app`*: The main application module containing the Android app's core components.

- *`:feature:book-playback`*: Handles the books summary playback functionality, including media controls and playback UI.

- *`:core:common`*: Includes common utilities and base classes used across the project.

- *`:core:data`*: Manages data sources, repositories, and data operations.

- *`:core:ui-kit`*: Provides reusable UI components and design elements used across the app.

### P.S.
For convenience and quicker review, the project was uploaded with critical files and fields (google-services.json, etc.) and already configured parameters, so no additional configuration is required.

