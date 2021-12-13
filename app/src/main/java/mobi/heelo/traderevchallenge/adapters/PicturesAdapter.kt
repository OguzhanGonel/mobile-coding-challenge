package mobi.heelo.traderevchallenge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_picture.view.*
import mobi.heelo.traderevchallenge.R
import mobi.heelo.traderevchallenge.models.UnsplashResponseItem

class PicturesAdapter: RecyclerView.Adapter<PicturesAdapter.PictureViewHolder>() {

    inner class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<UnsplashResponseItem>() {
        override fun areItemsTheSame(
            oldItem: UnsplashResponseItem, newItem: UnsplashResponseItem
        ): Boolean {
            //  since items have unique Ids, its sufficient to just check them
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UnsplashResponseItem, newItem: UnsplashResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_picture, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val currentPicture = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(currentPicture.urls.thumb)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo_item_iv)

            setOnClickListener {
                onItemClickListener?.let { it(currentPicture, position) }
            }

        }
    }

    private var onItemClickListener: ((UnsplashResponseItem, position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (UnsplashResponseItem, position: Int) -> Unit) {
        onItemClickListener = listener
    }

}