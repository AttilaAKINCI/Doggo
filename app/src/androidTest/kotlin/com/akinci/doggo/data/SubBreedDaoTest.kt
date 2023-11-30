package com.akinci.doggo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.akinci.doggo.core.storage.AppDatabase
import com.akinci.doggo.data.subbreed.local.SubBreedDao
import com.akinci.doggo.data.subbreed.local.SubBreedEntity
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
class SubBreedDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: AppDatabase

    private lateinit var subBreedDao: SubBreedDao

    @Before
    fun setup() {
        hiltRule.inject()
        subBreedDao = db.getSubBreedDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertSubBreedAndGetAllSubBreeds() = runTest {
        val subBreedList = listOf(
            SubBreedEntity(breed = "bulldog", name = "boston"),
            SubBreedEntity(breed = "bulldog", name = "english"),
            SubBreedEntity(breed = "hound", name = "afghan")
        )

        subBreedDao.insertSubBreeds(subBreedList)

        val result = subBreedDao.getAllSubBreeds(breed = "bulldog")

        result.size shouldBe 2
        result[0].breed shouldBe "bulldog"
        result[0].name shouldBe "boston"
        result[1].breed shouldBe "bulldog"
        result[1].name shouldBe "english"

        advanceUntilIdle()
    }
}
