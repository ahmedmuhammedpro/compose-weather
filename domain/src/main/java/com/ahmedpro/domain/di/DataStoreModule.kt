package com.ahmedpro.domain.di

import android.content.Context
import com.ahmedpro.domain.datastore.PrefStore
import com.ahmedpro.domain.datastore.PrefStoreImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesPrefStore(
        @ApplicationContext context: Context,
        gson: Gson
    ): PrefStore {
        return PrefStoreImpl( context = context, gson = gson)
    }
}