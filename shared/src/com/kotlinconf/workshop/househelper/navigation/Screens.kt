package com.kotlinconf.workshop.househelper.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.kotlinconf.workshop.househelper.DeviceId
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface Screen : NavKey

// Onboarding screens
@Serializable data object OnboardingWelcome : Screen
@Serializable data object OnboardingAbout : Screen
@Serializable data object OnboardingDone : Screen

// Main screens
@Serializable data object Dashboard : Screen
@Serializable data class LightDetails(val deviceId: DeviceId) : Screen
@Serializable data class CameraDetails(val deviceId: DeviceId) : Screen
@Serializable data class RenameDevice(val deviceId: DeviceId) : Screen

// Centrale configuratie voor veilige state restoration over procesherstarts
@OptIn(ExperimentalSerializationApi::class)
val houseHelperSavedStateConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<Screen>()
        }
    }
}
