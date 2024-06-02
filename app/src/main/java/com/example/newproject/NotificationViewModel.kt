package com.example.newproject

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.IOException

class NotificationViewModel(val token: String): ViewModel() {

    private val api: FcmApi = Retrofit.Builder()
//        .baseUrl("http://192.168.143.160:8081")  //for mobile fon connected on same wifi as computer
        .baseUrl("http://10.0.2.2:8081")//for emulator
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()


    fun sendMessage(msg: String , isBroadcast: Boolean) {
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = if(isBroadcast) null else token,
                notification = NotificationBody(
                    title = "New message!",
                    body = msg
                )
            )
            Log.d("check", "token : $token")

            try {
                if(isBroadcast) {
                    api.broadcast(messageDto)
                } else {
                    api.sendMessage(messageDto)
                }

            } catch(e: HttpException) {
                e.printStackTrace()
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }
}