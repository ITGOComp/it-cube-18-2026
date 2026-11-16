package com.example.it_cube_2026_photo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// Цвета приложения: тёмный фон и коралловая кнопка
private val colors = darkColorScheme(
    primary = Color(0xFFFF7A45),
    onPrimary = Color(0xFF1A0E08),
    background = Color(0xFF14110F),
    surface = Color(0xFF14110F),
    surfaceVariant = Color(0xFF211C19)
)

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Рисуем под строкой состояния, значки на панелях светлые
        val bars = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        enableEdgeToEdge(bars, bars)

        // Отсюда начинается интерфейс на Compose
        setContent {
            MaterialTheme(colorScheme = colors) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GeneratorScreen(
                        isLoading = viewModel.isLoading,
                        bitmap = viewModel.bitmap,
                        failed = viewModel.failed,
                        onGenerate = { viewModel.generate(it) }
                    )
                }
            }
        }
    }
}
