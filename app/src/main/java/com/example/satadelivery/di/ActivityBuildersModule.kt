package com.example.satadelivery.di

import com.example.dagger.di.scopes.ActivityScope
import com.example.satadelivery.MainActivity
import com.example.satadelivery.helper.FragmentFactoryModule
import com.example.satadelivery.presentation.auth.LoginActivity
import com.example.satadelivery.presentation.map_activity.MapActivity
import com.example.satadelivery.presentation.splashScreen.SplashScreen


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