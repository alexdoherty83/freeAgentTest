package com.freeagent.testapp.data.repo

import com.freeagent.testapp.data.model.FxModel
import com.freeagent.testapp.data.model.SymbolsModel
import com.freeagent.testapp.data.network.FixerIoRetrofitApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class FxRepo(protected open var mFixerApiClient: FixerIoRetrofitApi? = null) {

    open suspend fun getLatestFxRates(defaultCurrency: String): FxModel? {

        try {
            return withContext(Dispatchers.IO) {
                mFixerApiClient?.getLatestFxRates(defaultCurrency)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null

    }

    open suspend fun getSymbols(): SymbolsModel? {

        try {
            return withContext(Dispatchers.IO) {
                mFixerApiClient?.getSymbols()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null

    }

}
