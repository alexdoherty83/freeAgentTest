package com.freeagent.testapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.freeagent.testapp.FreeAgentApplication
import com.freeagent.testapp.R
import com.freeagent.testapp.data.model.ComparisonModel
import com.freeagent.testapp.databinding.ActivityMainBinding
import com.freeagent.testapp.ui.fragment.ComparisonFragment
import com.freeagent.testapp.ui.fragment.RatesListFragment
import com.freeagent.testapp.ui.viewmodel.FxViewModel
import com.freeagent.testapp.ui.viewmodel.FxViewModelFactory
import com.google.gson.Gson

open class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    //val fxViewModel = ViewModelProvider(this, FxViewModelFactory( )).get(FxViewModel::class.java) //by viewModels<FxViewModel>()

    protected open var mRatesListFragment: RatesListFragment? = null
    protected open var mComparisonFragment: ComparisonFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        try {
            if (savedInstanceState == null) {

                val app = application as FreeAgentApplication
                val factory = FxViewModelFactory(app.mFxRepo, app)
                val fxViewModel = ViewModelProvider(this, factory).get(FxViewModel::class.java)

                mRatesListFragment = RatesListFragment()
                mRatesListFragment?.mComparisonDelegate = { amount, baseCurrency, list ->
                    loadComparison(amount, baseCurrency, list)
                }
                mRatesListFragment?.let {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        addToBackStack(RatesListFragment::class.java.simpleName)
                        replace(R.id.fragment_container_view, it)
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun loadComparison(
        initialAmount: Double,
        currency: String,
        list: List<String>?
    ) {
        try {
            if (list != null) {
                mComparisonFragment = ComparisonFragment(initialAmount, currency, list)
                mComparisonFragment?.let {

                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        addToBackStack(ComparisonFragment::class.java.simpleName)
                        replace(R.id.fragment_container_view, it)
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}