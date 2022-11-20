package com.freeagent.testapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freeagent.testapp.R
import com.freeagent.testapp.databinding.ItemViewCurrencyBinding

open class RatesListAdapter : RecyclerView.Adapter<RatesListAdapter.FxViewHolder>() {

    open var mAmountToFx: Double? = null
    open var mList: Map<String, String>? = null
    open var isCompare = false

    open var mCheckChangedDelegate: (() -> Unit)? = null

    open var mCheckedItems = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FxViewHolder {

        val holder = FxViewHolder(
            ItemViewCurrencyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        holder.binding.currencyCompareToggle.setOnCheckedChangeListener { button, _ ->
            try {
                val tag = button.getTag(R.id.comparePosition) as String
                if (button.isChecked) {
                    mCheckedItems.add(tag)
                } else {
                    mCheckedItems.remove(tag)
                }
                mCheckChangedDelegate?.invoke()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: FxViewHolder, position: Int) {

        try {
            val keyValue = mList?.entries?.elementAt(position)
            val isAlreadyChecked = mCheckedItems.contains(keyValue?.key)
            holder.bindViewHolder(
                keyValue,
                (mAmountToFx ?: 0).toDouble(),
                isCompare,
                isAlreadyChecked
            )
            /*if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.WHITE)
            }
            else {
                holder.itemView.setBackgroundColor(Color.LTGRAY)
            }*/
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    open class FxViewHolder(open val binding: ItemViewCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        open fun bindViewHolder(
            keyValue: Map.Entry<String, String>?,
            amountToFx: Double,
            isCompare: Boolean,
            isAlreadyChecked: Boolean
        ) {
            try {
                binding.currencyNameLabel.text = keyValue?.key
                binding.currencyRateLabel.text =
                    ((keyValue?.value?.toDouble() ?: 0).toDouble() * amountToFx).toString()
                if (isCompare) {
                    binding.currencyCompareToggle.visibility = View.VISIBLE
                    binding.currencyCompareToggle.setTag(R.id.comparePosition, keyValue?.key)

                    binding.currencyCompareToggle.isChecked = isAlreadyChecked

                } else {
                    binding.currencyCompareToggle.visibility = View.GONE
                }

            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

    }
}