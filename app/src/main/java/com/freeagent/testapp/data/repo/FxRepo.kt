package com.freeagent.testapp.data.repo

import com.freeagent.testapp.data.model.ComparisonModel
import com.freeagent.testapp.data.model.FxModel
import com.freeagent.testapp.data.model.SymbolsModel
import com.freeagent.testapp.data.network.FixerIoRetrofitApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class FxRepo(protected open var mFixerApiClient: FixerIoRetrofitApi? = null) {

    protected open var mFxModel: FxModel? = null
    protected open var mComparisonModel: ComparisonModel? = null

    open suspend fun loadDummyFxModel(): FxModel? {

        mFxModel = withContext(Dispatchers.IO) {
            val modelData = "{\n" +
                    "  \"base\": \"USD\",\n" +
                    "  \"date\": \"2022-04-14\",\n" +
                    "  \"rates\": {\n" +
                    "    \"EUR\": 0.813399,\n" +
                    "    \"GBP\": 0.72007,\n" +
                    "    \"JPY\": 107.346001\n" +
                    "  },\n" +
                    "  \"success\": true,\n" +
                    "  \"timestamp\": 1519296206\n" +
                    "}"
            Gson().fromJson(modelData, FxModel::class.java)
        }
        return mFxModel
    }

    open suspend fun loadDummyComparisonModel(): ComparisonModel? {

        mComparisonModel = withContext(Dispatchers.IO) {
            val modelData = "{\n" +
                    "  \"base\": \"EUR\",\n" +
                    "  \"end_date\": \"2012-05-03\",\n" +
                    "  \"rates\": {\n" +
                    "    \"2012-05-01\": {\n" +
                    "      \"AUD\": 1.278047,\n" +
                    "      \"CAD\": 1.302303,\n" +
                    "      \"USD\": 1.322891\n" +
                    "    },\n" +
                    "    \"2012-05-02\": {\n" +
                    "      \"AUD\": 1.274202,\n" +
                    "      \"CAD\": 1.299083,\n" +
                    "      \"USD\": 1.315066\n" +
                    "    },\n" +
                    "    \"2012-05-03\": {\n" +
                    "      \"AUD\": 1.280135,\n" +
                    "      \"CAD\": 1.296868,\n" +
                    "      \"USD\": 1.314491\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"start_date\": \"2012-05-01\",\n" +
                    "  \"success\": true,\n" +
                    "  \"timeseries\": true\n" +
                    "}"
            Gson().fromJson(modelData, ComparisonModel::class.java)
        }
        return mComparisonModel
    }

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
                mFixerApiClient?.getRatesOverTime(startDate = startDate, endDate = endDate, defaultCurrency, symbols)
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
