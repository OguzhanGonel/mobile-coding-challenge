package mobi.heelo.traderevchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import mobi.heelo.traderevchallenge.repository.PicturesRepository
import mobi.heelo.traderevchallenge.viewmodels.PicturesViewModel
import mobi.heelo.traderevchallenge.viewmodels.PicturesViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: PicturesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val repository = PicturesRepository()
        val viewModelProviderFactory = PicturesViewModelProviderFactory(application, repository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PicturesViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}