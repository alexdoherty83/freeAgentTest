package com.freeagent.testapp.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freeagent.testapp.data.model.FxModel
import com.freeagent.testapp.ui.viewmodel.FxViewModel

abstract class BaseFragment : Fragment() {

    protected open var mFxModel: FxModel? = null
    protected open val fxViewModel by activityViewModels<FxViewModel>()

    protected open fun makeLayoutManager(): RecyclerView.LayoutManager? {
        // this would be a good time to think about tablet layouts...
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    abstract fun showLoading()

    abstract fun hideLoading()

}