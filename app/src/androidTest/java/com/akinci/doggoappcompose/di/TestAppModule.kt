package com.akinci.doggoappcompose.di

import android.content.Context
import androidx.room.Room
import com.akinci.doggoappcompose.data.local.DoggoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TestDB

    @Provides
    @TestDB
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context,
            DoggoDatabase::class.java
        ).allowMainThreadQueries().build()

}