# HouseHelper Workshop Tasks & Solutions Guide

This document provides a comprehensive guide to all core tasks (**Tasks 1–18**) and bonus tasks (**Bonus Tasks 1–4**) in the HouseHelper Kotlin Multiplatform & Compose Multiplatform workshop.

---

## Repository Branch Structure

- **`starter`** (Default Workshop Branch): Contains the starter skeleton of the HouseHelper app with placeholder stubs and `// TODO Task N` comments. Workshop attendees begin their exercises here.
- **`solutions`**: Contains the complete reference implementation with all 18 core tasks and 4 bonus tasks fully implemented and verified.
- **`main`**: The original upstream development branch where features were iterated before being structured into sequential workshop steps (`starter` &rarr; `solutions`).

---

## Core Workshop Tasks

### Task 1: Run Starter Project
- **Objective**: Verify the development environment and successfully launch the HouseHelper application across target platforms.
- **Platforms**: Desktop (JVM), Android, iOS, and Web (Wasm/JS).
- **Key Files**: Platform entry points (`desktopApp`, `androidApp`, `iosApp`, `webApp`).

---

### Task 2: Improve Onboarding with Compose Hot Reload
- **Objective**: Configure and experience rapid UI development using Compose Hot Reload on Desktop.
- **Key Changes**: Update the onboarding carousel and screen elements in `OnboardingScreen.kt` while observing live changes without full application rebuilds.
- **Key Files**: `shared/src/commonMain/kotlin/.../househelper/OnboardingScreen.kt`

---

### Task 3: Expand and Collapse Room Sections
- **Objective**: Add reactive UI state allowing users to expand and collapse room sections on the dashboard.
- **Key Changes**: Introduce an `expanded` state variable in `RoomSection` and toggle visibility of room device grids upon clicking the room header.
- **Key Files**: `shared/src/commonMain/kotlin/.../househelper/dashboard/RoomsContent.kt`

---

### Task 4: Add Subtitle to Onboarding
- **Objective**: Enhance the onboarding flow by displaying localized explanatory subtitles beneath page titles.
- **Key Changes**: Update `OnboardingScreen.kt` to render a subtitle `Text` composable and define the corresponding string resources.
- **Key Files**:
  - `shared/src/commonMain/kotlin/.../househelper/OnboardingScreen.kt`
  - `shared/src/commonMain/composeResources/values/strings.xml`

---

### Task 5: Implement CameraDetailsViewModel
- **Objective**: Connect the camera detail screen to real device data and asynchronous control via a dedicated ViewModel.
- **Key Changes**: Replace hardcoded preview values with reactive state from `CameraDetailsViewModel`, enabling real-time toggling of camera power state and status flow.
- **Key Files**:
  - `shared/src/commonMain/kotlin/.../househelper/devices/CameraDetailsViewModel.kt`
  - `shared/src/commonMain/kotlin/.../househelper/devices/CameraDetailsScreen.kt`

---

### Task 6: Load Camera Previews Using Coil
- **Objective**: Fetch and render live camera snapshot images asynchronously over the network.
- **Key Changes**: Integrate Coil's `AsyncImage` composable in `CameraDetailsScreen.kt` to load dynamic image URLs with fallback placeholder handling.
- **Key Files**: `shared/src/commonMain/kotlin/.../househelper/devices/CameraDetailsScreen.kt`

---

### Task 7: Preserve Room Expansion State with `rememberSaveable`
- **Objective**: Prevent room sections from resetting to their default state during configuration changes, screen rotations, or back-stack navigation.
- **Key Changes**: Upgrade the section expansion state from `remember { mutableStateOf(...) }` to `rememberSaveable`.
- **Key Files**: `shared/src/commonMain/kotlin/.../househelper/dashboard/RoomsContent.kt`

---

### Task 8: Dependency Injection & Assisted ViewModels
- **Objective**: Configure dependency injection bindings for parameterized ViewModels requiring runtime identifiers (`deviceId`, `roomId`).
- **Key Changes**: Set up ViewModel injection and parameter factories for `LightDetailsViewModel`, `RenameDeviceViewModel`, `CameraDetailsViewModel`, and `RoomViewModel`.
- **Key Files**:
  - `shared/src/commonMain/kotlin/.../househelper/di/`
  - Respective ViewModel and Screen files

---

