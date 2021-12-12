package mobi.heelo.traderevchallenge.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import mobi.heelo.traderevchallenge.MainActivity
import mobi.heelo.traderevchallenge.R
import mobi.heelo.traderevchallenge.adapters.ViewPagerAdapter
import mobi.heelo.traderevchallenge.viewmodels.PicturesViewModel

class PictureDetailsFragment : Fragment(R.layout.fragment_picture_detail) {
    val TAG = "PictureDetailsFragment"

    lateinit var viewModel: PicturesViewModel

    val args: PictureDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel



        var adapter = ViewPagerAdapter(viewModel.picturesArray, view.context)

        val viewPager2 = view.findViewById<ViewPager2>(R.id.viewPager2)
        viewPager2.adapter = adapter
        viewPager2.setOffscreenPageLimit(viewModel.picturesArray.size - 1);

        val position = args.positionValue
        viewPager2.setCurrentItem(position, true)


        viewPager2.registerOnPageChangeCallback(this@PictureDetailsFragment.pageChanger)

    }

    val pageChanger = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            Log.d(TAG, "onPageScrolled: position: $position")
            viewModel.currentDetailImagePosition = position
        }
    }
}