package com.kotlinconf.workshop.househelper.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.kotlinconf.workshop.househelper.DeviceId
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface Route : NavKey {
    // Onboarding routes
    @Serializable data object OnboardingWelcome : Route
    @Serializable data object OnboardingAbout : Route
    @Serializable data object OnboardingDone : Route

    // Main routes
    @Serializable data object Dashboard : Route
    @Serializable data class LightDetails(val deviceId: DeviceId) : Route
    @Serializable data class CameraDetails(val deviceId: DeviceId) : Route
    @Serializable data class RenameDevice(val deviceId: DeviceId) : Route
}

// Centrale configuratie voor veilige state restoration over procesherstarts
@OptIn(ExperimentalSerializationApi::class)
val houseHelperSavedStateConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<Route>()
        }
    }
}
