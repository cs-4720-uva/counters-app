package edu.virginia.cs.countersapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service.START_STICKY
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class TestActionReceiver : BroadcastReceiver() {
    var notificationStarted = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (intent.action == "TEST_ACTION" && !notificationStarted) {
            val intent = Intent(context, MyNotificationService::class.java)
            context.startForegroundService(intent)
            println("Received Test Action")
            notificationStarted = true
        } else if (intent.action == "TEST_ACTION" && notificationStarted) {
            val intent = Intent(context, MyNotificationService::class.java)
            context.stopService(intent)
            notificationStarted = false
        }
    }
}