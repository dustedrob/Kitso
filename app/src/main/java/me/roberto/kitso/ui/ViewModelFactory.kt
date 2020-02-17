package me.roberto.kitso.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.roberto.kitso.database.BookItemDao
import me.roberto.kitso.repository.KitsoRepository
import javax.inject.Inject

class ViewModelFactory @Inject constructor(val dataSource: BookItemDao,
                                           var kitsoRepository: KitsoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarketViewModel::class.java)) {
            return MarketViewModel(dataSource,kitsoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}