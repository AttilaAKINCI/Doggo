package com.akinci.doggoappcompose.data.di

import com.akinci.doggoappcompose.common.network.NetworkChecker
import com.akinci.doggoappcompose.common.repository.BaseRepository
import com.akinci.doggoappcompose.data.remote.api.DoggoServiceDao
import com.akinci.doggoappcompose.data.local.DoggoDatabase
import com.akinci.doggoappcompose.data.local.dao.BreedDao
import com.akinci.doggoappcompose.data.local.dao.ContentDao
import com.akinci.doggoappcompose.data.local.dao.SubBreedDao
import com.akinci.doggoappcompose.data.repository.DoggoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DoggoModule {

    @Provides
    @Singleton
    fun provideDoggoService(
        retrofit: Retrofit
    ): DoggoServiceDao = retrofit.create(DoggoServiceDao::class.java)

    @Provides
    @Singleton
    fun provideDoggoRepository(
        doggoServiceDao: DoggoServiceDao,
        breedDao: BreedDao,
        subBreedDao: SubBreedDao,
        contentDao: ContentDao,
        networkChecker: NetworkChecker,
        baseRepository: BaseRepository,
    ) = DoggoRepository(doggoServiceDao, breedDao, subBreedDao, contentDao, networkChecker, baseRepository)

    /** ROOM DB related definitions **/
    @Provides
    @Singleton
    fun provideBreedDao(db: DoggoDatabase): BreedDao = db.getBreedDao()

    @Provides
    @Singleton
    fun provideSubBreedDao(db: DoggoDatabase): SubBreedDao = db.getSubBreedDao()

    @Provides
    @Singleton
    fun provideContentDao(db: DoggoDatabase): ContentDao = db.getContentDao()

}