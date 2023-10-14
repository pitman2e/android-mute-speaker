package com.github.pitman2e.mutespeaker.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.pitman2e.mutespeaker.MuteServiceToggle

class OnBootBroadcastReceiver : BroadcastReceiver() {
    var TAG = this.javaClass.simpleName
    override fun onReceive(context: Context, intent: Intent) {
        MuteServiceToggle.enforceByPref(context)
    }
}