package org.example.project.feature.home.dialog.model

import org.example.project.feature.home.model.CardFilterType

sealed interface HomeDialogState {
    data class PickCardType(
        val currentType: CardFilterType
    ) : HomeDialogState

    data object Init : HomeDialogState
}