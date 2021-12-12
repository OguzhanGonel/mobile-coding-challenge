package mobi.heelo.traderevchallenge.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import mobi.heelo.traderevchallenge.MainActivity
import mobi.heelo.traderevchallenge.R
import mobi.heelo.traderevchallenge.adapters.PicturesAdapter
import mobi.heelo.traderevchallenge.utilities.Resource
import mobi.heelo.traderevchallenge.viewmodels.PicturesViewModel

class PicturesFragment : Fragment(R.layout.fragment_pictures) {

    val TAG = "PicturesFragment"

    lateinit var viewModel: PicturesViewModel
    lateinit var picturesAdapter: PicturesAdapter
    lateinit var pictures_rv: RecyclerView
    lateinit var pagination_pb: ProgressBar

    var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // casting it as MainActivity so that we have access to the viewmodel create in the activity
        viewModel = (activity as MainActivity).viewModel

        initializeViews(view)
        setupRecyclerView()

        setViews()
    }

    override fun onResume() {
        super.onResume()
        pictures_rv.scrollToPosition(viewModel.currentDetailImagePosition)
    }

    override fun onPause() {
        super.onPause()
        viewModel.rvState = pictures_rv.layoutManager?.onSaveInstanceState()
    }

    private fun initializeViews(view: View) {
        pictures_rv = view.findViewById(R.id.pictures_rv)
        pagination_pb = view.findViewById(R.id.pagination_pb)
        picturesAdapter = PicturesAdapter()
    }

    private fun setupRecyclerView() {
        pictures_rv.setHasFixedSize(true)
        pictures_rv.itemAnimator = null
        pictures_rv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        pictures_rv.addOnScrollListener(this@PicturesFragment.scrollListener)

        pictures_rv.adapter = picturesAdapter
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
                putInt("currentPosition", position)
            }

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
                            "Sorry, an error occured: $message ",
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



}