package org.example.project.feature.statistics.components

import org.example.project.core.presentation.components.BaseIntent

sealed interface StatisticsIntent : BaseIntent {
    data object OnResetProgressClick : StatisticsIntent
}