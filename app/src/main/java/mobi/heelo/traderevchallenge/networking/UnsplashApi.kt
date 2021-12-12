package mobi.heelo.traderevchallenge.networking

import mobi.heelo.traderevchallenge.models.UnsplashResponse
import mobi.heelo.traderevchallenge.utilities.Constants.Companion.API_KEY
import mobi.heelo.traderevchallenge.utilities.Constants.Companion.QUERY_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos")
    suspend fun getPictures(
        @Query("page")
        pageNumber: Int = 1,
        @Query("per_page")
        perPage: Int = QUERY_PAGE_SIZE,
        @Query("client_id")
        client_id: String = API_KEY
    ): Response<UnsplashResponse>
}