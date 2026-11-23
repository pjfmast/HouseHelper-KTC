package com.kotlinconf.workshop.househelper.navigation

import com.kotlinconf.workshop.househelper.DeviceId
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    // Start routes
    @Serializable data object OnboardingWelcome : Route
    @Serializable data object OnboardingAbout : Route
    @Serializable data object OnboardingDone : Route

    // Main routes
    @Serializable data object Dashboard : Route
    @Serializable data class LightDetails(val deviceId: DeviceId) : Route
    @Serializable data class CameraDetails(val deviceId: DeviceId) : Route

    // TODO Task 9: add new navigation class
}
