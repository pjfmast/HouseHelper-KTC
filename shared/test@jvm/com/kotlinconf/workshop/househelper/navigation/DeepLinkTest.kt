package com.kotlinconf.workshop.househelper.navigation

import com.kotlinconf.workshop.househelper.DeviceId
import com.kotlinconf.workshop.househelper.navigation.Route.CameraDetails
import com.kotlinconf.workshop.househelper.navigation.Route.Dashboard
import com.kotlinconf.workshop.househelper.navigation.Route.LightDetails
import com.kotlinconf.workshop.househelper.resolveDeepLink
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeepLinkTest {
    @Test
    fun lightDeepLinkCreatesDashboardAndLightDetailsBackStack() {
        assertEquals(
            listOf(Dashboard, LightDetails(DeviceId("living_room_floor_lamp"))),
            resolveDeepLink("househelper://light/living_room_floor_lamp"),
        )
    }

    @Test
    fun cameraDeepLinkCreatesDashboardAndCameraDetailsBackStack() {
        assertEquals(
            listOf(Dashboard, CameraDetails(DeviceId("bathroom_security_camera"))),
            resolveDeepLink("househelper://camera/bathroom_security_camera"),
        )
    }

    @Test
    fun unknownSchemeDoesNotMatch() {
        assertNull(resolveDeepLink("https://househelper/light/living_room_floor_lamp"))
    }

    @Test
    fun unknownPathDoesNotMatch() {
        assertNull(resolveDeepLink("househelper://unknown/device"))
    }
}
