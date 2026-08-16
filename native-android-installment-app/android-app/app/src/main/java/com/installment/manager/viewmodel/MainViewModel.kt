package com.installment.manager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.installment.manager.InstallmentApp
import com.installment.manager.data.model.Installment
import com.installment.manager.data.model.Payment
import com.installment.manager.notification.NotificationScheduler
import com.installment.manager.util.PersianDateUtil
import com.installment.manager.util.PreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = (application as InstallmentApp).database
    private val installmentDao = db.installmentDao()
    private val paymentDao = db.paymentDao()
    val preferencesManager = PreferencesManager(application)

    // UI State
    val allInstallments: StateFlow<List<Installment>> = installmentDao.getAllInstallments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeInstallments: StateFlow<List<Installment>> = installmentDao.getActiveInstallments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedInstallments: StateFlow<List<Installment>> = installmentDao.getCompletedInstallments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeCount: StateFlow<Int> = installmentDao.getActiveCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalRemainingAmount: StateFlow<Long> = installmentDao.getTotalRemainingAmount()
        .map { it ?: 0L }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val totalPaidAmount: StateFlow<Long> = installmentDao.getTotalPaidAmount()
        .map { it ?: 0L }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val overduePayments: StateFlow<List<Payment>> = paymentDao.getOverduePayments(System.currentTimeMillis())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val overdueCount: StateFlow<Int> = paymentDao.getOverdueCount(System.currentTimeMillis())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val upcomingPayments: StateFlow<List<Payment>> = paymentDao.getUpcomingPayments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isDarkMode: StateFlow<Boolean> = preferencesManager.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val currencyUnit: StateFlow<String> = preferencesManager.currencyUnit
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "toman")

    fun getInstallmentById(id: Long): Flow<Installment?> {
        return installmentDao.getInstallmentById(id)
    }

    fun getPaymentsForInstallment(installmentId: Long): Flow<List<Payment>> {
        return paymentDao.getPaymentsForInstallment(installmentId)
    }

    fun getPaymentsInRange(startMillis: Long, endMillis: Long): Flow<List<Payment>> {
        return paymentDao.getPaymentsInRange(startMillis, endMillis)
    }

    fun addInstallment(
        title: String,
        totalAmount: Long,
        installmentAmount: Long,
        totalCount: Int,
        category: String,
        dayOfMonth: Int,
        startYear: Int,
        startMonth: Int,
        note: String,
        notificationsEnabled: Boolean,
        reminderDaysBefore: Int
    ) {
        viewModelScope.launch {
            val startDateMillis = PersianDateUtil.toMillis(startYear, startMonth, dayOfMonth)

            val installment = Installment(
                title = title,
                totalAmount = totalAmount,
                installmentAmount = installmentAmount,
                totalCount = totalCount,
                category = category,
                dayOfMonth = dayOfMonth,
                startDateMillis = startDateMillis,
                note = note,
                notificationsEnabled = notificationsEnabled,
                reminderDaysBefore = reminderDaysBefore
            )

            val installmentId = installmentDao.insertInstallment(installment)

            // Generate all payment records
            val payments = mutableListOf<Payment>()
            for (i in 0 until totalCount) {
                val dueMillis = PersianDateUtil.addMonths(startDateMillis, i)
                payments.add(
                    Payment(
                        installmentId = installmentId,
                        paymentNumber = i + 1,
                        amount = installmentAmount,
                        dueDateMillis = dueMillis
                    )
                )
            }
            paymentDao.insertPayments(payments)

            // Schedule notifications
            if (notificationsEnabled) {
                for (payment in payments) {
                    NotificationScheduler.schedulePaymentReminder(
                        context = getApplication(),
                        installmentId = installmentId,
                        installmentTitle = title,
                        paymentNumber = payment.paymentNumber,
                        amount = payment.amount,
                        dueDateMillis = payment.dueDateMillis,
                        reminderDaysBefore = reminderDaysBefore
                    )
                }
            }
        }
    }

    fun togglePaymentPaid(payment: Payment, installment: Installment) {
        viewModelScope.launch {
            val newIsPaid = !payment.isPaid
            val paidDate = if (newIsPaid) System.currentTimeMillis() else null

            paymentDao.markPaymentPaid(payment.id, newIsPaid, paidDate)

            val newPaidCount = if (newIsPaid) installment.paidCount + 1 else installment.paidCount - 1
            val isCompleted = newPaidCount >= installment.totalCount

            installmentDao.updatePaymentProgress(installment.id, newPaidCount, isCompleted)

            // Update notifications
            if (newIsPaid) {
                NotificationScheduler.cancelPaymentReminder(
                    getApplication(),
                    installment.id,
                    payment.paymentNumber
                )
            } else if (installment.notificationsEnabled) {
                NotificationScheduler.schedulePaymentReminder(
                    context = getApplication(),
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

    fun deleteInstallment(installment: Installment) {
        viewModelScope.launch {
            NotificationScheduler.cancelAllRemindersForInstallment(
                getApplication(),
                installment.id,
                installment.totalCount
            )
            installmentDao.deleteInstallmentById(installment.id)
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setDarkMode(enabled) }
    }

    fun setCurrencyUnit(unit: String) {
        viewModelScope.launch { preferencesManager.setCurrencyUnit(unit) }
    }

    fun formatAmount(amount: Long, unit: String = "toman"): String {
        return if (unit == "rial") {
            PersianDateUtil.formatCurrencyRial(amount)
        } else {
            PersianDateUtil.formatCurrency(amount / 10)
        }
    }
}
