package at.sunilson.core.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

abstract class BaseBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {}
}