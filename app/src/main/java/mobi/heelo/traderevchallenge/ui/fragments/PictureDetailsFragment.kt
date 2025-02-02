package mobi.heelo.traderevchallenge.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_picture_detail.*
import mobi.heelo.traderevchallenge.MainActivity
import mobi.heelo.traderevchallenge.R
import mobi.heelo.traderevchallenge.adapters.ViewPagerAdapter
import mobi.heelo.traderevchallenge.viewmodels.PicturesViewModel

class PictureDetailsFragment : Fragment(R.layout.fragment_picture_detail) {

    lateinit var viewModel: PicturesViewModel

    var isInitialLoad: Boolean = true

    // if I wanted to access the args from the Navigation Component, I would use the below
//    val args: PictureDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        var adapter = ViewPagerAdapter(viewModel.picturesArray, view.context)

        viewPager2.adapter = adapter
        viewPager2.setOffscreenPageLimit(viewModel.picturesArray.size - 1);

        //  If I wanted to access the Navigation Components args
//        val scrollToPosition = args.positionValue

        viewPager2.registerOnPageChangeCallback(this@PictureDetailsFragment.pageChanger)
    }

    val pageChanger = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            if (!isInitialLoad) {
                viewModel.currentDetailImagePosition = position
            }
            isInitialLoad = false
            viewModel.shouldScrollRecyclerView = true
        }
    }


    override fun onResume() {
        super.onResume()

        if (viewPager2 != null) {
            viewPager2.postDelayed(Runnable {
                viewPager2.setCurrentItem(viewModel.currentDetailImagePosition)
            }, 100)
        }
    }

}