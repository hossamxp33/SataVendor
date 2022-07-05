package com.sata.satadelivery.di

import com.sata.dagger.di.scopes.ActivityScope
import com.sata.satadelivery.MainActivity
import com.sata.satadelivery.helper.FragmentFactoryModule
import com.sata.satadelivery.presentation.auth.LoginActivity
import com.sata.satadelivery.presentation.map_activity.MapActivity
import com.sata.satadelivery.presentation.splashScreen.SplashScreen


import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
Created by Prokash Sarkar on Tue, January 19, 2021
 **/

@Module
interface ActivityBuildersModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class, FragmentFactoryModule::class])
    fun contributeMainActivity(): MainActivity



//  @ActivityScope
//    @ContributesAndroidInjector(modules = [MainModule::class, FragmentFactoryModule::class])
//    fun contributeLoginAsActivity(): LoginAsActivity
//
//  @ActivityScope
//    @ContributesAndroidInjector(modules = [MainModule::class, FragmentFactoryModule::class])
//    fun contributeSplashScreen(): SplashScreen

  @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class, FragmentFactoryModule::class])
    fun contributeMapActivity(): MapActivity

  @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class, FragmentFactoryModule::class])
    fun contributeLoginActivity(): LoginActivity

  @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class, FragmentFactoryModule::class])
    fun contributeSplashScreen(): SplashScreen




}