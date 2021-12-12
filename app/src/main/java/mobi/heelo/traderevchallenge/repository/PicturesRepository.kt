package mobi.heelo.traderevchallenge.repository

import mobi.heelo.traderevchallenge.networking.RetrofitInstance

class PicturesRepository {

    suspend fun getPictures(pageNumber: Int) =
        RetrofitInstance.api.getPictures(pageNumber)

}