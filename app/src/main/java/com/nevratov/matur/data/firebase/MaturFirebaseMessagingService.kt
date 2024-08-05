package com.nevratov.matur.data.firebase


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nevratov.matur.R
import com.nevratov.matur.presentation.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MaturFirebaseMessagingService () : FirebaseMessagingService() {



    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        log("From: ${message.from}")

        //
        if (message.data.isNotEmpty()) {
            log("Message data payload: ${message.data}")
        }

        //
        message.notification?.let {
            log("Message Notification Body: ${it.body}")
            it.body?.let { body ->
                sendNotification(body)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "onNewToken = $token")
    }


    private fun sendNotification(messageBody: String) {
        log("in sendNotification")
        val requestCode = 0
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val chanelId = CHANEL_ID
        val notificationBuilder = NotificationCompat.Builder(this, chanelId)
            .setSmallIcon(R.drawable.matur_ico)
            .setContentTitle(CONTENT_TITLE)
            .setContentText(messageBody)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            chanelId,
            CHANEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        log("notification notify")
    }

    companion object {
        private const val CHANEL_ID = "20"
        private const val CONTENT_TITLE = "Сообщение"
        private const val CHANEL_NAME = "received_messages"
        private const val NOTIFICATION_ID = 22

    }

    private fun log(message: String) {
        Log.d("FCM", message)
    }
}