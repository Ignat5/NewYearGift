package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource

import newyeargift.composeapp.generated.resources.Res
import newyeargift.composeapp.generated.resources.compose_multiplatform
import org.example.project.feature.home.HomeScreen

@Composable
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
            ,
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
    ) {
        composable(route = AppRoute.Home.route) {
            HomeScreen()
        }
    }
}

enum class AppRoute {
    Home;

    val route get() = this.name
}