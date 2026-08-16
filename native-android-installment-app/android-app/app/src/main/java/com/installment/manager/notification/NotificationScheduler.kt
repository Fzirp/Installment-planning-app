package com.installment.manager.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object NotificationScheduler {

    fun schedulePaymentReminder(
        context: Context,
        installmentId: Long,
        installmentTitle: String,
        paymentNumber: Int,
        amount: Long,
        dueDateMillis: Long,
        reminderDaysBefore: Int = 3
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, PaymentAlarmReceiver::class.java).apply {
            action = "com.installment.manager.PAYMENT_REMINDER"
            putExtra("installment_title", installmentTitle)
            putExtra("amount", amount)
            putExtra("due_date", dueDateMillis)
            putExtra("installment_id", installmentId)
            putExtra("payment_number", paymentNumber)
        }

        val requestCode = (installmentId * 100 + paymentNumber).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Calculate reminder time: X days before due date, at 9:00 AM
        val reminderCal = Calendar.getInstance().apply {
            timeInMillis = dueDateMillis
            add(Calendar.DAY_OF_MONTH, -reminderDaysBefore)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val reminderTimeMillis = reminderCal.timeInMillis

        // Only schedule if the reminder is in the future
        if (reminderTimeMillis > System.currentTimeMillis()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            reminderTimeMillis,
                            pendingIntent
                        )
                    } else {
                        // Fallback to inexact alarm
                        alarmManager.setAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            reminderTimeMillis,
                            pendingIntent
                        )
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        reminderTimeMillis,
                        pendingIntent
                    )
                }
            } catch (e: SecurityException) {
                // Fallback to inexact alarm
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    reminderTimeMillis,
                    pendingIntent
                )
            }
        }
    }

    fun cancelPaymentReminder(
        context: Context,
        installmentId: Long,
        paymentNumber: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, PaymentAlarmReceiver::class.java).apply {
            action = "com.installment.manager.PAYMENT_REMINDER"
        }

        val requestCode = (installmentId * 100 + paymentNumber).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

    fun cancelAllRemindersForInstallment(
        context: Context,
        installmentId: Long,
        totalPayments: Int
    ) {
        for (i in 1..totalPayments) {
            cancelPaymentReminder(context, installmentId, i)
        }
    }
}
