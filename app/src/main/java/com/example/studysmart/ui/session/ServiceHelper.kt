package com.example.studysmart.ui.session

import android.content.Context
import android.content.Intent
import androidx.compose.material3.contentColorFor

object ServiceHelper {
    fun triggerForeGroundServiceI(context: Context , action : String){
        Intent(context , StudySessionTimerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}