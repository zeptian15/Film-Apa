package com.septian.filmapauiux.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.septian.filmapauiux.BuildConfig
import com.septian.filmapauiux.R
import com.septian.filmapauiux.model.TvShow
import kotlinx.android.synthetic.main.item_tv.view.*

class TvShowRecyclerAdapter :
    RecyclerView.Adapter<TvShowRecyclerAdapter.TvViewHolder>() {
    // Inisialisasi Item Click
    private var onItemClickCallback: OnItemClickCallback? = null
    private val mData = ArrayList<TvShow>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<TvShow>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TvViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_tv, viewGroup, false)
        return TvViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: TvViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class TvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: TvShow) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(BuildConfig.URL_IMAGES + tvShow.poster)
                    .apply(RequestOptions().override(550, 550))
                    .into(img_poster)
                Glide.with(itemView.context)
                    .load(BuildConfig.URL_IMAGES + tvShow.background)
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