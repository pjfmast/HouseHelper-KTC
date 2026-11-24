# Verschillen en Migraties: HouseHelper-KTC vs. Oorspronkelijke Workshop

Dit document beschrijft de belangrijkste verschillen en migraties tussen de oorspronkelijke versie van de KotlinConf 2026 workshop code ([github.com/kropp/HouseHelper (branch `solutions`)](https://github.com/kropp/HouseHelper/tree/solutions)) en deze gemoderniseerde **HouseHelper-KTC** repository.

---

## Overzicht van de Belangrijkste Wijzigingen

| Onderdeel | Oorspronkelijk (`kropp/HouseHelper`) | HouseHelper-KTC |
| :--- | :--- | :--- |
| **Build & Tooling** | Gradle (`build.gradle.kts`, `settings.gradle.kts`, wrapper) | **Kotlin Toolchain (KTC / Amper)** (`project.yaml`, `module.yaml`, `./kotlin`) |
| **Dependency Injection** | **Metro** (compile-time code-generatie KMP DI) | **Koin 4.x** met Koin Compiler Plugin & Koin Annotations |
| **Libraries & Dependencies** | Oudere versies van Navigation 3, Lifecycle, Coil, Ktor | Geüpgraded naar nieuwste stabiele versies (o.a. Nav3 1.2.x, Lifecycle 2.11, Koin 4.2, Coil 3.6, Ktor 3.6) |
| **Navigatiemodel** | `Screens.kt` (`sealed interface Screen : NavKey`) | `Routes.kt` (`sealed interface Route : NavKey`) |
| **Deep Linking & Backstack** | Basis concepten | Navigation 3 DeepLink URI matchers (`withBackStack`) & unit tests |
| **Documentatie** | Engelstalige workshop slides en taken | Aanvullende Nederlandstalige architectuur- en migratiedocumentatie |

---

## 1. Migratie naar Kotlin Toolchain (KTC / Amper)

De oorspronkelijke workshop gebruikte het traditionele Gradle build-systeem met multiplatform Kotlin-DSL configuraties. In **HouseHelper-KTC** is het project volledig gemigreerd naar de nieuwe **Kotlin Toolchain (KTC)** aangedreven door JetBrains Amper:

* **Declaratieve YAML-configuratie**:
  * In plaats van `settings.gradle.kts` wordt het project globaal geconfigureerd in `project.yaml`.
  * In plaats van `build.gradle.kts` per module (zoals `shared`, `androidApp`, `desktopApp`, `iosApp`, `webApp`) heeft elke module nu een beknopt `module.yaml` bestand.
* **Directe platformdefinities**:
  * Multiplatform targets worden eenvoudig gedeclareerd: `platforms: [ jvm, android, iosArm64, iosSimulatorArm64, wasmJs ]`.
  * Platform-specifieke dependencies en bronbestanden worden georganiseerd via contextuele secties zoals `dependencies@android`, `dependencies@jvm`, `dependencies@ios` en folders zoals `src` en `test@jvm`.
* **Self-bootstrapping CLI-scripts**:
  * Het project bevat `kotlin` (Unix shell) en `kotlin.bat` (Windows) wrappers in de root.
  * Geen zware lokale Gradle installatie of daemon configuratie nodig; opdrachten kunnen direct uitgevoerd worden via `./kotlin build`, `./kotlin test` of `./kotlin run -m desktopApp`.
* **Vereenvoudigde resource-generatie**:
  * Compose Multiplatform resources worden rechtstreeks beheerd via de declaratie `settings.compose.resources` in `module.yaml`.

---

## 2. Koin in plaats van Metro (Dependency Injection)

In de oorspronkelijke workshopcode werd **Metro** gebruikt als DI-framework (een experimenteel compile-time code-generatie framework voor Kotlin Multiplatform):

* **Overstap naar Koin 4.x (`4.2.2`)**:
  * Volledig idiomatische Koin-setup voor Kotlin Multiplatform.
  * **Koin Compiler Plugin (`1.2.3`)**: Geactiveerd via `settings.kotlin.compilerPlugins: [ id: io.insert-koin.compiler.plugin, version: $koin-compiler-plugin ]` in `shared/module.yaml`. Hierdoor worden injecties en module-definities tijdens het compileren gecontroleerd en geoptimaliseerd zonder extra KSP-overhead.
* **Koin Annotations**:
  * Gebruik van `@Module`, `@ComponentScan`, `@Singleton` (voor bijv. `DemoHouseService`), `@KoinViewModel` en `@InjectedParam` voor parameterized (assisted) injectie.
  * Applicatiestart via `startKoin<KoinApp>()` en `initApp()` in `AppModule.kt`.
* **Compose ViewModel integratie (`koin-compose-viewmodel`)**:
  * Schermen injecteren ViewModels direct in Compose via `koinViewModel()` en met parameters via:
    ```kotlin
    val viewModel: LightDetailsViewModel = koinViewModel { parametersOf(deviceId) }
    ```
  * Ondersteunt parameterized ViewModels (`LightDetailsViewModel`, `CameraDetailsViewModel`, `RenameDeviceViewModel` en `RoomViewModel`).

---

## 3. Update van Libraries & API-Moderniseringen

Verschillende kernbibliotheken zijn bijgewerkt naar recentere versies (`libs.versions.toml`):

* **AndroidX Navigation 3**:
  * Geüpgraded naar `androidx-navigation3 = "1.2.0"` (Multiplatform) en `1.2.2` (Android).
  * Volledige integratie van de nieuwe Navigation 3 Deep Link API's: `UriDeepLinkMatcher`, `DeepLinkUri`, `DeepLinkRequest`, `withBackStack` en `BackStackMatchResult`.
  * Ondersteuning voor state restoration via `rememberSaveableStateHolderNavEntryDecorator()` en `rememberViewModelStoreNavEntryDecorator()`.
* **AndroidX Lifecycle & ViewModel**:
  * Geüpgraded naar versie `2.11.0` (o.a. `lifecycle-viewmodel-compose`, `lifecycle-runtime-compose` en `lifecycle-viewmodel-navigation3`).
* **AndroidX Activity Compose**:
  * Geüpgraded naar `1.13.0`.
* **Image Loading & Networking**:
  * **Coil 3** geüpgraded naar `3.6.3` (`coil-compose`, `coil-network-ktor3`).
  * **Ktor** geüpgraded naar versie `3.6.1` (`ktor-client-okhttp`, `ktor-client-darwin`).
* **KotlinX Ecosystem**:
  * `kotlinx-coroutines = "1.11.0"`
  * `kotlinx-serialization = "1.13.0"`
* **Desktop & Platform Libraries**:
  * JavaFX `23.0.2` voor desktop video rendering.
  * Logback `1.6.2`.
* **Verwijderen van Overbodige `@OptIn` Annotaties**:
  * Verschillende API's (bijv. binnen Compose UI en Navigation) die in de oorspronkelijke workshop nog experimenteel waren, zijn nu stabiel. Onnodige `@OptIn` annotaties zijn opgeschoond.

---

## 4. Hernoemen van 'Screen' naar 'Route' voor Backstack Entries

In de oorspronkelijke versie waren de navigatiesleutels gedefinieerd als schermen (`Screens.kt`):

* **Oorspronkelijk**:
  * Bestand: `Screens.kt`
  * Interface: `sealed interface Screen : NavKey`
  * Objecten/Classes: `Screen.Dashboard`, `Screen.LightDetails`, `Screen.CameraDetails`, etc.
* **In HouseHelper-KTC**:
  * Bestand: `Routes.kt`
  * Interface: `sealed interface Route : NavKey`
  * Objecten/Classes: `Route.Dashboard`, `Route.LightDetails`, `Route.CameraDetails`, `Route.RenameDevice`, etc.

### Waarom deze wijziging?
1. **Duidelijke scheiding van verantwoordelijkheden**:
   * Een **Route** is een zuiver data-object (`NavKey`) dat de parameters en identiteit van een navigatiebestemming op de `backStack` vertegenwoordigt.
   * Een **Screen** is de visuele Composable UI-weergave (zoals `DashboardScreen`, `LightDetailsScreen`, `RenameDeviceScreen`).
   * Dit voorkomt verwarring tussen het navigatielabel/de sleutel en de feitelijke UI-implementatie.
2. **Aansluiting bij officiële Navigation 3 conventies**:
   * In Jetpack Navigation 3 en moderne Compose-architecturen wordt de term *Route* of *Key* standaard gehanteerd voor de elementen op de backstack.
3. **Type-safe State Persistence**:
   * In `Routes.kt` is tevens de `houseHelperSavedStateConfig` gedefinieerd (`SavedStateConfiguration`), waarbij polymorfe serialisatie via `subclassesOfSealed<Route>()` zorgt dat alle routes betrouwbaar bewaard en hersteld worden bij configuratiewijzigingen en process death.

---

## 5. Aanvullende Toevoegingen in HouseHelper-KTC

* **Automatische Deep Link Backstack Reconstructie**:
  * In `App.kt` worden deep links (bijv. `househelper://light/{id}`) via `withBackStack` automatisch opgebouwd met `Dashboard` als root, zodat bij terugnavigeren (`onBack`) de gebruiker altijd netjes op het dashboard belandt.
* **Geautomatiseerde Unit Tests**:
  * Toegevoegd in `shared/test@jvm/.../DeepLinkTest.kt`: unit tests die valideren dat deep links correct parsen naar de verwachte backstack en dat ongeldige URI's veilig worden genegeerd.
* **Architectuurdocumentatie**:
  * Toegevoegd in `documentation/navigation3_architectuur.md` en `navigation3_architectuur.svg`: een compleet visueel en tekstueel overzicht van de Navigation 3 keten (`NavKey` &rarr; `NavBackStack` &rarr; `NavDisplay` &rarr; `EntryProvider` &rarr; `SceneStrategy` &rarr; `NavEntry` / `Scene`).
