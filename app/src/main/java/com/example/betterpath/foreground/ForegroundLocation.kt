package com.example.betterpath.foreground

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.betterpath.R

class ForegroundLocation : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    enum class Actions {
        START, STOP
    }

    private fun start(){
        val notification = NotificationCompat.Builder(this, "better_path_channel")
            .setSmallIcon(R.drawable.logo_ia)
            .setContentTitle(getString(R.string.localization_is_active))
            .setContentInfo("")
            .setAutoCancel(false)
            .build()

        // id necessariao per aggiornare la notifica senza crearne una nuova
        // id >= 1
        startForeground(1, notification)

    }

    private fun stop(){
        val notification = NotificationCompat.Builder(this, "better_path_channel")
            .setSmallIcon(R.drawable.logo_ia)
            .setContentTitle(getString(R.string.end_of_tracking))
            .setContentInfo(getString(R.string.end_of_tracking_ConInfo))
            .setAutoCancel(false)
            .build()
        startForeground(1, notification)
        stopSelf()
    }
}