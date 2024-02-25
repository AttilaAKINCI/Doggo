package com.akinci.doggo.data.di

import android.content.Context
import androidx.room.Room
import com.akinci.doggo.data.room.AppDatabase
import com.akinci.doggo.data.room.AppDatabaseKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabaseKeys.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideBreedDao(database: AppDatabase) = database.getBreedDao()

    @Provides
    fun provideSubBreedDao(database: AppDatabase) = database.getSubBreedDao()

    @Provides
    fun provideImageDao(database: AppDatabase) = database.getImageDao()
}
