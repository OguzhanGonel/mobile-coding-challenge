package mobi.heelo.traderevchallenge.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mobi.heelo.traderevchallenge.repository.PicturesRepository

class PicturesViewModelProviderFactory(
    val app: Application,
    val picturesRepository: PicturesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PicturesViewModel(app, picturesRepository) as T
    }
}
