package com.example.cote.di

import com.example.cote.data.remote.CoteApi
import com.example.cote.domain.repo.CoteRepo
import com.example.cote.domain.repo.CoteRepoImpl
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
    fun ProvideAstinApi() : CoteApi {
        return Retrofit
            .Builder()
            .baseUrl("https://astinapp.ir/android/keravat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoteApi::class.java)
    }



    @Provides
    @Singleton
    fun provideAstinRepo(api : CoteApi ) : CoteRepo {
        return CoteRepoImpl(api)
    }


}