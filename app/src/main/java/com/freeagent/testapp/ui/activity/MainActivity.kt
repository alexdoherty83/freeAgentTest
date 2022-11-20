package com.freeagent.testapp.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.freeagent.testapp.FreeAgentApplication
import com.freeagent.testapp.R
import com.freeagent.testapp.databinding.ActivityMainBinding
import com.freeagent.testapp.ui.fragment.RatesListFragment
import com.freeagent.testapp.ui.viewmodel.FxViewModel
import com.freeagent.testapp.ui.viewmodel.FxViewModelFactory

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    //val fxViewModel = ViewModelProvider(this, FxViewModelFactory( )).get(FxViewModel::class.java) //by viewModels<FxViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {

            val app = application as FreeAgentApplication
            val factory = FxViewModelFactory(app.mFxRepo, app)
            val fxViewModel = ViewModelProvider(this, factory).get(FxViewModel::class.java)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<RatesListFragment>(R.id.fragment_container_view)
            }
        }
    }
}