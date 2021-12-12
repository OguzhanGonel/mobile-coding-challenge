package mobi.heelo.traderevchallenge.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_pictures.*
import mobi.heelo.traderevchallenge.MainActivity
import mobi.heelo.traderevchallenge.R
import mobi.heelo.traderevchallenge.adapters.PicturesAdapter
import mobi.heelo.traderevchallenge.utilities.Resource
import mobi.heelo.traderevchallenge.viewmodels.PicturesViewModel


class PicturesFragment : Fragment(R.layout.fragment_pictures) {

    val TAG = "PicturesFragment"

    lateinit var viewModel: PicturesViewModel
    lateinit var picturesAdapter: PicturesAdapter
    lateinit var staggeredGlManager: StaggeredGridLayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // casting it as MainActivity so that we have access to the viewmodel create in the activity
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        setViews()
    }


    private fun setupRecyclerView() {
        picturesAdapter = PicturesAdapter()

        staggeredGlManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        pictures_rv.apply {
            adapter = picturesAdapter
            layoutManager = staggeredGlManager
            addOnScrollListener(this@PicturesFragment.scrollListener)
            setHasFixedSize(true)
            itemAnimator = null
            setItemViewCacheSize(20)
        }
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }



    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // I will implement the logic for infinite scrolling later
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // I will implement the logic for infinite scrolling later

        }
    }

    private fun setViews() {
        picturesAdapter.setOnItemClickListener { unsplashResponseItem, position ->

            val bundle = Bundle().apply {
                putInt("positionValue", position)
            }

            viewModel.currentDetailImagePosition = position

            findNavController().navigate(
                R.id.action_picturesFragment_to_pictureDetailsFragment,
                bundle
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
    }

    private fun showProgressBar() {
        pagination_pb.visibility = View.VISIBLE
    }


}