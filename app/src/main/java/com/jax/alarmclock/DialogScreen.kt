package com.jax.alarmclock

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DialogScreen : AppCompatActivity() {

    private var ringtone: Ringtone? = null
    private lateinit var buttonCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_screen)
        buttonCancel = findViewById(R.id.buttonCancel)
        buttonCancel.setOnClickListener {
            ringtone?.stop()
        }
        ringtone = RingtoneManager.getRingtone(
            this,
            RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE)
        )

        if (ringtone == null) {
            ringtone = RingtoneManager.getRingtone(
                this,
                RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM)
            )
        }

        if (ringtone == null) {
            ringtone = RingtoneManager.getRingtone(
                this,
                RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION)
            )
        }

        ringtone?.let {
            if (!it.isPlaying) {
                it.play()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone?.let {
            if (it.isPlaying) {
                it.stop()
            }
        }
    }
}
