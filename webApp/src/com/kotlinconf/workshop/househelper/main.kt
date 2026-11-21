package com.kotlinconf.workshop.househelper

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kotlinconf.workshop.househelper.di.initApp

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initApp()
    ComposeViewport {
        App()
    }
}
