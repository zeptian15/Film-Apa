package com.septian.filmapauiux.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.septian.filmapauiux.BuildConfig
import com.septian.filmapauiux.R
import com.septian.filmapauiux.model.Favourite
import kotlinx.android.synthetic.main.item_favourite.view.*

class FavouriteAdapter: RecyclerView.Adapter<FavouriteAdapter.FavViewHolder>() {
    // Inisialisasi Item Click
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    var favList = ArrayList<Favourite>()
        set(favList){
            if(favList.size > 0){
                this.favList.clear()
            }
            this.favList.addAll(favList)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteAdapter.FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite,parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteAdapter.FavViewHolder, position: Int) {
        holder.bind(favList[position])
    }

    override fun getItemCount(): Int = this.favList.size

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fav: Favourite) {
            with(itemView) {
                with(itemView) {
                    Glide.with(itemView.context)
                        .load(BuildConfig.URL_IMAGES + fav.poster)
                        .apply(RequestOptions().override(550, 550))
                        .into(img_poster)
                    Glide.with(itemView.context)
                        .load(BuildConfig.URL_IMAGES + fav.background)
                        .apply(RequestOptions().override(550, 550))
                        .into(img_background)
                    tv_title_favourite.text = fav.title

                    itemView.setOnClickListener { onItemClickCallback?.onItemClicked(fav) }
                }
            }
        }
    }

    fun addItem(fav: Favourite){
        this.favList.add(fav)
        notifyItemInserted(this.favList.size - 1)
    }

    fun removeItem(position: Int){
        this.favList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.favList.size)
    }

    // Inisialisasi Interface OnItemClick
    interface OnItemClickCallback {
        fun onItemClicked(data: Favourite)
    }
}