package com.akinci.doggoapp.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.akinci.doggoapp.coroutine.TestContextProvider
import com.akinci.doggoapp.data.local.DoggoDatabase
import com.akinci.doggoapp.data.local.entity.BreedEntity
import com.akinci.doggoapp.data.local.entity.SubBreedEntity
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
class SubBreedDaoTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: DoggoDatabase

    private val coroutineContext = TestContextProvider()
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
    fun insertSubBeeedAndGetAllSubBreeds() {
        val breed = "bulldog"
        val subBreedList = listOf(
            SubBreedEntity(breed= breed, name = "boston", selected = true),
            SubBreedEntity(breed= breed, name = "english", selected = false)
        )

        runBlockingTest {
            subBreedDao.insertSubBreed(subBreedList)
        }

        val fetchedList = subBreedDao.getAllSubBreeds(breed = breed)

        for (index in fetchedList.indices){
            assertThat(fetchedList[index].breed).isEqualTo(subBreedList[index].breed)
            assertThat(fetchedList[index].name).isEqualTo(subBreedList[index].name)
            assertThat(fetchedList[index].selected).isEqualTo(subBreedList[index].selected)
        }

        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()
    }
}