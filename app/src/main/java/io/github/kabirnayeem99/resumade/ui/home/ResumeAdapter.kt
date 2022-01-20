package io.github.kabirnayeem99.resumade.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kabirnayeem99.resumade.databinding.ListItemResumeBinding
import io.github.kabirnayeem99.resumade.domain.entity.ResumeOverview

class ResumeAdapter : RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder>() {

    private var onResumeCardClick: (resumeId: Long) -> Unit = {}

    private var onResumeCardLongClick: (resumeId: Long) -> Unit = {}

    fun setOnResumeCardClickListener(func: (resumeId: Long) -> Unit) {
        onResumeCardClick = func
    }

    fun setOnResumeCardLongClickListener(func: (resumeId: Long) -> Unit) {
        onResumeCardLongClick = func
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResumeViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemResumeBinding.inflate(layoutInflater, parent, false)
        return ResumeViewHolder(binding)
    }


    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ResumeViewHolder, position: Int) {
        val resume = differ.currentList[position]
        holder.bind(resume)
    }

    inner class ResumeViewHolder(val binding: ListItemResumeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(resumeOverview: ResumeOverview) {
            binding.resumeOverview = resumeOverview

            binding.mcvResumeItemRoot.setOnClickListener {
                onResumeCardClick(resumeOverview.id)
            }

            binding.mcvResumeItemRoot.setOnLongClickListener {
                onResumeCardLongClick(resumeOverview.id)
                true
            }
        }
    }


    fun submitList(newResumesList: List<ResumeOverview>) {
        differ.submitList(newResumesList)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<ResumeOverview>() {
        override fun areItemsTheSame(oldItem: ResumeOverview, newItem: ResumeOverview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResumeOverview, newItem: ResumeOverview): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
}