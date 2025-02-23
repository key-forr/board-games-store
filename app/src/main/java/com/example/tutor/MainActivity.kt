package com.example.tutor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tutor.data.database.DbHelper
import com.example.tutor.data.entity.User
import com.example.tutor.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = DbHelper.getDb(this)

        binding.button.setOnClickListener {
            val user = User(null, "Denys", "Kifor")

            lifecycleScope.launch(Dispatchers.IO) {
                db.getUserDao().insertUser(user)
                db.getUserDao().clearTable()
            }
        }
    }
}