### Task 9: Add Navigation to Rename Screen
- **Objective**: Enable users to initiate device renaming from device cards via secondary actions (long press / right click or context button).
- **Key Changes**: Define the `RenameDevice` navigation route in `Routes.kt` and trigger navigation from `RoomsContent.kt`.
- **Key Files**:
  - `shared/src/commonMain/kotlin/.../househelper/navigation/Routes.kt`
  - `shared/src/commonMain/kotlin/.../househelper/dashboard/RoomsContent.kt`
  - `shared/src/commonMain/kotlin/.../househelper/App.kt`

---

### Task 10: Display Rename Screen as a Dialog
- **Objective**: Render the device renaming UI as a modal dialog overlay on top of the dashboard instead of a full-screen navigation destination.
- **Key Changes**: Configure navigation scene strategies (`DialogSceneStrategy`) in `App.kt` and adapt `RenameDeviceScreen.kt` to dialog constraints.
- **Key Files**:
  - `shared/src/commonMain/kotlin/.../househelper/App.kt`
  - `shared/src/commonMain/kotlin/.../househelper/devices/RenameDeviceScreen.kt`

---

### Task 11: Animate Rotation for Room Header Arrow
- **Objective**: Provide smooth visual feedback when toggling room section expansion.
- **Key Changes**: Use `animateFloatAsState` to smoothly interpolate the arrow icon rotation between 0° (collapsed) and 180° (expanded).
- **Key Files**: `shared/src/commonMain/kotlin/.../househelper/dashboard/RoomsContent.kt`

---

### Task 12: Animate Room Items Visibility
- **Objective**: Create smooth visual transitions when room device grids expand or collapse.
- **Key Changes**: Wrap device content with `AnimatedVisibility` utilizing vertical expansion/shrink and fade enter/exit transitions.
- **Key Files**: `shared/src/commonMain/kotlin/.../househelper/dashboard/RoomsContent.kt`

---

### Task 13: Animate Transition Between Dashboard Tabs
- **Objective**: Eliminate abrupt screen swaps when switching between Dashboard tabs (Rooms vs Settings).
- **Key Changes**: Apply `AnimatedContent` with crossfade/slide transition specs across tab destinations in `DashboardScreen.kt`.
- **Key Files**: `shared/src/commonMain/kotlin/.../househelper/dashboard/DashboardScreen.kt`

---

### Task 14: Custom Theme Palette & Dark Mode Support
- **Objective**: Implement a consistent, branded Material 3 design system with dynamic light and dark color schemes.
- **Key Changes**: Define `AppLightColorScheme`, `AppDarkColorScheme`, and `AppShapes` in `theme/AppColors.kt`, and adapt `App.kt` to system dark mode settings (`isSystemInDarkTheme()`).
- **Key Files**:
  - `shared/src/commonMain/kotlin/.../househelper/theme/AppColors.kt`
  - `shared/src/commonMain/kotlin/.../househelper/theme/AppShapes.kt`
  - `shared/src/commonMain/kotlin/.../househelper/App.kt`

---

### Task 15: Handle Safe Insets & Edge-to-Edge Layout
- **Objective**: Ensure content is properly padded away from system bars, display cutouts, and navigation gestures across all form factors.
- **Key Changes**: Replace static padding with `WindowInsets.safeDrawing` handling and content padding in `DashboardScreen.kt` and `RoomsContent.kt`.
- **Key Files**:
  - `shared/src/commonMain/kotlin/.../househelper/dashboard/DashboardScreen.kt`
  - `shared/src/commonMain/kotlin/.../househelper/dashboard/RoomsContent.kt`

---

### Task 16: Multiplatform Video Streaming on Camera Screen
- **Objective**: Integrate real-time video playback inside the camera detail view across all supported targets.
- **Key Changes**: Embed the multiplatform `VideoPlayer` component in `CameraDetailsScreen.kt` with play/pause controls.
- **Key Files**:
  - `shared/src/commonMain/kotlin/.../househelper/devices/CameraDetailsScreen.kt`
  - `shared/src/commonMain/kotlin/.../househelper/VideoPlayer.kt`

---

### Task 17: Support Accessibility & Semantics for Device Cards
- **Objective**: Make interactive device cards fully accessible to screen readers and assistive technologies.
- **Key Changes**: Configure Compose `Modifier.semantics(mergeDescendants = true) { ... }` with explicit click labels and accessibility roles.
- **Key Files**: `shared/src/commonMain/kotlin/.../househelper/dashboard/RoomsContent.kt`

