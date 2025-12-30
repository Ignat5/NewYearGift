package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.feature.home.HomeScreen
import org.example.project.feature.statistics.StatisticsScreen
import org.example.project.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            RootNavigation()
        }
    }
}

@Composable
private fun RootNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppRoute.Home.route,
        modifier = Modifier
            .sizeIn(maxWidth = 412.dp, maxHeight = 915.dp)
            .fillMaxSize()
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp)),
    ) {
        composable(route = AppRoute.Home.route) {
            HomeScreen(
                onNavigateToStatistics = {
                    navController.navigate(AppRoute.Statistics.route)
                }
            )
        }
        composable(route = AppRoute.Statistics.route) {
            StatisticsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

enum class AppRoute {
    Home,
    Statistics;

    val route get() = this.name
}