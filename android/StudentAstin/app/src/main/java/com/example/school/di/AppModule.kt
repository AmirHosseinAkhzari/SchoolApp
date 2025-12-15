package com.example.school.di

import android.app.Application
import com.example.school.data.model.NfcManager
import com.example.school.data.remote.AstinApi
import com.example.school.domain.repository.AstinRepo
import com.example.school.domain.repository.AstinRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Provider
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun ProvideAstinApi() : AstinApi {
        return Retrofit
            .Builder()
            .baseUrl("https://astinapp.ir/android/astin/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AstinApi::class.java)
    }



    @Provides
    @Singleton
    fun NfcManager(appContext : Application ) : NfcManager {
        return NfcManager(appContext.applicationContext)
    }


    @Provides
    @Singleton
    fun provideAstinRepo(api : AstinApi  ,appContext : Application  ,nfcManager: NfcManager) : AstinRepo {
        return AstinRepoImpl(api ,  nfcManager , appContext )
    }


}