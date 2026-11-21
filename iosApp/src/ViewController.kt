package com.kotlinconf.workshop.househelper

import androidx.compose.ui.window.ComposeUIViewController
import com.kotlinconf.workshop.househelper.di.initApp

fun ViewController() = ComposeUIViewController(
    configure = {
        initApp()
    }
) { App() }
