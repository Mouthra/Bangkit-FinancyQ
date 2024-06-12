package com.example.financyq.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.financyq.R
import com.example.financyq.ui.welcome.WelcomeActivity
import com.example.financyq.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val fadeIn: Animation = AnimationUtils.loadAnimation(this, R.anim.fade)
        val slideUp: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        // Apply animations
        binding.logo.startAnimation(fadeIn)
        binding.title.startAnimation(slideUp)
        binding.team.startAnimation(slideUp)
        binding.gp1.startAnimation(fadeIn)
        binding.gp2.startAnimation(fadeIn)

        // Start main activity after some delay
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3 seconds delay

    }



}