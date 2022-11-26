package com.freeagent.testapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freeagent.testapp.databinding.ItemViewComparisonBinding
import com.google.gson.internal.LinkedTreeMap
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

open class ComparisonListAdapter :
    RecyclerView.Adapter<ComparisonListAdapter.ComparisonViewHolder>() {

    open var mAmountToFx: Double? = null

    open var mList: TreeMap<String, LinkedTreeMap<String, String>>? = null

    open var mRowColour: Int = 0

    val mDecimalFormat = DecimalFormat("#.##")

    init {
        mDecimalFormat.roundingMode = RoundingMode.CEILING
    }

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
                (mAmountToFx ?: 0).toDouble(),
                mDecimalFormat
            )

            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.WHITE)
            } else {
                holder.itemView.setBackgroundColor(mRowColour)
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
            toDouble: Double,
            mDecimalFormat: DecimalFormat
        ) {

            try {
                binding.comparisonDate.text = item?.key
                binding.comparisonValueOne.text =
                    mDecimalFormat.format(
                        (item?.value?.entries?.elementAt(0)?.value?.toDouble()
                            ?: 0).toDouble() * toDouble
                    ).toString()
                binding.comparisonValueTwo.text =
                    mDecimalFormat.format(
                        (item?.value?.entries?.elementAt(1)?.value?.toDouble()
                            ?: 0).toDouble() * toDouble
                    ).toString()
            } catch (e: Throwable) {
                e.printStackTrace()
            }

        }
    }

}