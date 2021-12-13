package mobi.heelo.traderevchallenge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_picture_detail.view.*
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

        holder.itemView.apply {

            Glide.with(context).load(currentPicture.urls.thumb).into(photo_detail_item_iv)
            photo_detail_item_iv.contentDescription = currentPicture.description

            Glide.with(context).load(currentPicture.user.profile_image.medium)
                .into(user_profile_image_iv)
            user_name_tv.text = currentPicture.user.name
            user_username_tv.text = "@".plus(currentPicture.user.username)
            number_of_likes_tv.text = currentPicture.likes.toString()
        }
    }

    override fun getItemCount(): Int {
        return pictures.size
    }
}