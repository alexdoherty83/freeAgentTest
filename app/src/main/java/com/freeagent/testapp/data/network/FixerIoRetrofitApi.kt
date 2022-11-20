package com.freeagent.testapp.data.network

import android.content.Context
import com.freeagent.testapp.data.model.FxModel
import com.freeagent.testapp.data.model.SymbolsModel
import com.freeagent.testapp.utils.HelpfulUtils
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


open class FixerIoRetrofitApi(
    protected open val API_KEY: String,
    protected open val BASE_URL: String
) {

    protected open var fixerIoService: FixerIoService? = null

    open fun setup(context: Context) {

        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient(context))
            .build()

        fixerIoService = builder.create(FixerIoService::class.java)

    }

    protected open fun getHttpClient(context: Context): OkHttpClient {

        return OkHttpClient.Builder()
            .cache(getCache(context))
            .addInterceptor { chain ->
                var request = chain.request()

                val cacheControl = CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(1, TimeUnit.DAYS)
                    .build()

                request = if (HelpfulUtils.hasNetwork(context) == true)
                    request.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 5)
                        //.cacheControl(cacheControl)
                        .build()
                else
                    request.newBuilder()
                        .header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                        )
                        //.cacheControl(cacheControl)
                        .build()
                chain.proceed(request)
            }
            .build()
    }

    protected open fun getCache(context: Context): Cache? {
        return Cache(context.cacheDir, (20 * 1024 * 1024).toLong())
    }

    open fun getLatestFxRates(defaultCurrency: String, symbols: String): FxModel? {
        try {
            val call = fixerIoService?.latestFxRates(API_KEY, defaultCurrency, symbols)
            val response = call?.execute()
            return response?.body()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    open fun getRatesOverTime(startDate: String, endDate: String, defaultCurrency: String, symbols: String): FxModel? {
        try {
            val call = fixerIoService?.compareRatesOverTime(API_KEY, startDate, endDate, defaultCurrency, symbols)
            val response = call?.execute()
            return response?.body()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    open fun getSymbols(): SymbolsModel? {
        try {
            val call = fixerIoService?.listAvailableSymbols(API_KEY)
            val response = call?.execute()
            return response?.body()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    interface FixerIoService {

        @GET("/fixer/symbols")
        fun listAvailableSymbols(@Header("apikey") apikey: String): Call<SymbolsModel>

        @GET("/fixer/latest")
        fun latestFxRates(
            @Header("apikey") apikey: String,
            @Query("base") defaultCurrency: String,
            @Query("symbols") symbols: String
        ): Call<FxModel>

        @GET("/fixer/timeseries")
        fun compareRatesOverTime(
            @Header("apikey") apikey: String,
            @Query("start_date") startDate: String,
            @Query("end_date") endDate: String,
            @Query("base") defaultCurrency: String,
            @Query("symbols") symbols: String
        ): Call<FxModel>


    }

}