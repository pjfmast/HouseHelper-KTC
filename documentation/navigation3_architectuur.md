# Navigation 3 — Architectuurketen

Overzicht van de kernarchitectuur en gegevensstroom binnen Jetpack Compose **Navigation 3**.

---

## Kernbegrippen

### 1. `NavKey` (of Route / Key)
* **KDoc / Officiële definitie:** Een unieke identifier voor een bestemming/scherm.
* **Rol:** Dient als het pure data-model voor navigatie. Bevat geen UI-code, maar uitsluitend de parameters die nodig zijn om een scherm te identificeren en de toestand te reconstrueren (bijvoorbeeld `@Serializable` class of object zoals `Route.Dashboard` of `LightDetails(val deviceId: String)`).

### 2. `NavBackStack` (of `backStack`)
* **KDoc / Officiële definitie:** Een state-holder en observabele lijst (`SnapshotStateList<T>`) die de geschiedenis van navigatiesleutels (`NavKey`'s) bijhoudt.
* **Rol:** Beheert de navigatiegeschiedenis. In Navigation 3 is de backstack een standaard Compose state-lijst. Navigeren gebeurt via reguliere lijstoperaties (`backStack.add(...)`, `backStack.removeLastOrNull()`, `backStack.clear()`), vaak aangemaakt via `rememberNavBackStack(...)`.

### 3. `NavDisplay`
* **KDoc / Officiële definitie:** *"A composable that displays the current screen based on the `backStack`."*
* **Rol:** Het centrale UI-component dat alle onderdelen samenbrengt:
  1. Observeert de `backStack`.
  2. Haalt via de `entryProvider` de `NavEntry`'s op voor de sleutels op de backstack.
  3. Doorloopt de `sceneStrategies` om te bepalen welke `Scene` (en layout) getoond moet worden.
  4. Handelt terugnavigatie af via `onBack`.

### 4. `EntryProvider` (en `entryProvider { ... }`)
* **KDoc / Officiële definitie:** *"A function that provides a `NavEntry` for a given key."* (`(key: T) -> NavEntry<T>`).
* **Rol:** Fungeert als de router of opzoekfunctie (lookup). Zet een willekeurige navigatiesleutel (`NavKey`) uit de backstack om naar een concrete `NavEntry` (met UI en metadata). Met de DSL `entryProvider { entry<A> { ... }; entry<B> { ... } }` worden alle bekende bestemmingen op één centrale plek geregistreerd.

### 5. `NavEntry` (en `entry<T>`)
* **KDoc / Officiële definitie:** *"An entry in the navigation hierarchy, representing a destination."*
* **Rol:** Koppelt een specifieke `NavKey` aan de bijbehorende UI (Compose content), een uniek ID en optionele `metadata`. Via `entry<T> { ... }` wordt gedefinieerd welke composable getoond moet worden wanneer een specifieke sleutel `T` actief is.

### 6. `SceneStrategy`
* **KDoc / Officiële definitie:** *"A strategy to determine which scene to render a list of entries."*
* **Rol:** Bepaalt hoe de actieve entries op het scherm gepositioneerd en weergegeven worden. Een strategie inspecteert de lijst van `NavEntry`'s en berekent een `Scene` (of geeft `null` terug).
  * `SinglePaneSceneStrategy`: toont de bovenste entry schermvullend (standaard fallback).
  * `TwoPaneSceneStrategy`: toont twee entries naast elkaar (voor tablets of foldables).
  * `DialogSceneStrategy`: inspecteert de metadata van de bovenste entry en toont deze in een popup `Dialog` overlay.

### 7. `Scene`
* **KDoc / Officiële definitie:** De berekende UI-weergave/layout die daadwerkelijk getoond wordt (bijvoorbeeld `DialogScene` of `SinglePaneScene`).

### 8. `NavEntryDecorator`
* **KDoc / Officiële definitie:** *"Wraps the content of a `NavEntry` with additional behaviors or scopes."*
* **Rol:** Voegt optionele lifecycle- en state-management functionaliteit toe aan elke entry (`rememberSaveableStateHolderNavEntryDecorator` voor UI-statebehoud en `rememberViewModelStoreNavEntryDecorator` voor scoped ViewModels en Lifecycles).

---

## Architectuurdiagram

### Tekstweergave (Flow)

```text
               +---------------------------------------+
               |                NavKey                 |  [DATA / ROUTE]
               | (Puur data-object, bijv. Dashboard)   |
               +---------------------------------------+
                                   |
                                   | 1. Geplaatst op
                                   v
               +---------------------------------------+
               |             NavBackStack              |  [STATE HOLDER]
               | (Observabele lijst met geschiedenis)  |
               +---------------------------------------+
                                   |
                                   | 2. Geobserveerd door
                                   v
               +---------------------------------------+
               |              NavDisplay               |  [CORE CONTROLLER]
               | (Centrale Composable container)       |
               +---------------------------------------+
                        /                     \
       3. Zoekt UI op  /                       \  5. Geeft List<NavEntry> door
                      v                         v
+---------------------------+             +---------------------------+
|       EntryProvider       |             |       SceneStrategy       |
| (Lookup router DSL)       |             | (Layout: Dialog, Pane)    |
+---------------------------+             +---------------------------+
              |                                         |
 4. Levert op |                                         | 6. Berekent Scene
              v                                         v
+---------------------------+  Rendert inhoud   +---------------------------+
|         NavEntry          | ----------------> |           Scene           |
| (UI Content + Metadata)   |                   | (Uiteindelijke UI layout) |
+---------------------------+                   +---------------------------+
```

---

## Bestanden

* Architectuurdiagram (SVG vector voor presentaties): `navigation3_architectuur.svg`
