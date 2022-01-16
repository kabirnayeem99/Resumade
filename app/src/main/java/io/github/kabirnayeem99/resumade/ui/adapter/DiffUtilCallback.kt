package io.github.kabirnayeem99.resumade.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import io.github.kabirnayeem99.resumade.data.database.ResumeEntity

class DiffUtilCallback<T : ResumeEntity>(
    private val oldList: List<T>,
    private val newList: List<T>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}