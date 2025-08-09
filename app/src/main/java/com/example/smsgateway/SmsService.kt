package com.example.smsgateway

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.telephony.SmsManager
import java.net.ServerSocket
import kotlin.concurrent.thread

class SmsService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        thread {
            try {
                val serverSocket = ServerSocket(8080)
                while (true) {
                    val socket = serverSocket.accept()
                    val reader = socket.getInputStream().bufferedReader()
                    val data = reader.readLine()
                    val parts = data.split(",")
                    if (parts.size >= 2) {
                        val phone = parts[0]
                        val message = parts.subList(1, parts.size).joinToString(",")
                        SmsManager.getDefault().sendTextMessage(phone, null, message, null, null)
                    }
                    socket.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return START_STICKY
    }
}