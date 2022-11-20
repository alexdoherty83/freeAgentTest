package com.freeagent.testapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freeagent.testapp.R
import com.freeagent.testapp.data.model.FxModel
import com.freeagent.testapp.data.model.SymbolsModel
import com.freeagent.testapp.databinding.FragmentRatesListBinding
import com.freeagent.testapp.ui.adapter.RatesListAdapter
import com.freeagent.testapp.ui.viewmodel.FxViewModel
import com.freeagent.testapp.ui.widget.VerticalSpaceItemDecoration
import java.util.*
import kotlin.collections.ArrayList

open class RatesListFragment : Fragment() {

    protected open val binding by lazy { FragmentRatesListBinding.inflate(layoutInflater) }
    protected open var mRatesListAdapter: RatesListAdapter? = null
    protected open val fxViewModel by activityViewModels<FxViewModel>()
    protected open var defaultCurrency: String? = null
    protected open var selectedCurrencies: Array<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            loadDefaults()
            setupAdapter()
            setupSpinner()
            setupRecyclerView()
            setupViewModel()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun setupSpinner() {
        try {
            selectedCurrencies?.let {
                val sortedList = it.sortedArray()
                val spinnerAdapter = context?.let { notNullContext ->
                    ArrayAdapter(
                        notNullContext,
                        android.R.layout.simple_spinner_item,
                        sortedList
                    )
                }
                binding.selectedCurrencyLabel.post {
                    binding.selectedCurrencyLabel.adapter = spinnerAdapter
                    binding.selectedCurrencyLabel.setSelection(sortedList.indexOf(defaultCurrency))
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun loadDefaults() {
        try {
            defaultCurrency = resources.getString(R.string.fx_default_currency)
            selectedCurrencies = resources.getStringArray(R.array.fx_selectedCurrencies)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun setupViewModel() {

        try {
            showLoading()
            obtainLatestRates()
            /*fxViewModel.getSymbols(success = {
                obtainedSymbols(it)
            }, failure = {
                failedToObtainSymbols()
            })*/

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun obtainLatestRates() {
        defaultCurrency?.let { currency ->
            fxViewModel.getLatestFxRates(defaultCurrency = currency, success = {
                obtainedFxRates(it)
            }) {
                failedToObtainModel()
            }
        }
    }

    protected open fun obtainedSymbols(symbolsModel: SymbolsModel?) {
        try {

            if (symbolsModel != null) {
                if (symbolsModel.symbols != null && symbolsModel.symbols.size > 0) {

                    val from = ArrayList<String>(symbolsModel.symbols.keys)
                    val spinnerAdapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_item,
                            from
                        )
                    }
                    binding.selectedCurrencyLabel.post {
                        binding.selectedCurrencyLabel.adapter = spinnerAdapter
                        binding.selectedCurrencyLabel.setSelection(from.indexOf(defaultCurrency))
                    }
                }
            }

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun failedToObtainSymbols() {
        try {
            hideLoading()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    protected open fun obtainedFxRates(model: FxModel?) {
        try {

            mRatesListAdapter?.mList = model?.rates?.filter { victim ->
                selectedCurrencies?.contains(victim.key) == true && victim.key != defaultCurrency
            }
            binding.ratesListRecycler.post {
                // yes google, i know it's better to notify one item
                // instead of all of them, but i'm changing
                // more than one item at this time
                mRatesListAdapter?.notifyDataSetChanged()
                hideLoading()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun failedToObtainModel() {
        try {
            hideLoading()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun setupAdapter() {
        mRatesListAdapter = RatesListAdapter()
    }

    protected open fun setupRecyclerView() {
        try {
            binding.ratesListRecycler.apply {
                layoutManager = makeLayoutManager()
                adapter = mRatesListAdapter
                addItemDecoration(
                    VerticalSpaceItemDecoration(
                        resources.getDimensionPixelSize(
                            R.dimen.padding
                        )
                    )
                )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun makeLayoutManager(): RecyclerView.LayoutManager? {
        // this would be a good time to think about tablet layouts...
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    protected open fun showLoading() {

    }

    protected open fun hideLoading() {

    }
}