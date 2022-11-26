package com.freeagent.testapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.freeagent.testapp.data.model.ComparisonModel
import com.freeagent.testapp.databinding.FragmentComparisonBinding
import java.text.SimpleDateFormat
import java.util.*

open class ComparisonFragment(
    protected open val mAmount: Double,
    protected open val mCurrency: String,
    protected open val mSelectedCurrencies: List<String>
) : BaseFragment() {

    protected open var mComparisonModel: ComparisonModel? = null
    protected open val binding by lazy { FragmentComparisonBinding.inflate(layoutInflater) }

    protected open val dateFormatter = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            setupAmount()
            setupComparison()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun setupComparison() {
        try {
            showLoading()
            val calendar = Calendar.getInstance()

            val today = dateFormatter.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, -5)
            val fiveDaysAgo = dateFormatter.format(calendar.time)
            fxViewModel.getComparisonForRates(
                defaultCurrency = mCurrency,
                success = {
                    obtainedComparison(it)
                },
                failure = {
                    hideLoading()
                },
                startDate = fiveDaysAgo,
                endDate = today,
                symbols = mSelectedCurrencies.joinToString(separator = ",")
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun obtainedComparison(comparisonModel: ComparisonModel?) {
        try {
            mComparisonModel = comparisonModel

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    protected open fun setupAmount() {
        try {
            binding.comparisonAmount.text = "$mAmount $mCurrency"
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }
}