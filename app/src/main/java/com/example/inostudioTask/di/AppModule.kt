package com.example.inostudioTask.di

import com.example.inostudioTask.common.Constants
import com.example.inostudioTask.data.remote.FilmApi
import com.example.inostudioTask.data.repository.FilmRepositoryImpl
import com.example.inostudioTask.domain.repository.FilmRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideFilmApi(): FilmApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
            .build()
            .create(FilmApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsFilmRepository(filmRepositoryImpl: FilmRepositoryImpl): FilmRepository
}


