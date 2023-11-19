package com.akinci.doggo.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.akinci.doggo.coroutine.TestContextProvider
import com.akinci.doggo.data.image.local.ImageDao
import com.akinci.doggo.data.local.DoggoDatabase
import com.akinci.doggo.data.image.local.ImageEntity
import com.akinci.doggo.di.TestAppModule
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
class ContentDaoTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: DoggoDatabase

    private val coroutineContext = TestContextProvider()
    private lateinit var contentDao: ImageDao

    @Before
    fun setup() {
        hiltRule.inject()
        contentDao = db.getContentDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertContentAndGetAllContent() {
        val contentList = listOf(
            ImageEntity(breed = "bulldog", subBreed = "french", contentURL = "https://images.dog.ceo/breeds/keeshond/n02112350_9643.jpg"),
            ImageEntity(breed = "hound", subBreed = "afghan", contentURL = "https://images.dog.ceo/breeds/hound-afghan/n02088094_6430.jpg"),
        )

        runBlockingTest {
            contentDao.insertImage(contentList)
        }

        val fetchedList = contentDao.getAllContents(breed = "bulldog", subBread = "french")

        assertThat(fetchedList[0].breed).isEqualTo(contentList[0].breed)
        assertThat(fetchedList[0].subBreed).isEqualTo(contentList[0].subBreed)
        assertThat(fetchedList[0].contentURL).isEqualTo(contentList[0].contentURL)

        assertThat(fetchedList[0].breed).isNotEqualTo(contentList[1].breed)
        assertThat(fetchedList[0].subBreed).isNotEqualTo(contentList[1].subBreed)
        assertThat(fetchedList[0].contentURL).isNotEqualTo(contentList[1].contentURL)

        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()
    }
}