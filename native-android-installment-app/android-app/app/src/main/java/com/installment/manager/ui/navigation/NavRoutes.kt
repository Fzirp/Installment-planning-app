package com.installment.manager.ui.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object AddInstallment : NavRoutes("add_installment")
    object InstallmentDetail : NavRoutes("installment_detail/{installmentId}") {
        fun createRoute(installmentId: Long) = "installment_detail/$installmentId"
    }
    object Calendar : NavRoutes("calendar")
    object Statistics : NavRoutes("statistics")
    object Settings : NavRoutes("settings")
}
