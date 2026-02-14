package com.esmermap.app

import android.content.Intent
import android.graphics.Movie
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashImage = findViewById<ImageView>(R.id.splashImage)

        // نمایش GIF
        Glide.with(this)
            .asGif()
            .load(R.drawable.splash)
            .into(splashImage)

        // گرفتن مدت واقعی GIF
        val gifDuration = getGifDuration(R.drawable.splash)

        // +1000ms برای موندن فریم آخر
        val totalTime = gifDuration + 1000L

        splashImage.postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
            overridePendingTransition(0, 0)
        }, totalTime)
    }

    private fun getGifDuration(resId: Int): Long {
        val input = resources.openRawResource(resId)
        val bytes = input.use {
            val buffer = ByteArrayOutputStream()
            val tmp = ByteArray(8 * 1024)
            while (true) {
                val read = it.read(tmp)
                if (read <= 0) break
                buffer.write(tmp, 0, read)
            }
            buffer.toByteArray()
        }

        val movie = Movie.decodeByteArray(bytes, 0, bytes.size)
        val duration = movie?.duration() ?: 0
        return if (duration <= 0) 2000L else duration.toLong()
    }
}
