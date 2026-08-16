package com.installment.manager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.installment.manager.ui.screens.*
import com.installment.manager.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: MainViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate(NavRoutes.AddInstallment.route) },
                onInstallmentClick = { id ->
                    navController.navigate(NavRoutes.InstallmentDetail.createRoute(id))
                },
                onCalendarClick = { navController.navigate(NavRoutes.Calendar.route) },
                onStatisticsClick = { navController.navigate(NavRoutes.Statistics.route) },
                onSettingsClick = { navController.navigate(NavRoutes.Settings.route) }
            )
        }

        composable(NavRoutes.AddInstallment.route) {
            AddInstallmentScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.InstallmentDetail.route,
            arguments = listOf(
                navArgument("installmentId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val installmentId = backStackEntry.arguments?.getLong("installmentId") ?: 0L
            InstallmentDetailScreen(
                viewModel = viewModel,
                installmentId = installmentId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Calendar.route) {
            CalendarScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onInstallmentClick = { id ->
                    navController.navigate(NavRoutes.InstallmentDetail.createRoute(id))
                }
            )
        }

        composable(NavRoutes.Statistics.route) {
            StatisticsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Settings.route) {
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
