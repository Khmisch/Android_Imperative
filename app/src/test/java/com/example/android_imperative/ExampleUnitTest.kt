package com.example.android_imperative

import com.example.android_imperative.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun checkStatusCode() = runTest{
        val responsePopular = AppModule().tvShowService().apiTVShowPopular(1)
        val responseDetails = AppModule().tvShowService().apiTVShowDetails(1)

        assertEquals(responsePopular.code(), 200)
        assertEquals(responseDetails.code(), 200)
    }

    @Test
    fun responseIsSuccessful() = runTest{
        val responsePopular = AppModule().tvShowService().apiTVShowPopular(1)
        val responseDetails = AppModule().tvShowService().apiTVShowDetails(1)
        assertTrue(responsePopular.isSuccessful)
        assertTrue(responseDetails.isSuccessful)
    }

    @Test
    fun checkTVShowListNotNull() = runTest{
        val responsePopular = AppModule().tvShowService().apiTVShowPopular(1)
        val responseDetails = AppModule().tvShowService().apiTVShowDetails(1)
        assertNotNull(responsePopular.body())
        assertNotNull(responseDetails.body())
        assertNotNull(responsePopular.body()!!.tv_shows)
        assertNotNull(responseDetails.body()!!.tvShow)
    }

    @Test
    fun checkTVShowListSize() = runTest{
        val responsePopular = AppModule().tvShowService().apiTVShowPopular(1)
        val tvShowPopular = responsePopular.body()
        assertEquals(tvShowPopular!!.tv_shows.size, 20)
    }

    @Test
    fun checkFirstTVShowStatus() = runTest{
        val responsePopular = AppModule().tvShowService().apiTVShowPopular(1)
        val tvShowPopular = responsePopular.body()
        val tvShows = tvShowPopular!!.tv_shows
        val tvShow = tvShows[0]
        assertEquals(tvShow.status, "Running")
    }
}