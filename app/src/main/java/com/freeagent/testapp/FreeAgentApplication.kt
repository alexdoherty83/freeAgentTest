package com.freeagent.testapp

import android.app.Application
import com.freeagent.testapp.data.network.FixerIoRetrofitApi
import com.freeagent.testapp.data.repo.FxRepo

open class FreeAgentApplication : Application() {

    open var mFixerApiClient: FixerIoRetrofitApi? = null
    open var mFxRepo: FxRepo? = null

    override fun onCreate() {
        super.onCreate()

        try {
            mFixerApiClient =
                FixerIoRetrofitApi(API_KEY = BuildConfig.API_KEY, BASE_URL = "https://api.apilayer.com")
            mFixerApiClient?.setup(this)
            mFxRepo = FxRepo(mFixerApiClient = mFixerApiClient)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


}