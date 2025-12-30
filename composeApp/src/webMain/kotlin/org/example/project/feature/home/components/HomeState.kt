package org.example.project.feature.home.components

import androidx.compose.runtime.Stable
import org.example.project.core.presentation.components.BaseState
import org.example.project.domain.model.card.Card
import org.example.project.feature.home.dialog.model.HomeDialogState
import org.example.project.feature.home.model.CardFilterType

@Stable
sealed interface HomeState : BaseState {

    @Stable
    data class Data(
        val currentFilterType: CardFilterType,
        val cards: List<Card>,
        val dialogState: HomeDialogState,
        val logs: String
    ) : HomeState

    @Stable
    data object Init : HomeState
}