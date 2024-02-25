package com.akinci.doggo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.akinci.doggo.data.room.AppDatabase
import com.akinci.doggo.data.room.breed.BreedDao
import com.akinci.doggo.data.room.breed.BreedEntity
import com.akinci.doggo.di.TestAppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@SmallTest
class BreedDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: AppDatabase

    private lateinit var breedDao: BreedDao

    @Before
    fun setup() {
        hiltRule.inject()
        breedDao = db.getBreedDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getAllBreedsFromRoomDB() = runTest {
        val breedList = listOf(
            BreedEntity(name = "hound"),
            BreedEntity(name = "bulldog")
        )

        breedDao.insertBreeds(breedList)

        val result = breedDao.getAllBreeds()

        result.size shouldBe 2
        result[0].name shouldBe "hound"
        result[1].name shouldBe "bulldog"

        advanceUntilIdle()
    }

}