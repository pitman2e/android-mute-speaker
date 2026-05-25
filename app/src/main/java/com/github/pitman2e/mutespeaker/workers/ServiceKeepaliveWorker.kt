package com.github.pitman2e.mutespeaker.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.pitman2e.mutespeaker.MuteServiceToggle.createDisableMuteSpeakerNotification
import com.github.pitman2e.mutespeaker.Prefs
import com.github.pitman2e.mutespeaker.services.HeadsetStateService

class ServiceKeepaliveWorker (context: Context, workerParams: WorkerParameters):
    Worker(context, workerParams) {
    companion object {
        private val TAG = ServiceKeepaliveWorker::class.java.simpleName
    }

    override fun doWork(): Result {
        try
        {
            if (Prefs.getIsEnableNotification(applicationContext)) {
                HeadsetStateService.startService(applicationContext)
            } else {
                HeadsetStateService.stopService(applicationContext)
                createDisableMuteSpeakerNotification(applicationContext)
            }
        } catch (ex: Exception) {
            Log.e(TAG, ex.stackTraceToString())
            return Result.failure()
        }

        return Result.success()
    }
}