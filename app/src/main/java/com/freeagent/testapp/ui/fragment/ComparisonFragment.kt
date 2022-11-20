package com.freeagent.testapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.freeagent.testapp.databinding.FragmentComparisonBinding
import com.freeagent.testapp.ui.viewmodel.FxViewModel

open class ComparisonFragment: Fragment() {

    protected open val binding by lazy { FragmentComparisonBinding.inflate(layoutInflater) }
    protected open val fxViewModel by activityViewModels<FxViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

}