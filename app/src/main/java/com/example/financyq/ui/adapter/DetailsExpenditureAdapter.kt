package com.example.financyq.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financyq.data.response.TransactionsItem
import com.example.financyq.databinding.ItemListDetailsexpenditureBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailsExpenditureAdapter(private val onItemClick: (TransactionsItem) -> Unit) : ListAdapter<TransactionsItem, DetailsExpenditureAdapter.DetailExpenditureViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailExpenditureViewHolder {
        val binding = ItemListDetailsexpenditureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailExpenditureViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: DetailExpenditureViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        Log.e("fikry12", "onBindViewHolder: ${item.deskripsi}")
    }

    class DetailExpenditureViewHolder(private val binding: ItemListDetailsexpenditureBinding, private val onItemClick: (TransactionsItem) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransactionsItem) {
            binding.tvTanggal.text = formatDateTime(item.tanggal)
            binding.tvKategori.text = item.kategori
            binding.tvDeskripsi.text = item.deskripsi
            binding.tvJumlah.text = formatToRupiah(item.jumlah ?: 0)
            binding.tvSumber.text = item.sumber
            Log.e("fikry13", "bind: ${item.deskripsi}")

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }

        private fun formatDateTime(dateTime: String?): String {
            return if (dateTime != null) {
                try {
                    val serverTimeZone = TimeZone.getTimeZone("Asia/Jakarta")
                    val userTimeZone = TimeZone.getDefault()

                    val inputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy, HH:mm", Locale("in", "ID"))
                    inputFormat.timeZone = serverTimeZone

                    val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy, HH:mm", Locale.getDefault())
                    outputFormat.timeZone = userTimeZone

                    val date = inputFormat.parse(dateTime)
                    if (date != null) {
                        outputFormat.format(date)
                    } else {
                        "Tanggal Tidak Diketahui"
                    }
                } catch (e: Exception) {
                    "Tanggal Tidak Valid"
                }
            } else {
                "Tanggal Tidak Diketahui"
            }
        }

        private fun formatToRupiah(value: Int): String {
            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            return numberFormat.format(value)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionsItem>() {
            override fun areItemsTheSame(oldItem: TransactionsItem, newItem: TransactionsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TransactionsItem, newItem: TransactionsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
