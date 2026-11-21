package com.kotlinconf.workshop.househelper.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.plugin.module.dsl.startKoin

@Module
@Configuration
@ComponentScan("com.kotlinconf.workshop.househelper")
class AppModule

@KoinApplication
object KoinApp

fun initApp(appDeclaration: KoinAppDeclaration? = null) {
    startKoin<KoinApp> {
        appDeclaration?.invoke(this)
    }
}
