package com.septian.rickymaulana.filmapa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.septian.filmapauiux.R
import com.septian.filmapauiux.model.TvShow
import kotlinx.android.synthetic.main.item_tv.view.*

class TvAdapter(private val listTv: ArrayList<TvShow>) :
    RecyclerView.Adapter<TvAdapter.TvViewHolder>() {
    // Inisialisasi Item Click
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TvViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_tv, viewGroup, false)
        return TvViewHolder(view)
    }

    override fun getItemCount(): Int = listTv.size

    override fun onBindViewHolder(holder: TvViewHolder, position: Int) {
        holder.bind(listTv[position])
    }

    inner class TvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: TvShow) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(tvShow.poster)
                    .apply(RequestOptions().override(550, 550))
                    .into(img_background)
                tv_title_tv.text = tvShow.title

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(tvShow) }
            }
        }
    }

    // Inisialisasi Interface OnItemClick
    interface OnItemClickCallback {
        fun onItemClicked(data: TvShow)
    }
}