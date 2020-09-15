package com.sunasterisk.thooi.ui.placespicker

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunasterisk.thooi.data.model.UserAddress
import com.sunasterisk.thooi.databinding.ItemAddressBinding
import com.sunasterisk.thooi.util.inflater

class AddressAdapter : ListAdapter<UserAddress, AddressAdapter.ViewHolder>(Companion) {

    private var onItemClick: ((UserAddress) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemAddressBinding.inflate(parent.inflater), onItemClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnclickListener(listener: (UserAddress) -> Unit) {
        onItemClick = listener
    }

    class ViewHolder(val binding: ItemAddressBinding, onItemClick: ((UserAddress) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {
        private var address: UserAddress? = null

        init {
            if (onItemClick != null) itemView.setOnClickListener { address?.let { onItemClick(it) } }
        }

        fun bind(item: UserAddress) {
            address = item
            binding.address = item
        }
    }

    companion object : DiffUtil.ItemCallback<UserAddress>() {
        override fun areItemsTheSame(oldItem: UserAddress, newItem: UserAddress) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserAddress, newItem: UserAddress) =
            oldItem == newItem
    }
}
