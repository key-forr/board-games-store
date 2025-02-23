package com.example.tutor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tutor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = DbHelper.getDb(this)

        binding.button.setOnClickListener {
            val user = User(null,
                "Denys", "Kifor")

            Thread{
                db.getUserDao().insertUser(user)
            }.start()
        }



    }
}
