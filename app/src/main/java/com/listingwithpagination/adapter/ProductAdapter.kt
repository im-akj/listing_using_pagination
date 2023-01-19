package com.listingwithpagination.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.listingwithpagination.adapter.ProductAdapter.ImageViewHolder
import com.listingwithpagination.databinding.ProductLayoutBinding
import com.listingwithpagination.model.Product
import javax.inject.Inject

class ProductAdapter @Inject constructor() :
    PagingDataAdapter<Product, ImageViewHolder>(diffCallback) {
    private val favoritesState = HashMap<Int, Boolean>()
    inner class ImageViewHolder(val binding: ProductLayoutBinding) :
        ViewHolder(binding.root) {
            init {
                binding.actionBtn.setOnClickListener {
                }
                binding.addToWishlist.setOnClickListener {
                    val state = favoritesState.getOrDefault(absoluteAdapterPosition, false);
                    favoritesState[absoluteAdapterPosition] = !state
                    binding.addToWishlist.setImageResource(if(!state) android.R.drawable.star_on else android.R.drawable.star_off)
                }
            }
        }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ProductLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            holder.itemView.apply {
                tvName.text = "${item?.name}"
                tvPrice.text = "₹${item?.final_price}"
                ratingBar.rating = item?.rating ?: 0F
                ratingBarTxt.text = "(${item?.rating_count})"
                addToWishlist.setImageResource(if(favoritesState.getOrDefault(position, false)) android.R.drawable.star_on else android.R.drawable.star_off)
                imageView.load(item?.image_url) {
                    crossfade(true)
                    crossfade(1000)
                }

                if ((item?.quantity ?: 0) < 1) {
                    imageView.alpha = 0.4F
                    oosTxt.visibility = View.VISIBLE
                    actionBtn.text = "Notify Me"
                } else {
                    imageView.alpha = 1F
                    oosTxt.visibility = View.GONE
                    actionBtn.text = item?.button_text ?: "Add to Bag"
                }
            }
        }
    }

    fun isLast(findLastVisibleItemPosition: Int): Boolean {
        return itemCount - 1 == findLastVisibleItemPosition
    }
}