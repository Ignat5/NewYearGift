package org.example.project.feature.statistics.components

import org.example.project.core.presentation.components.BaseState

sealed interface StatisticsState : BaseState {

    data class Data(
        val progress: Float,
        val allCardsCount: Int,
        val doneCardsCount: Int
    ) : StatisticsState

    data object Init : StatisticsState
}