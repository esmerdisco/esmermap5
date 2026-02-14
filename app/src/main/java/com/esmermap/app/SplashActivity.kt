package com.esmermap.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashImage = findViewById<ImageView>(R.id.splashImage)

        Glide.with(this)
            .asGif()
            .load(R.drawable.splash)
            .into(object : CustomTarget<GifDrawable>() {

                override fun onResourceReady(
                    resource: GifDrawable,
                    transition: Transition<in GifDrawable>?
                ) {
                    splashImage.setImageDrawable(resource)
                    resource.setLoopCount(1) // فقط یک بار پخش شود
                    resource.start()

                    val duration = resource.duration

                    splashImage.postDelayed({
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }, duration.toLong())
                }

                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {}
            })
    }
}
