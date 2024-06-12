package com.example.financyq.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.financyq.R
import com.example.financyq.data.response.EduFinanceResponse
import com.example.financyq.databinding.ItemListHomeBinding

class ShortcutAdapter : RecyclerView.Adapter<ShortcutAdapter.EduFinanceViewHolder>() {

    private val items = ArrayList<EduFinanceResponse>()

    fun submitList(newItems: List<EduFinanceResponse>){
        items.clear()
        items.addAll(newItems)
        Log.e("fikry2", "submitList: ${items[0]}", )

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EduFinanceViewHolder {
        Log.e("fikry7", "fikry: ${items[0]}", )
        val binding = ItemListHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EduFinanceViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ShortcutAdapter.EduFinanceViewHolder, position: Int) {
        Log.e("fikry3", "onBindViewHolder: ${items[position].imageUrl}", )
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class EduFinanceViewHolder(private val binding: ItemListHomeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: EduFinanceResponse){
            Log.e("fikry4", "bind: ${item.imageUrl}", )
            binding.tvDetails.text = R.string.see_details.toString()
            Glide.with(binding.root)
                .load(item.imageUrl)
                .into(binding.imgEducation)
                .clearOnDetach()
        }
    }
}