package com.kotlinconf.workshop.househelper

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kotlinconf.workshop.househelper.di.initApp
import javafx.application.Platform

fun main() {
    initApp()
    application {
        // Interop settings
        System.setProperty("compose.interop.blending", "true")
        Platform.setImplicitExit(false)

        Window(
            onCloseRequest = ::exitApplication,
            title = "HouseHelper",
            state = rememberWindowState(width = 500.dp, height = 800.dp),
            alwaysOnTop = true,
        ) {
            App()
        }
    }
}
