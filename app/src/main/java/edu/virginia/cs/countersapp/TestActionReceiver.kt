package edu.virginia.cs.countersapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TestActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (intent.action == "TEST_ACTION") {
            println("Received Test Action")
        }
    }
}