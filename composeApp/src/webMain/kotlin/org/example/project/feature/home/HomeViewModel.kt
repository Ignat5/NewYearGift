package org.example.project.feature.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.core.presentation.BaseViewModel
import org.example.project.domain.model.card.CardType
import org.example.project.domain.use_case.CardUseCase
import org.example.project.feature.home.components.HomeIntent
import org.example.project.feature.home.components.HomeSideEffect
import org.example.project.feature.home.components.HomeState
import org.example.project.feature.home.dialog.model.HomeDialogState
import org.example.project.feature.home.model.CardFilterType

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val cardUseCase: CardUseCase
) : BaseViewModel<HomeState, HomeIntent, HomeSideEffect>() {

    private val currentPageIndexState = MutableStateFlow(0)
    private val currentFilterTypeState = MutableStateFlow(CardFilterType.All)
    private val cardsState = readCardsState()
    private val cardsFlow = cardsState.filterNotNull()
    private val uiDialogState = MutableStateFlow<HomeDialogState>(HomeDialogState.Init)

    override val uiState: StateFlow<HomeState> = combine(
        currentPageIndexState,
        currentFilterTypeState,
        cardsFlow,
        uiDialogState
    ) { currentPageIndex, currentFilterType, cards, dialogState ->
        HomeState.Data(
            currentPageIndex = currentPageIndex,
            currentFilterType = currentFilterType,
            cards = cards,
            dialogState = dialogState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        HomeState.Init
    )

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OnNextClick -> onNextClick()
            is HomeIntent.OnPickCardFilterTypeClick -> onPickCardFilterTypeClick()
            is HomeIntent.OnStatisticsClick -> onStatisticsClick()
            is HomeIntent.OnConfirmCardFilterType -> onConfirmCardFilterType(intent)
            is HomeIntent.OnDismissDialogRequest -> onDismissDialogRequest()
        }
    }

    private fun onNextClick() {
        val allCards = cardsState.value ?: return
        val currentCard = allCards[currentPageIndexState.value]
        viewModelScope.launch { cardUseCase.markCardAsDone(currentCard.id) }
        currentPageIndexState.update { currentPageIndex ->
            (currentPageIndex + 1).coerceAtMost(allCards.lastIndex)
        }
    }

    private fun onPickCardFilterTypeClick() {
        uiDialogState.update {
            HomeDialogState.PickCardType(currentType = currentFilterTypeState.value)
        }
    }

    private fun onStatisticsClick() {
        sendSideEffect(HomeSideEffect.Navigation.Statistics)
    }

    private fun onConfirmCardFilterType(intent: HomeIntent.OnConfirmCardFilterType) {
        currentFilterTypeState.update { intent.type }
        resetDialogState()
    }

    private fun onDismissDialogRequest() = resetDialogState()

    private fun resetDialogState() = uiDialogState.update { HomeDialogState.Init }

    private fun readCardsState() = currentFilterTypeState.flatMapLatest { filterType ->
        val type = when (filterType) {
            CardFilterType.Question -> CardType.Question
            CardFilterType.Action -> CardType.Action
            CardFilterType.Quiz -> CardType.Quiz
            else -> null
        }
        type?.let {
            cardUseCase.readCardsByType(type = type, isDone = false, isRandom = true)
        } ?: cardUseCase.readCards(isDone = false, isRandom = true)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )

}