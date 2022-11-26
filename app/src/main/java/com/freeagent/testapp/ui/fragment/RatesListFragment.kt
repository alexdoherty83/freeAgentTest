package com.freeagent.testapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.freeagent.testapp.R
import com.freeagent.testapp.data.model.FxModel
import com.freeagent.testapp.databinding.FragmentRatesListBinding
import com.freeagent.testapp.ui.adapter.RatesListAdapter
import com.freeagent.testapp.ui.widget.VerticalSpaceItemDecoration

open class RatesListFragment : BaseFragment() {

    open var mComparisonDelegate: ((Double, String, List<String>?) -> Unit)? = null

    protected open val binding by lazy { FragmentRatesListBinding.inflate(layoutInflater) }
    protected open var mRatesListAdapter: RatesListAdapter? = null
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
            setupAmountInput()
            setupCompareButton()
            setupRecyclerView()
            setupViewModel()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    protected open fun setupCompareButton() {
        try {
            binding.toggleCompareButton.setOnClickListener {
                try {
                    mRatesListAdapter?.isCompare = !mRatesListAdapter?.isCompare!!

                    binding.ratesListRecycler.post {
                        try {
                            mRatesListAdapter?.notifyDataSetChanged()
                        } catch (e: Throwable) {
                            e.printStackTrace()
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

    protected open fun setupAmountInput() {
        try {
            binding.amountInputField.setOnEditorActionListener { textView, actionId, _ ->
                try {// make sure this matches what was set for imeAction in the layout!
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        if (!TextUtils.isEmpty(textView.text))
                            obtainLatestRates()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                false
            }
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
                    binding.selectedCurrencyLabel.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                position: Int,
                                p3: Long
                            ) {
                                try {
                                    defaultCurrency = spinnerAdapter?.getItem(position)
                                    obtainedFxRates(mFxModel)
                                } catch (e: Throwable) {
                                    e.printStackTrace()
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }
                        }
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
            //showLoading()
            //obtainLatestRates()

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun obtainLatestRates() {
        try {
            defaultCurrency?.let { currency ->

                selectedCurrencies?.let { currencies ->

                    val symbols = currencies.joinToString(separator = ",")

                    fxViewModel.getLatestFxRates(
                        defaultCurrency = currency,
                        symbols = symbols,
                        success = {
                            obtainedFxRates(it)
                        }) {
                        failedToObtainModel()
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    protected open fun obtainedFxRates(model: FxModel?) {
        try {

            hideLoading()

            mFxModel = model
            if (!TextUtils.isEmpty(binding.amountInputField.text)) {
                mRatesListAdapter?.mList = model?.rates?.filter { victim ->
                    /*selectedCurrencies?.contains(victim.key) == true &&*/ victim.key != defaultCurrency
                }
                mRatesListAdapter?.mAmountToFx = binding.amountInputField.text.toString().toDouble()
                binding.ratesListRecycler.post {
                    // yes google, i know it's better to notify one item
                    // instead of all of them, but i'm changing
                    // more than one item at this time
                    mRatesListAdapter?.notifyDataSetChanged()
                }
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
        try {
            mRatesListAdapter = RatesListAdapter()
            mRatesListAdapter?.mCheckChangedDelegate = {
                try {
                    val count = (mRatesListAdapter?.mCheckedItems?.size ?: 0)
                    if (count > 1) {
                        showComparison()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun showComparison() {
        try {
            if (mComparisonDelegate != null) {
                mRatesListAdapter?.mCheckedItems?.let { items ->
                    defaultCurrency?.let { currency ->
                        mComparisonDelegate?.invoke(
                            binding.amountInputField.text.toString().toDouble(),
                            currency, items
                        )
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
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

    override fun showLoading() {

    }

    override fun hideLoading() {

    }
}