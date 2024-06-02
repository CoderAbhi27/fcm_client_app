package com.example.newproject

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {

    private lateinit var submitBtn: Button
    private lateinit var tokenBtn: Button
    private lateinit var tokenET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestNotificationPermission() //sending the request for notification permission

        tokenET = findViewById(R.id.tokenET)
        submitBtn = findViewById(R.id.submitBtn)
        tokenBtn = findViewById(R.id.tokenBtn)

        tokenBtn.setOnClickListener {
            generateToken()
        }

        submitBtn.setOnClickListener {
            val intent = Intent(this, SendNotificationActivity::class.java)
            val remoteToken = tokenET.text?.toString()
            intent.putExtra("remoteToken", remoteToken)
            startActivity(intent)
        }

    }

    private fun requestNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if(!hasPermission) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun generateToken(){
        lifecycleScope.launch {
            val localToken = Firebase.messaging.token.await()
            Log.d("check", "token : $localToken")
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(ClipData.newPlainText("localToken" , localToken))

            Toast.makeText(
                this@MainActivity,
                "Copied local token!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}