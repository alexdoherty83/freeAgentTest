package com.freeagent.testapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freeagent.testapp.data.model.FxModel
import com.freeagent.testapp.ui.viewmodel.FxViewModel
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    protected open var mFxModel: FxModel? = null
    protected open val fxViewModel by activityViewModels<FxViewModel>()

    protected open var mSnackbar: Snackbar? = null

    companion object {

        const val INITED = "inited"

    }

    protected open fun makeLayoutManager(): RecyclerView.LayoutManager? {
        // this would be a good time to think about tablet layouts...
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(INITED, true)
        super.onSaveInstanceState(outState)
    }

    abstract fun showLoading()

    abstract fun hideLoading()

}