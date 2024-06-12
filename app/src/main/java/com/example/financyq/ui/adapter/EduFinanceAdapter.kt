package com.example.financyq.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.financyq.data.response.EduFinanceResponse
import com.example.financyq.databinding.ItemListEdufinanceBinding

class EduFinanceAdapter : RecyclerView.Adapter<EduFinanceAdapter.EduFinanceViewHolder>() {

    private val items = ArrayList<EduFinanceResponse>()

    fun submitList(newItems: List<EduFinanceResponse>) {
        items.clear()
        items.addAll(newItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EduFinanceViewHolder {
        val binding = ItemListEdufinanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EduFinanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EduFinanceViewHolder, position: Int) {
        Log.e("fikry6", "onBindViewHolder: ${items[position].imageUrl}", )
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class EduFinanceViewHolder(private val binding: ItemListEdufinanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EduFinanceResponse) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
            Glide.with(binding.root)
                .load(item.imageUrl)
                .into(binding.imgEducation)
                .clearOnDetach()
        }
    }
}
