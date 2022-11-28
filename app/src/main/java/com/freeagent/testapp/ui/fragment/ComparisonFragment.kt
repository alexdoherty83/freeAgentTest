package com.freeagent.testapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.freeagent.testapp.R
import com.freeagent.testapp.data.model.ComparisonModel
import com.freeagent.testapp.databinding.FragmentComparisonBinding
import com.freeagent.testapp.ui.adapter.ComparisonListAdapter
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

open class ComparisonFragment(
    protected open val mAmount: Double,
    protected open val mCurrency: String,
    protected open val mSelectedCurrencies: List<String>
) : BaseFragment() {

    protected open var mComparisonModel: ComparisonModel? = null
    protected open val binding by lazy { FragmentComparisonBinding.inflate(layoutInflater) }

    protected open val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    protected open var mAdapter: ComparisonListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            if (savedInstanceState == null) {
                setupAmount()
                setupAdapter()
                setupRecyclerView()
                setupHeader()
                setupComparison()
            }
        } catch (e: Throwable) {
            e.printStackTrace()

        }

    }

    @SuppressLint("SetTextI18n")
    protected open fun setupAmount(): Throwable? {
        try {
            binding.comparisonAmount.text = "$mAmount $mCurrency"
        } catch (e: Throwable) {
            e.printStackTrace()
            return e
        }
        return null
    }

    protected open fun setupAdapter(): Throwable? {

        try {
            mAdapter = ComparisonListAdapter()
            mAdapter?.mAmountToFx = mAmount
            mAdapter?.mRowColour = ResourcesCompat.getColor(resources, R.color.comparison_row, null)
        } catch (e: Throwable) {
            e.printStackTrace()
            return e
        }
        return null

    }

    protected open fun setupRecyclerView(): Throwable? {

        try {
            binding.comparisonRecyclerView.apply {
                layoutManager = makeLayoutManager()
                adapter = mAdapter
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return e
        }
        return null

    }

    protected open fun setupHeader(): Throwable? {

        try {
            if (mSelectedCurrencies.size > 1) {
                binding.comparisonCurrenyOneFilter.text = mSelectedCurrencies[0]
                binding.comparisonCurrenyTwoFilter.text = mSelectedCurrencies[1]
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return e
        }
        return null

    }

    protected open fun setupComparison(): Throwable? {
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
            return e
        }
        return null
    }

    @SuppressLint("NotifyDataSetChanged")
    protected open fun obtainedComparison(comparisonModel: ComparisonModel?): Throwable? {
        try {
            hideLoading()
            mComparisonModel = comparisonModel
            mAdapter?.mList = mComparisonModel?.rates
            binding.comparisonRecyclerView.post {

                try {
                    mAdapter?.notifyDataSetChanged()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

        } catch (e: Throwable) {
            e.printStackTrace()
            return e
        }
        return null
    }

    override fun showLoading(): Throwable? {

        try {
            mSnackbar =
                Snackbar.make(binding.root, R.string.loading_comparison, Snackbar.LENGTH_INDEFINITE)
            mSnackbar?.show()
        } catch (e: Throwable) {
            e.printStackTrace()
            return e
        }
        return null
    }

    override fun hideLoading(): Throwable? {
        try {
            mSnackbar?.dismiss()
        } catch (e: Throwable) {
            e.printStackTrace()
            return e
        }
        return null
    }
}