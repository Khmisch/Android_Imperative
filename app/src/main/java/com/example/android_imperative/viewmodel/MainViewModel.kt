package com.example.android_imperative.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_imperative.model.TVShow
import com.example.android_imperative.model.TVShowDetails
import com.example.android_imperative.model.TVShowPopular
import com.example.android_imperative.repository.TVShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val tvShowRepository: TVShowRepository): ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val tvShowFromApi = MutableLiveData<ArrayList<TVShow>>()
    val tvShowFromDB = MutableLiveData<List<TVShow>>()

    val tvShowPopular = MutableLiveData<TVShowPopular>()
    val tvShowDetails = MutableLiveData<TVShowDetails>()

    /**
     * Retrofit Related
     */
    fun apiTVShowPopular(page: Int){
        isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val response = tvShowRepository.apiTVShowPopular(page)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    val resp = response.body()
                    tvShowPopular.postValue(resp)

                    tvShowFromApi.postValue(resp!!.tv_shows)
                    isLoading.value = false
                }else{
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }


    /**
     * Room Related
     */

    fun insertTVSowToDB(tvShow: TVShow){
        viewModelScope.launch {
            tvShowRepository.insertTVShowsToDB(tvShow)
        }
    }

}