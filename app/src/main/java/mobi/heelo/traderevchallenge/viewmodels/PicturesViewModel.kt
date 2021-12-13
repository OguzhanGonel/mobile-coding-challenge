package mobi.heelo.traderevchallenge.viewmodels

import android.app.Application

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mobi.heelo.traderevchallenge.app.PicturesApplication
import mobi.heelo.traderevchallenge.models.UnsplashResponse
import mobi.heelo.traderevchallenge.models.UnsplashResponseItem
import mobi.heelo.traderevchallenge.repository.PicturesRepository
import mobi.heelo.traderevchallenge.utilities.Resource
import okio.IOException
import retrofit2.Response

class PicturesViewModel(
    app: Application,
    val picturesRepository: PicturesRepository
) : AndroidViewModel(app) {

    //  the UnsplashResponse is wrapped with the custom Resource class I made for api responses
    val picturesLiveData: MutableLiveData<Resource<UnsplashResponse>> = MutableLiveData()
    var apiPageNumber = 1
    var unsplashResponse: UnsplashResponse? = null
    var currentDetailImagePosition: Int = 0
    var shouldScrollRecyclerView: Boolean = false

    lateinit var picturesArray: ArrayList<UnsplashResponseItem>

    init {
        getPictures()
    }

    fun getPictures() = viewModelScope.launch {
        safeUnsplashApiCall()
    }

    private fun handleUnsplashApiResponse(response: Response<UnsplashResponse>):
            Resource<UnsplashResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                apiPageNumber++
                if (unsplashResponse == null) {
                    unsplashResponse = resultResponse
                } else {
                    val oldImages = unsplashResponse
                    val newImages = resultResponse
                    oldImages?.addAll(newImages)
                }
                picturesArray = unsplashResponse ?: resultResponse
                return Resource.Success(unsplashResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }


    private suspend fun safeUnsplashApiCall() {
        picturesLiveData.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = picturesRepository.getPictures(apiPageNumber)
                picturesLiveData.postValue(handleUnsplashApiResponse(response))
            } else {
                picturesLiveData.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    picturesLiveData.postValue(Resource.Error("Sorry, network failure"))
                }
                else -> {
                    picturesLiveData.postValue(Resource.Error("Sorry, something went wrong"))
                }
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<PicturesApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            //  This looks like the below is deprecated but it will run fine for phones running Lollipop or lower
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}