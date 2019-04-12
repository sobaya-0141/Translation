package sobaya.app.translation.repository

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationApi {

    @GET("exec")
    fun translation(@Query("text") text: String): Deferred<String>
}