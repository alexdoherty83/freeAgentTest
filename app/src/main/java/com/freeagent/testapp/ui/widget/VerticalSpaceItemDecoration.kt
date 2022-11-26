package com.freeagent.testapp.ui.widget

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {

        val itemCount = parent.adapter?.itemCount ?: 0

        if (itemCount != 0) {
            if (itemPosition != itemCount - 1) {
                outRect.bottom = verticalSpaceHeight
            }
        }

    }
}