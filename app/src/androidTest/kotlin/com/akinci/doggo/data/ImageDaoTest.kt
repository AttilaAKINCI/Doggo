package com.akinci.doggo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.akinci.doggo.core.storage.AppDatabase
import com.akinci.doggo.data.image.local.ImageDao
import com.akinci.doggo.data.image.local.ImageEntity
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
class ImageDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: AppDatabase

    private lateinit var imageDao: ImageDao

    @Before
    fun setup() {
        hiltRule.inject()
        imageDao = db.getImageDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getImagesFromRoomDbByBreedAndSubBreed() = runTest {
        val contentList = listOf(
            ImageEntity(
                breed = "bulldog",
                subBreed = "french",
                imageUrl = "https://images.dog.ceo/breeds/keeshond/n02112350_9643.jpg"
            ),
            ImageEntity(
                breed = "hound",
                subBreed = "afghan",
                imageUrl = "https://images.dog.ceo/breeds/hound-afghan/n02088094_6430.jpg"
            ),
        )

        imageDao.insertImages(contentList)

        val result = imageDao.getImages(breed = "bulldog", subBreed = "french")
        result.size shouldBe 1
        result[0].imageUrl shouldBe "https://images.dog.ceo/breeds/keeshond/n02112350_9643.jpg"

        advanceUntilIdle()
    }

    @Test
    fun getImageFromRoomDBByBreed() = runTest {
        val contentList = listOf(
            ImageEntity(
                breed = "bulldog",
                subBreed = "french",
                imageUrl = "https://images.dog.ceo/breeds/keeshond/n02112350_9643.jpg"
            ),
            ImageEntity(
                breed = "bulldog",
                subBreed = "afghan",
                imageUrl = "https://images.dog.ceo/breeds/hound-afghan/n02088094_6430.jpg"
            ),
        )

        imageDao.insertImages(contentList)

        val result = imageDao.getImages(breed = "bulldog")
        result.size shouldBe 2
        result[0].imageUrl shouldBe "https://images.dog.ceo/breeds/keeshond/n02112350_9643.jpg"
        result[1].imageUrl shouldBe "https://images.dog.ceo/breeds/hound-afghan/n02088094_6430.jpg"

        advanceUntilIdle()
    }
}
