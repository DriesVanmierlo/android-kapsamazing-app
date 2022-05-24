package org.vanmierlo.kapsamazing_app

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.vanmierlo.kapsamazing_app.data.KapsalonRoom
import org.vanmierlo.kapsamazing_app.data.MyRepository
import java.lang.IllegalArgumentException

class MainViewModel(private val repository: MyRepository): ViewModel() {
    val allKapsalons: LiveData<List<KapsalonRoom>> = repository.allKapsalons.asLiveData()

    fun insert(kapsalon: KapsalonRoom) = viewModelScope.launch {
        repository.insertKapsalon(kapsalon)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAllKapsalons()
    }
}

class KapsalonViewModelFactory(private val repository: MyRepository): ViewModelProvider.Factory {
    override fun <T:ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}