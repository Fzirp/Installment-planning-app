package com.installment.manager.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.installment.manager.InstallmentApp
import com.installment.manager.MainActivity
import com.installment.manager.R
import com.installment.manager.util.PersianDateUtil

class PaymentAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val installmentTitle = intent.getStringExtra("installment_title") ?: "قسط"
        val amount = intent.getLongExtra("amount", 0)
        val dueDateMillis = intent.getLongExtra("due_date", System.currentTimeMillis())
        val installmentId = intent.getLongExtra("installment_id", 0)
        val paymentNumber = intent.getIntExtra("payment_number", 0)

        val persianDate = PersianDateUtil.fromMillis(dueDateMillis)
        val amountFormatted = PersianDateUtil.formatCurrency(amount / 10) // Convert Rial to Toman

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("installment_id", installmentId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            installmentId.toInt(),
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, InstallmentApp.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("یادآوری پرداخت قسط")
            .setContentText("$installmentTitle - قسط ${PersianDateUtil.toPersianDigits(paymentNumber)}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$installmentTitle\nقسط شماره ${PersianDateUtil.toPersianDigits(paymentNumber)}\nمبلغ: $amountFormatted\nسررسید: ${persianDate.format()}")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(
                (installmentId * 100 + paymentNumber).toInt(),
                notification
            )
        } catch (e: SecurityException) {
            // Notification permission not granted
            e.printStackTrace()
        }
    }
}
