package org.example.project.feature.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import org.example.project.core.presentation.BaseViewModel
import org.example.project.domain.model.card.CardType
import org.example.project.domain.use_case.CardUseCase
import org.example.project.feature.home.components.HomeIntent
import org.example.project.feature.home.components.HomeSideEffect
import org.example.project.feature.home.components.HomeState
import org.example.project.feature.home.model.CardFilterType

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val cardUseCase: CardUseCase
) : BaseViewModel<HomeState, HomeIntent, HomeSideEffect>() {

    private val currentFilterTypeState = MutableStateFlow(CardFilterType.Question)
    private val cardsFlow = readCards()

    override val uiState: StateFlow<HomeState> = combine(
        currentFilterTypeState,
        cardsFlow
    ) { currentFilterType, cards ->
        HomeState.Data(
            currentFilterType = currentFilterType,
            cards = cards
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        HomeState.Init
    )

    override fun onIntent(intent: HomeIntent) {

    }

    private fun readCards() = currentFilterTypeState.flatMapLatest { filterType ->
        val type = when (filterType) {
            CardFilterType.Question -> CardType.Question
            CardFilterType.Action -> CardType.Action
            CardFilterType.Quiz -> CardType.Quiz
            else -> null
        }
        type?.let {
            cardUseCase.readCardsByType(type = type, isDone = false, isRandom = true)
        } ?: cardUseCase.readCards(isDone = false, isRandom = true)
    }

}