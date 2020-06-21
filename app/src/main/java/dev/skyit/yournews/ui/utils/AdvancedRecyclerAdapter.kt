package dev.skyit.yournews.ui.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.lang.IllegalArgumentException

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}


abstract class ElementHolderFactory<T>() {
    abstract fun createHolder(inflater: LayoutInflater, viewType: Int = 0) : BaseViewHolder<T>
}


abstract class AdvancedRecyclerAdapter<T>(
    private val items: ArrayList<T> = arrayListOf(),
    private val elementHolderFactory: ElementHolderFactory<T>
) : RecyclerView.Adapter<BaseViewHolder<T>>() {


    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        return elementHolderFactory.createHolder(inflater, viewType)

    }

    protected abstract fun extractId(item: T) : Any

    private inner class Differ(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return extractId(oldList[oldItemPosition]) == extractId(newList[newItemPosition])
        }

        override fun getOldListSize(): Int  = oldList.count()

        override fun getNewListSize(): Int = newList.count()

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
        }

    }


    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return getItemViewType(position, item)
    }

    open fun getItemViewType(position: Int, element: T) : Int = 0

    fun updateData(newList: List<T>) {
        val diff = Differ(items, newList)
        val diffResult = DiffUtil.calculateDiff(diff)
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }




}