package mobi.heelo.traderevchallenge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import mobi.heelo.traderevchallenge.R
import mobi.heelo.traderevchallenge.models.UnsplashResponseItem

class ViewPagerAdapter(
    var pictures: ArrayList<UnsplashResponseItem>, var context:
    Context
) : RecyclerView.Adapter<ViewPagerAdapter.PictureViewHolder>() {

    inner class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_picture_detail, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val currentPicture = pictures[position]

        val photo_detail_item_iv: ImageView =
            holder.itemView.findViewById<ImageView>(R.id.photo_detail_item_iv)

        Glide.with(context).load(currentPicture.urls.thumb).into(photo_detail_item_iv)
    }

    override fun getItemCount(): Int {
        return pictures.size
    }
}