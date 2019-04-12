package sobaya.app.translation.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import sobaya.app.translation.repository.TranslationApi
import sobaya.app.translation.repository.TranslationRepository
import java.util.*

val module = module {
    single { createMoshi() }
    single { createOkHttp() }
    single { createRetrofit(get(), get()) }
    single<TranslationApi> { createApi(get()) }
    single { TranslationRepository(get()) }
}

fun createMoshi() = Moshi.Builder()
    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
    .add(KotlinJsonAdapterFactory())
    .build()


fun createOkHttp() = OkHttpClient.Builder().build()

fun createRetrofit(okHttpClient: OkHttpClient, moshi: Moshi) = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl("https://script.google.com/macros/s/AKfycbzIQfppyIcgYlfb5LcjXIaxJ5gLoLYXL3VpsBpHMBTMtwqXvEk/")
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

fun createApi(retrofit: Retrofit) = retrofit.create(TranslationApi::class.java)