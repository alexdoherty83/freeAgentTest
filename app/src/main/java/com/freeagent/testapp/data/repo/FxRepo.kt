package com.freeagent.testapp.data.repo

import com.freeagent.testapp.data.model.ComparisonModel
import com.freeagent.testapp.data.model.FxModel
import com.freeagent.testapp.data.model.SymbolsModel
import com.freeagent.testapp.data.network.FixerIoRetrofitApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class FxRepo(protected open var mFixerApiClient: FixerIoRetrofitApi? = null) {

    open suspend fun getLatestFxRates(defaultCurrency: String, symbols: String): FxModel? {

        try {
            // away from the UI thread if you please
            return withContext(Dispatchers.IO) {
                mFixerApiClient?.getLatestFxRates(defaultCurrency, symbols)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null

    }

    open suspend fun getRatesOverTime(
        startDate: String,
        endDate: String,
        defaultCurrency: String,
        symbols: String
    ): ComparisonModel? {

        try {
            // away from the UI thread if you please
            return withContext(Dispatchers.IO) {
                mFixerApiClient?.getRatesOverTime(startDate, endDate, defaultCurrency, symbols)
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
