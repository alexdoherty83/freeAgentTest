package com.freeagent.testapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freeagent.testapp.R
import com.freeagent.testapp.data.model.FxModel
import com.freeagent.testapp.databinding.FragmentRatesListBinding
import com.freeagent.testapp.ui.adapter.RatesListAdapter
import com.freeagent.testapp.ui.viewmodel.FxViewModel
import com.freeagent.testapp.ui.widget.VerticalSpaceItemDecoration

open class RatesListFragment : Fragment() {

    protected open var mFxModel: FxModel? = null
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
            setupAmountInput()
            setupRecyclerView()
            setupViewModel()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun setupAmountInput() {
        binding.amountInputField.setOnEditorActionListener { textView, actionId, p2 ->
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
                fxViewModel.getLatestFxRates(defaultCurrency = currency, success = {
                    obtainedFxRates(it)
                }) {
                    failedToObtainModel()
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
                    selectedCurrencies?.contains(victim.key) == true && victim.key != defaultCurrency
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