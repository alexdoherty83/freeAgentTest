package com.freeagent.testapp

import com.freeagent.testapp.data.model.ComparisonModel
import com.freeagent.testapp.data.model.FxModel
import com.google.gson.Gson
import org.junit.Test

class FeedValidatorTest {

    @Test fun feedValidator_fxModel_validResponse() {

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

        val response = Gson().fromJson(modelData, FxModel::class.java)

        assert(response != null)

        assert(response.success == true)

        assert(response.rates != null)

        assert(response.rates?.isNotEmpty() == true)

        assert(response.rates?.size == 3)

    }

    @Test fun feedValidator_comparisonModel_validResponse() {

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
        val response = Gson().fromJson(modelData, ComparisonModel::class.java)
        assert(response != null)

        assert(response.success == true)

        assert(response.rates != null)

        assert(response.rates?.isNotEmpty() == true)

        assert(response.rates?.size == 3)

        assert(response.rates?.entries?.elementAt(0)?.key == "2012-05-01")

        assert(response.rates?.entries?.elementAt(0)?.value != null)

        assert(response.rates?.entries?.elementAt(0)?.value?.isNotEmpty() != null)

        assert(response.rates?.entries?.elementAt(0)?.value?.size == 3)

    }

}