package com.example.financyq.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.financyq.R
import com.example.financyq.data.response.EduFinanceResponse
import com.example.financyq.databinding.ItemListHomeBinding

class ShortcutAdapter : ListAdapter<EduFinanceResponse, ShortcutAdapter.EduFinanceViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EduFinanceViewHolder {
        val binding = ItemListHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EduFinanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EduFinanceViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        Log.e("fikry3", "onBindViewHolder: ${item.imageUrl}")
    }

    class EduFinanceViewHolder(private val binding: ItemListHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EduFinanceResponse) {
            binding.tvDetails.setText(R.string.see_details)
            Glide.with(binding.root)
                .load(item.imageUrl)
                .into(binding.imgEducation)
                .clearOnDetach()
            Log.e("fikry4", "bind: ${item.imageUrl}")
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EduFinanceResponse>() {
            override fun areItemsTheSame(oldItem: EduFinanceResponse, newItem: EduFinanceResponse): Boolean {
                return oldItem.id == newItem.id // Assuming EduFinanceResponse has a unique ID field
            }

            override fun areContentsTheSame(oldItem: EduFinanceResponse, newItem: EduFinanceResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}
