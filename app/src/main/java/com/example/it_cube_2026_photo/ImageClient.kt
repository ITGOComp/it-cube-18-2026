package com.example.it_cube_2026_photo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

object ImageClient {

    private const val AUTH_URL = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth"
    private const val API_URL = "https://gigachat.devices.sberbank.ru/api/v1"

    // Возвращает картинку или null, если что-то пошло не так.
    // "suspend" и "Dispatchers.IO" - чтобы долгий запрос шёл в фоне и приложение не зависало.
    suspend fun generate(text: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val token = getToken()
            val imageId = askToDraw(token, text)
            downloadImage(token, imageId)
        } catch (e: Exception) {
            null
        }
    }

    // Шаг 1: получаем токен
    private fun getToken(): String {
        val connection = URL(AUTH_URL).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Authorization", "Basic ${BuildConfig.GIGACHAT_KEY}")
        connection.setRequestProperty("RqUID", UUID.randomUUID().toString())
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        connection.doOutput = true
        connection.outputStream.write("scope=GIGACHAT_API_PERS".toByteArray())

        val answer = connection.inputStream.bufferedReader().readText()
        return JSONObject(answer).getString("access_token")
    }

    // Шаг 2: просим нарисовать, получаем номер картинки
    private fun askToDraw(token: String, text: String): String {
        val messages = JSONArray()
            .put(JSONObject().put("role", "system").put("content", "Ты — Василий Кандинский"))
            .put(JSONObject().put("role", "user").put("content", "Нарисуй: $text"))
        val body = JSONObject()
            .put("model", "GigaChat")
            .put("messages", messages)
            .put("function_call", "???")
            .toString()

        val connection = URL("$API_URL/chat/completions").openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.readTimeout = 120_000 // рисование может занять пару минут
        connection.setRequestProperty("Authorization", "Bearer $token")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true
        connection.outputStream.write(body.toByteArray())

        val answer = connection.inputStream.bufferedReader().readText()
        val content = JSONObject(answer)
            .getJSONArray("choices").getJSONObject(0)
            .getJSONObject("message").getString("content")

        return Regex("""<img\s+src="([^"]+)"""").find(content)!!.groupValues[1]
    }

    // Шаг 3: скачиваем картинку
    private fun downloadImage(token: String, imageId: String): Bitmap? {
        val connection = URL("$API_URL/files/$imageId/content").openConnection() as HttpURLConnection
        connection.setRequestProperty("Authorization", "Bearer $token")
        connection.setRequestProperty("Accept", "application/jpg")
        return BitmapFactory.decodeStream(connection.inputStream)
    }
}
