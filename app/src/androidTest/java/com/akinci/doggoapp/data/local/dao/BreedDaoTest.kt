package com.akinci.doggoapp.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.akinci.doggoapp.coroutine.TestContextProvider
import com.akinci.doggoapp.data.local.DoggoDatabase
import com.akinci.doggoapp.data.local.entity.BreedEntity
import com.akinci.doggoapp.di.TestAppModule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
@ExperimentalCoroutinesApi
class BreedDaoTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: DoggoDatabase

    private val coroutineContext = TestContextProvider()
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
    fun insertBreedAndGetAllBreeds() {
        val breedList = listOf(
            BreedEntity(name = "hound", selected = false),
            BreedEntity(name = "bulldog", selected = true)
        )

        runBlockingTest {
            breedDao.insertBreed(breedList)
        }

        val fetchedList = breedDao.getAllBreeds()

        for (index in fetchedList.indices){
            assertThat(fetchedList[index].name).isEqualTo(breedList[index].name)
            assertThat(fetchedList[index].selected).isEqualTo(breedList[index].selected)
        }

        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()
    }
}