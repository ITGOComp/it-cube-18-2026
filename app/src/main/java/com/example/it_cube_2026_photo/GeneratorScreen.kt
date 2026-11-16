package com.example.it_cube_2026_photo

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GeneratorScreen(
    isLoading: Boolean,                 // идёт ли генерация
    bitmap: Bitmap?,                    // готовая картинка (или null)
    failed: Boolean,                    // не получилось ли
    onGenerate: (String) -> Unit        // что делать по нажатию на кнопку
) {
    // Текст, который ввёл пользователь
    var text by rememberSaveable { mutableStateOf("") }

    // Column выкладывает всё сверху вниз
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()              // отступы от краёв экрана
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Нарисуй словами",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Лиса в космическом скафандре на Марсе") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onGenerate(text.trim()) },
            enabled = text.isNotBlank(),  // ПРОПУСК 2: кнопка не должна нажиматься, пока идёт рисование (&& ???)
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сгенерировать")
        }

        // Место для результата. Что внутри, зависит от ситуации.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)      // квадрат
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator()
                bitmap != null -> Image(
                    bitmap = bitmap.???(),
                    contentDescription = text,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                failed -> Text("Не получилось. Попробуйте ещё раз")
                else -> Text("Здесь появится картинка")
            }
        }
    }
}
