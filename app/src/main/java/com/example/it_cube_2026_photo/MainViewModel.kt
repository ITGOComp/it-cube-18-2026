package com.example.it_cube_2026_photo

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)      // идёт ли сейчас генерация
        private set
    var bitmap by mutableStateOf<Bitmap?>(null) // готовая картинка (или null)
        private set
    var failed by mutableStateOf(false)         // не получилось ли
        private set

    fun generate(prompt: String) {
        isLoading = true
        failed = false
        viewModelScope.launch {                 // работаем в фоне
            bitmap = ImageClient.generate(prompt)
            failed = bitmap == null
            // ПРОПУСК 3: ??? = false
        }
    }
}
