package com.freeagent.testapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.freeagent.testapp.FreeAgentApplication
import com.freeagent.testapp.data.model.ComparisonModel
import com.freeagent.testapp.data.model.FxModel
import com.freeagent.testapp.data.model.SymbolsModel
import com.freeagent.testapp.data.repo.FxRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class FxViewModel(
    protected open val fxRepo: FxRepo?,
    application: Application
) : AndroidViewModel(application) {

    open var mFxModel: FxModel? = null
    open var mSymbolsModel: SymbolsModel? = null

    fun getSymbols(
        success: ((model: SymbolsModel?) -> Unit)? = null,
        failure: (() -> Unit)? = null
    ) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    mSymbolsModel = fxRepo?.getSymbols()
                    when (mSymbolsModel) {
                        is SymbolsModel -> {
                            success?.invoke(mSymbolsModel)
                        }
                        else -> {
                            failure?.invoke()
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun getLatestFxRates(
        defaultCurrency: String,
        symbols: String,
        success: ((model: FxModel?) -> Unit)? = null,
        failure: (() -> Unit)? = null
    ) {

        try {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    mFxModel = fxRepo?.getLatestFxRates(defaultCurrency, symbols)
                    when (mFxModel) {
                        is FxModel -> {
                            success?.invoke(mFxModel)
                        }
                        else -> {
                            failure?.invoke()
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    fun getComparisonForRates(
        defaultCurrency: String,
        symbols: String,
        startDate: String,
        endDate: String,
        success: ((model: ComparisonModel?) -> Unit)? = null,
        failure: (() -> Unit)? = null
    ) {

        try {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val comparisonModel =
                        fxRepo?.getRatesOverTime(startDate, endDate, defaultCurrency, symbols)
                    when (comparisonModel) {
                        is ComparisonModel -> {
                            success?.invoke(comparisonModel)
                        }
                        else -> {
                            failure?.invoke()
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

}

open class FxViewModelFactory(
    protected open val repo: FxRepo?,
    protected open val app: FreeAgentApplication
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FxViewModel(repo, app) as T
    }

}