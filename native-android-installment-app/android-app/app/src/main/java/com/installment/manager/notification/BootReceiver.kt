package com.installment.manager.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.installment.manager.InstallmentApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            // Reschedule all alarms after boot
            val db = (context.applicationContext as InstallmentApp).database
            CoroutineScope(Dispatchers.IO).launch {
                val installments = db.installmentDao().getInstallmentsWithNotifications()
                for (installment in installments) {
                    val payments = db.paymentDao().getPaymentsForInstallmentOnce(installment.id)
                    val unpaidPayments = payments.filter { !it.isPaid }
                    for (payment in unpaidPayments) {
                        NotificationScheduler.schedulePaymentReminder(
                            context = context,
                            installmentId = installment.id,
                            installmentTitle = installment.title,
                            paymentNumber = payment.paymentNumber,
                            amount = payment.amount,
                            dueDateMillis = payment.dueDateMillis,
                            reminderDaysBefore = installment.reminderDaysBefore
                        )
                    }
                }
            }
        }
    }
}
