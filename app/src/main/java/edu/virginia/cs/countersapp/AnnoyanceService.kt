package edu.virginia.cs.countersapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class AnnoyanceService : Service() {
    private lateinit var player: MediaPlayer;

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI)
        player.isLooping = true
        player.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}