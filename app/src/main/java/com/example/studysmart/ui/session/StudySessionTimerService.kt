package com.example.studysmart.ui.session

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.example.studysmart.util.Constants.ACTION_SERVICE_CANCEL
import com.example.studysmart.util.Constants.ACTION_SERVICE_START
import com.example.studysmart.util.Constants.ACTION_SERVICE_STOP
import com.example.studysmart.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.studysmart.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.studysmart.util.Constants.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import com.example.studysmart.util.pad

@AndroidEntryPoint
class StudySessionTimerService : Service(){

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder : NotificationCompat.Builder

    private lateinit var timer : Timer

    var duration: Duration = Duration.ZERO
        private set
    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set
    var currentTimerState = mutableStateOf(TimerState.IDLE)
        private set


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action.let {
            when(it){
                ACTION_SERVICE_START -> {
                    startForeGroundService()
                    startTimer{ hours , minutes ,seconds ->
                        updateContent(hour = hours , minute = minutes , seconds = seconds)
                    }
                }
                ACTION_SERVICE_STOP ->{

                }
                ACTION_SERVICE_CANCEL ->{

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    @SuppressLint("ForegroundServiceType")
    private fun startForeGroundService() {
        createNotificationChannel()
        startForeground(
            NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW

            )
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun updateContent(hour : String ,minute : String , seconds : String){
            notificationManager
                .notify(
                    NOTIFICATION_ID,
                    notificationBuilder
                        .setContentText("$hour:$minute:$seconds")
                        .build()
                )

    }

    private fun startTimer (
        onTick : (h : String , m : String , s : String) -> Unit
    ){
        currentTimerState.value = TimerState.STARTED
        timer = fixedRateTimer(initialDelay = 1000L , period = 1000L){
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value,minutes.value,seconds.value)
        }
    }

    private fun updateTimeUnits(){
        duration.toComponents{ hours, minutes, seconds, _ ->
            this@StudySessionTimerService.hours.value = hours.pad()
            this@StudySessionTimerService.minutes.value = minutes.pad()
            this@StudySessionTimerService.seconds.value = seconds.pad()
        }
    }

}


enum class TimerState{
    IDLE,
    STARTED,
    STOPPED
}









