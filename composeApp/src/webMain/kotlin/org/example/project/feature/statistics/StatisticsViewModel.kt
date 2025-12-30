package org.example.project.feature.statistics

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.core.presentation.BaseViewModel
import org.example.project.domain.use_case.CardUseCase
import org.example.project.feature.statistics.components.StatisticsIntent
import org.example.project.feature.statistics.components.StatisticsSideEffect
import org.example.project.feature.statistics.components.StatisticsState

class StatisticsViewModel(
    private val cardUseCase: CardUseCase
) : BaseViewModel<StatisticsState, StatisticsIntent, StatisticsSideEffect>() {

    private val isResetState = MutableStateFlow(false)

    private val allCardsState = readCardsState()
    private val allCardsFlow = allCardsState.filterNotNull()

    override val uiState: StateFlow<StatisticsState> = allCardsFlow.map { allCards ->
        val allCardsCount = allCards.size
        val doneCardsCount = allCards.count { it.isDone }
        StatisticsState.Data(
            progress = doneCardsCount / allCardsCount.toFloat(),
            allCardsCount = allCardsCount,
            doneCardsCount = doneCardsCount
        ).also(::println)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        StatisticsState.Init
    )

    override fun onIntent(intent: StatisticsIntent) {
        when (intent) {
            is StatisticsIntent.OnResetProgressClick -> onResetProgressClick()
        }
    }

    private fun onResetProgressClick() {
        viewModelScope.launch {
            cardUseCase.resetProgress()
            isResetState.update { true }
        }
    }

    private fun readCardsState() = isResetState.flatMapLatest {
        cardUseCase.readCards()
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                null
            )
    }

}