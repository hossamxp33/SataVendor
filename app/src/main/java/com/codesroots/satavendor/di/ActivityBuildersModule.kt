package com.codesroots.satavendor.di

import com.codesroots.dagger.di.scopes.ActivityScope
import com.codesroots.satavendor.MainActivity
import com.codesroots.satavendor.helper.FragmentFactoryModule
import com.codesroots.satavendor.presentation.auth.LoginActivity
import com.codesroots.satavendor.presentation.map_activity.MapActivity
import com.codesroots.satavendor.presentation.splashScreen.SplashScreen


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