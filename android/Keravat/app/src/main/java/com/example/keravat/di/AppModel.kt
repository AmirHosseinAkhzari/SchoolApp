package com.example.keravat.di

import android.app.Application
import com.example.keravat.data.remote.KeravatApi
import com.example.keravat.domain.repo.KeravatRepo
import com.example.keravat.domain.repo.KeravatRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun ProvideAstinApi() : KeravatApi {
        return Retrofit
            .Builder()
            .baseUrl("https://astinapp.ir/android/keravat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KeravatApi::class.java)
    }



    @Provides
    @Singleton
    fun provideAstinRepo(api : KeravatApi ) : KeravatRepo {
        return KeravatRepoImpl(api)
    }


}