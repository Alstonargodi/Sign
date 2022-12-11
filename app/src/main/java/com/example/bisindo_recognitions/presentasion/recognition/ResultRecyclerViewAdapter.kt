package com.example.bisindo_recognitions.presentasion.recognition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bisindo_recognitions.databinding.ItemCardPredictBinding
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.util.*
import kotlin.math.min

class ResultRecyclerViewAdapter: RecyclerView.Adapter<ResultRecyclerViewAdapter.ViewHolder>() {
    private var categories : MutableList<Category?> = mutableListOf()
    private var adapterSize : Int = 0

    class ViewHolder(private val binding : ItemCardPredictBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(label: String?, score: Float?) {
            with(binding) {
                var label = label ?: "-"
                var accuracy = if (score != null) String.format(
                    Locale.US,
                    "%.2f",
                    score
                ) else "-"
                tvResult.text = "$label - $accuracy"
            }
        }
    }

    fun updateResults(listClassifications: List<Classifications>?) {
        categories = MutableList(adapterSize) { null }
        listClassifications?.let { it ->
            if (it.isNotEmpty()) {
                val sortedCategories = it[0].categories.sortedBy { it?.index }
                val min = min(sortedCategories.size, categories.size)
                for (i in 0 until min) {
                    categories[i] = sortedCategories[i]
                }
            }
        }
    }

    fun updateAdapterSize(size: Int) {
        adapterSize = size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCardPredictBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       categories[position].let { category ->
           holder.bind(category?.label,category?.score)
       }
    }

    override fun getItemCount(): Int = categories.size
}