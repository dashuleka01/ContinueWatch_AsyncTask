package ru.spbstu.icc.kspt.lab2.continuewatch

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    var secondsElapsed: Int = 0

    companion object {
        val NUM = "number"
    }

    var my = myAsync()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        loadSeconds()
        my = myAsync()
        my.execute()
    }

    override fun onStop() {
        super.onStop()
        saveSeconds()
        my.cancel(true)
    }

    private fun saveSeconds() {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val ed: SharedPreferences.Editor = sPref.edit()
        ed.putInt(NUM, secondsElapsed)
        ed.commit()
        Log.v("myapptest", "saveSeconds")
    }

    private fun loadSeconds() {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        secondsElapsed = sPref.getInt(NUM, 0)
        Log.v("myapptest", "loadSeconds")
    }

    inner class myAsync() : AsyncTask<Void, Int, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                while (true){
                    if (isCancelled) return null
                    publishProgress(++secondsElapsed)
                    TimeUnit.SECONDS.sleep(1)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            findViewById<TextView>(R.id.textSecondsElapsed)
                .text = "Seconds elapsed: " + values[0]
        }
    }
}