---

### Task 18: Add UI Tests for RoomSection
- **Objective**: Verify room section interactions and rendering using automated Compose UI unit tests.
- **Key Changes**: Write tests in `RoomSectionTest.kt` verifying header click events, expand/collapse toggles, and device card visibility.
- **Key Files**: `shared/src/jvmTest/kotlin/.../househelper/RoomSectionTest.kt`

---

## Bonus Workshop Tasks

### Bonus Task 1: Move Room State into RoomViewModel
- **Objective**: Architecturally decouple room UI from in-composable state by hoisting room expansion and device interaction into a dedicated `RoomViewModel`.
- **Key Changes**: Create `RoomViewModel.kt` to manage room-level StateFlows and delegate UI events from `RoomsContent.kt`.
- **Key Files**:
  - `shared/src/commonMain/kotlin/.../househelper/dashboard/RoomViewModel.kt`
  - `shared/src/commonMain/kotlin/.../househelper/dashboard/RoomsContent.kt`

---

### Bonus Task 2: Persist ViewModel State Using `SavedStateHandle`
- **Objective**: Ensure room expansion state survives process death and system-initiated state restoration in `RoomViewModel`.
- **Key Changes**: Inject `SavedStateHandle` into `RoomViewModel` and read/write expansion state keys via `getMutableStateFlow(...)` / `saveable`.
- **Key Files**: `shared/src/commonMain/kotlin/.../househelper/dashboard/RoomViewModel.kt`

---

### Bonus Task 3: Deep Linking to Light Details Screen
- **Objective**: Allow external applications and links (e.g. `househelper://light/{id}`) to route directly to a specific device details screen.
- **Key Changes**: Add deep link URI parsing and back-stack initialization in `App.kt`, and configure native platform intent filters / URL schemes.
- **Key Files**:
  - `shared/src/commonMain/kotlin/.../househelper/App.kt`
  - `androidApp/src/main/AndroidManifest.xml`
  - `iosApp/iosApp/`

---

### Bonus Task 4: Add Platform Share Action to Light Detail
- **Objective**: Enable users to export and share device information via native platform sharing sheets.
- **Key Changes**: Add a share action icon in `LightDetailsScreen.kt` triggering multiplatform text sharing.
- **Key Files**: `shared/src/commonMain/kotlin/.../househelper/devices/LightDetailsScreen.kt`

---

## Summary Checklist

| # | Task | Area | Description |
|:---|:---|:---|:---|
| **1** | Run starter project | Setup | Verify multiplatform build and run targets |
| **2** | Hot reload | Tooling | Rapid UI iteration with Compose Hot Reload |
| **3** | Room expansion | State | Expand/collapse room sections |
| **4** | Onboarding subtitle | UI / i18n | Localized subtitle text on onboarding pages |
| **5** | Camera ViewModel | Architecture | Dynamic camera state & power toggles |
| **6** | Network images | Media | Remote image loading with Coil `AsyncImage` |
| **7** | `rememberSaveable` | State | Preserve expansion state across recreation |
| **8** | ViewModels in DI | DI | Parameterized ViewModel injection |
| **9** | Navigate to rename | Navigation | Route to `RenameDevice` destination |
| **10** | Dialog rename screen | Navigation | Render rename screen in a modal dialog scene |
| **11** | Chevron animation | Motion | `animateFloatAsState` for 180° arrow rotation |
| **12** | Items animation | Motion | `AnimatedVisibility` for room device grid |
| **13** | Tab animation | Motion | `AnimatedContent` for dashboard tab transitions |
| **14** | Custom theme | Theming | Material 3 light & dark color schemes |
| **15** | Window insets | Layout | `safeDrawing` padding for edge-to-edge screens |
| **16** | Video player | Media | Multiplatform video streaming component |
| **17** | Accessibility | A11y | Merged semantics and click actions |
| **18** | UI testing | QA | Automated Compose UI tests for `RoomSection` |
| **B1** | `RoomViewModel` | Architecture | Hoist room state to dedicated ViewModel |
| **B2** | `SavedStateHandle` | Architecture | Process death state persistence |
| **B3** | Deep linking | Navigation | URL scheme handling for direct device routing |
| **B4** | Share action | Platform | Native platform text sharing integration |
