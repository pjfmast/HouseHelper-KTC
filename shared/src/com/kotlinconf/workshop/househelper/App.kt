package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.deeplink.BackStackMatchResult
import androidx.navigation3.runtime.deeplink.DeepLinkMatcher
import androidx.navigation3.runtime.deeplink.DeepLinkRequest
import androidx.navigation3.runtime.deeplink.DeepLinkUri
import androidx.navigation3.runtime.deeplink.UriDeepLinkMatcher
import androidx.navigation3.runtime.deeplink.withBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.kotlinconf.workshop.househelper.dashboard.DashboardScreen
import com.kotlinconf.workshop.househelper.devices.CameraDetailsScreen
import com.kotlinconf.workshop.househelper.devices.LightDetailsScreen
import com.kotlinconf.workshop.househelper.devices.RenameDeviceScreen
import com.kotlinconf.workshop.househelper.navigation.Route.CameraDetails
import com.kotlinconf.workshop.househelper.navigation.Route.Dashboard
import com.kotlinconf.workshop.househelper.navigation.Route.LightDetails
import com.kotlinconf.workshop.househelper.navigation.Route.OnboardingAbout
import com.kotlinconf.workshop.househelper.navigation.Route.OnboardingDone
import com.kotlinconf.workshop.househelper.navigation.Route.OnboardingWelcome
import com.kotlinconf.workshop.househelper.navigation.Route.RenameDevice
import com.kotlinconf.workshop.househelper.navigation.Route
import com.kotlinconf.workshop.househelper.navigation.houseHelperSavedStateConfig
import com.kotlinconf.workshop.househelper.theme.AppDarkColorScheme
import com.kotlinconf.workshop.househelper.theme.AppLightColorScheme
import com.kotlinconf.workshop.househelper.theme.AppShapes
import househelper.shared.generated.resources.Res
import househelper.shared.generated.resources.onboarding_about
import househelper.shared.generated.resources.onboarding_about_subtitle
import househelper.shared.generated.resources.onboarding_done
import househelper.shared.generated.resources.onboarding_done_subtitle
import househelper.shared.generated.resources.onboarding_next_button
import househelper.shared.generated.resources.onboarding_welcome
import househelper.shared.generated.resources.onboarding_welcome_subtitle
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.serializer
import org.jetbrains.compose.resources.stringResource

/**
 * Handles links in the househelper://{type}/{id} format
 *
 * Examples:
 * househelper://light/living_room_floor_lamp
 * househelper://camera/bathroom_security_camera
 */
fun navigateToDeepLink(uri: String) {
    resolveDeepLink(uri)?.let(deeplinkRequests::trySend)
}

// Collating matchers with different generic types requires wildcards (official Navigation 3 pattern).
// Every matcher uses withBackStack with Route keys, so the cast in resolveDeepLink is safe.
private val deepLinkMatchers: List<DeepLinkMatcher<*, *>> = listOf(
    UriDeepLinkMatcher(
        DeepLinkUri("househelper://light/{deviceId}"),
        serializer<LightDetails>(),
    ).withBackStack { listOf(Dashboard, it.key) },
    UriDeepLinkMatcher(
        DeepLinkUri("househelper://camera/{deviceId}"),
        serializer<CameraDetails>(),
    ).withBackStack { listOf(Dashboard, it.key) },
)

internal fun resolveDeepLink(uri: String): List<Route>? {
    val request = DeepLinkRequest(uri)
    val result = deepLinkMatchers.mapNotNull { it.match(request) }.maxOrNull() ?: return null
    @Suppress("UNCHECKED_CAST")
    return (result as BackStackMatchResult<*, *>).backStack as List<Route>
}

private val deeplinkRequests = Channel<List<Route>>(capacity = 1)

@Composable
fun App() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) {
            AppDarkColorScheme
        } else {
            AppLightColorScheme
        },
        shapes = AppShapes,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val backStack = rememberNavBackStack(
                houseHelperSavedStateConfig,
                OnboardingWelcome
            )
            val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }

            LaunchedEffect(Unit) {
                while (true) {
                    val routes = deeplinkRequests.receive()
                    backStack.clear()
                    backStack.addAll(routes)
                }
            }

            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                sceneStrategies = listOf(dialogStrategy),
                entryProvider = entryProvider {
                    entry<OnboardingWelcome> {
                        OnboardingScreen(
                            text = stringResource(Res.string.onboarding_welcome),
                            subtitle = stringResource(Res.string.onboarding_welcome_subtitle),
                            buttonText = stringResource(Res.string.onboarding_next_button),
                            icon = Icons.Default.Favorite,
                            onNext = { backStack.add(OnboardingAbout) }
                        )
                    }
                    entry<OnboardingAbout> {
                        OnboardingScreen(
                            text = stringResource(Res.string.onboarding_about),
                            subtitle = stringResource(Res.string.onboarding_about_subtitle),
                            buttonText = stringResource(Res.string.onboarding_next_button),
                            icon = Icons.Default.Info,
                            onNext = { backStack.add(OnboardingDone) }
                        )
                    }
                    entry<OnboardingDone> {
                        OnboardingScreen(
                            text = stringResource(Res.string.onboarding_done),
                            subtitle = stringResource(Res.string.onboarding_done_subtitle),
                            buttonText = stringResource(Res.string.onboarding_next_button),
                            icon = Icons.Default.Home,
                            onNext = {
                                backStack.clear()
                                backStack.add(Dashboard)
                            }
                        )
                    }
                    entry<Dashboard> {
                        DashboardScreen(
                            onNavigateToLightDetails = { deviceId ->
                                backStack.add(LightDetails(deviceId))
                            },
                            onNavigateToCameraDetails = { deviceId ->
                                backStack.add(CameraDetails(deviceId))
                            }
                        )
                    }
                    entry<LightDetails> {
                        LightDetailsScreen(
                            deviceId = it.deviceId,
                            onNavigateUp = { backStack.removeLastOrNull() },
                        )
                    }
                    entry<CameraDetails> {
                        CameraDetailsScreen(
                            deviceId = it.deviceId,
                            onNavigateUp = { backStack.removeLastOrNull() },
                            onNavigateToRename = { deviceId ->
                                backStack.add(RenameDevice(deviceId))
                            },
                        )
                    }
                    entry<RenameDevice>(
                        metadata = DialogSceneStrategy.dialog()
                    ) {
                        RenameDeviceScreen(
                            deviceId = it.deviceId,
                            onDismiss = {
                                backStack.removeLastOrNull()
                            },
                        )
                    }
                },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            )
        }
    }
}
