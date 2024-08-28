package com.jax.alarmclock

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var timePicker: MaterialTimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        button = findViewById(R.id.alarm_button)
    }

    private fun setUpTimePickerDialog() {
        timePicker = MaterialTimePicker
            .Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Set Alarm")
            .build()

        timePicker.show(supportFragmentManager, "TAG")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            setUpAlarm(this, hour, minute)
        }
    }

    private fun setUpClickListeners() {
        button.setOnClickListener {
            setUpTimePickerDialog()
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setUpAlarm(context: Context, hour: Int, minute: Int) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntentForRingtone(context)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val alarmClockInfo = getAlarmClockInfo(calendar, getPendingIntentForDialog(context))

        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
    }

    private fun getAlarmClockInfo(calendar: Calendar, pendingIntent: PendingIntent): AlarmManager.AlarmClockInfo {
        return AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)
    }

    private fun getPendingIntentForDialog(context: Context): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return PendingIntent.getActivity(
            context, ALARM_DIALOG_CODE, intent,  PendingIntent.FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )
    }

    private fun getPendingIntentForRingtone(context: Context): PendingIntent {
        val intent = Intent(this, DialogScreen::class.java).apply {
            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return PendingIntent.getActivity(
            context, ALARM_RINGTONE_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )
    }

    companion object {
        const val ALARM_DIALOG_CODE = 0
        const val ALARM_RINGTONE_CODE = 1
    }
}
