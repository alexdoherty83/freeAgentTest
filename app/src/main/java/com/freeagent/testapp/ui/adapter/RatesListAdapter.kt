package com.freeagent.testapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freeagent.testapp.databinding.ItemViewCurrencyBinding

open class RatesListAdapter : RecyclerView.Adapter<RatesListAdapter.FxViewHolder>() {

    open var mAmountToFx: Double? = null
    open var mList: Map<String, String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FxViewHolder(
            ItemViewCurrencyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FxViewHolder, position: Int) {

        try {
            val keyValue = mList?.entries?.elementAt(position)
            holder.bindViewHolder(keyValue, (mAmountToFx ?: 0).toDouble())
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    open class FxViewHolder(protected open val binding: ItemViewCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        open fun bindViewHolder(keyValue: Map.Entry<String, String>?, amountToFx: Double) {
            try {
                binding.currencyNameLabel.text = keyValue?.key
                binding.currencyRateLabel.text = ((keyValue?.value?.toDouble() ?: 0).toDouble() * amountToFx).toString()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

    }
}