package com.example.newproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SendNotificationActivity : AppCompatActivity() {
    private lateinit var sendBtn: Button
    private lateinit var broadcastBtn: Button
    private lateinit var messageET: EditText
    private lateinit var api: FcmApi
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_send_notification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        token = intent.getStringExtra("remoteToken").toString()

        messageET = findViewById(R.id.msgET)
        broadcastBtn = findViewById(R.id.broadcastBtn)
        sendBtn = findViewById(R.id.sendBtn)

        val vm = NotificationViewModel(token)

        sendBtn.setOnClickListener {
            vm.sendMessage(messageET.text.toString() ,false)
        }
        broadcastBtn.setOnClickListener {
            vm.sendMessage(messageET.text.toString() ,true)
        }
    }

}