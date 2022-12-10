package com.example.bisindo_recognitions.presentasion.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bisindo_recognitions.databinding.ItemCardHomeBinding
import com.example.bisindo_recognitions.model.remote.recognitionresponse.BisindoApiResponse

class HomeRecyclerViewAdapter(private var dataItem : BisindoApiResponse): RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemCardHomeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCardHomeBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataItem[position]
        holder.binding.apply {
            textView2.text = item.name

            Glide.with(holder.itemView.context)
                .load(item.image)
                .into(imageView)
        }
    }

    override fun getItemCount(): Int = dataItem.size

}