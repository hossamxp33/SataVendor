package com.codesroots.satavendor.helper


import com.codesroots.satavendor.di.AppComponent
import com.codesroots.satavendor.di.DaggerAppComponent
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import java.net.URISyntaxException

/**
Created by Prokash Sarkar on Tue, January 19, 2021
 **/

open class BaseApplication : DaggerApplication() {
    private var mSocket: Socket? = null
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return initializeDaggerComponent()
    }

    companion object {
        lateinit var appComponent: AppComponent

    }
    override fun onCreate() {
        super.onCreate()
        try {
//creating socket instance

            mSocket = IO.socket(Constants.SocketURL)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
        initDI()
    }

    private fun initDI() {
        appComponent = DaggerAppComponent.factory()
            .create(this)

    }

    open fun initializeDaggerComponent(): AppComponent {
        val component: AppComponent = DaggerAppComponent.factory()
            .create(this)

        component.inject(this)

        return component
    }
    fun getMSocket(): Socket? {
        return mSocket
    }

}