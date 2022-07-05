package com.sata.satadelivery.di

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.sata.satadelivery.datalayer.APIServices
import com.sata.satadelivery.helper.PreferenceHelper
import com.sata.satadelivery.repository.DataSource
import com.sata.satadelivery.repository.RemoteDataSource

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule() {


//    @Singleton
//    @Provides
//    fun provideTasksRemoteDataSource(apiService: APIServices): DataSource {
//        return RemoteDataSource(apiService)
//    }


    @Singleton
    @Provides
    fun provideTasksBranchDataSource(apiService: APIServices): DataSource {
        return RemoteDataSource(apiService)
        }
        @Provides
    fun providePreferenceHelper(context: Context): PreferenceHelper {
        return PreferenceHelper(context)
    }
    @Provides
    fun provideSweetAlertDialog(context: Context): SweetAlertDialog {
        return SweetAlertDialog(context)
    }
}
