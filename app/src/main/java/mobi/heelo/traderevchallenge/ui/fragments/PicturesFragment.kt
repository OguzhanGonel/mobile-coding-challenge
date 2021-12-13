package mobi.heelo.traderevchallenge.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.fragment_pictures.*
import mobi.heelo.traderevchallenge.MainActivity
import mobi.heelo.traderevchallenge.R
import mobi.heelo.traderevchallenge.adapters.PicturesAdapter
import mobi.heelo.traderevchallenge.utilities.Constants.Companion.QUERY_PAGE_SIZE
import mobi.heelo.traderevchallenge.utilities.Resource
import mobi.heelo.traderevchallenge.viewmodels.PicturesViewModel


class PicturesFragment : Fragment(R.layout.fragment_pictures) {

    val TAG = "PicturesFragment"

    lateinit var viewModel: PicturesViewModel
    lateinit var picturesAdapter: PicturesAdapter
    lateinit var staggeredGlManager: StaggeredGridLayoutManager

    val RECYCLER_STATE_KEY = "RECYCLER_STATE"

    var isLoading = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // casting it as MainActivity so that we have access to the viewmodel create in the activity
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        setViews()
    }


    private fun setupRecyclerView() {
        picturesAdapter = PicturesAdapter()

        staggeredGlManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        pictures_rv.apply {
            adapter = picturesAdapter
            layoutManager = staggeredGlManager
            addOnScrollListener(this@PicturesFragment.scrollListener)
            setHasFixedSize(true)
            itemAnimator = null
            setItemViewCacheSize(20)
        }


    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            // checks if we are currently scrolling
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        private fun getFirstVisibleItem(firstVisibleItemPositions: IntArray): Int {
            var minSize = 0
            if (firstVisibleItemPositions.size > 0) {
                minSize = firstVisibleItemPositions[0]
                for (position in firstVisibleItemPositions) {
                    if (position < minSize) {
                        minSize = position
                    }
                }
            }
            return minSize
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val firstVisibleItemPositions =
                (staggeredGlManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(
                    null
                )
            var firstVisibleItem = getFirstVisibleItem(firstVisibleItemPositions)

            val visibleItemCount = staggeredGlManager.childCount
            val totalItemCount = staggeredGlManager.itemCount

            val isLastItem = firstVisibleItem + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItem >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                !isLoading && isLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getPictures()
                isScrolling = false
            }

        }
    }

    private fun setViews() {
        picturesAdapter.setOnItemClickListener { unsplashResponseItem, position ->

            // If I wanted to send the position as an argument of the Navigation Components
//            val bundle = Bundle().apply {
//                putInt("positionValue", position)
//            }

            viewModel.currentDetailImagePosition = position

            findNavController().navigate(
                R.id.action_picturesFragment_to_pictureDetailsFragment,
            )
        }

        viewModel.picturesLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { unsplashResponse ->
                        picturesAdapter.differ.submitList(unsplashResponse.toList())
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "Error occured: $message")
                        Toast.makeText(
                            activity,
                            "Sorry, an error occured: $message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        pagination_pb.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        pagination_pb.visibility = View.VISIBLE
        isLoading = true
    }


    override fun onResume() {
        super.onResume()

// add a should rotate here
        if (pictures_rv != null && viewModel.shouldScrollRecyclerView) {
            viewModel.shouldScrollRecyclerView = false
            pictures_rv.scrollToPosition(viewModel.currentDetailImagePosition)
        }
    }

    override fun onPause() {
        super.onPause()


    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (pictures_rv != null) {
            val layoutManager = pictures_rv.layoutManager as StaggeredGridLayoutManager
            outState.putParcelable(RECYCLER_STATE_KEY, layoutManager.onSaveInstanceState());
        }

        super.onSaveInstanceState(outState)
    }
}