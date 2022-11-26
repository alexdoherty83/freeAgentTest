package com.freeagent.testapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freeagent.testapp.databinding.ItemViewComparisonBinding
import com.google.gson.internal.LinkedTreeMap
import java.util.*

open class ComparisonListAdapter :
    RecyclerView.Adapter<ComparisonListAdapter.ComparisonViewHolder>() {

    open var mAmountToFx: Double? = null

    open var mList: TreeMap<String, LinkedTreeMap<String, String>>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComparisonViewHolder {

        val holder = ComparisonViewHolder(
            ItemViewComparisonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        return holder

    }

    override fun onBindViewHolder(holder: ComparisonViewHolder, position: Int) {

        try {
            val item = mList?.entries?.elementAt(position)
            holder.bindViewHolder(
                item,
                (mAmountToFx ?: 0).toDouble())

            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.WHITE)
            }
            else {
                holder.itemView.setBackgroundColor(Color.LTGRAY)
            }

        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    open class ComparisonViewHolder(open val binding: ItemViewComparisonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        open fun bindViewHolder(
            item: MutableMap.MutableEntry<String, LinkedTreeMap<String, String>>?,
            toDouble: Double
        ) {

            try {
                binding.comparisonDate.text = item?.key
                binding.comparisonValueOne.text = ((item?.value?.entries?.elementAt(0)?.value?.toDouble() ?: 0).toDouble() * toDouble).toString()
                binding.comparisonValueTwo.text = ((item?.value?.entries?.elementAt(1)?.value?.toDouble() ?: 0).toDouble() * toDouble).toString()
            } catch (e: Throwable) {
                e.printStackTrace()
            }

        }
    }

}