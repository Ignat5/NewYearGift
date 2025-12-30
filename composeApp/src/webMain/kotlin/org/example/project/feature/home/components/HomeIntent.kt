package org.example.project.feature.home.components

import org.example.project.core.presentation.components.BaseIntent
import org.example.project.feature.home.model.CardFilterType

sealed interface HomeIntent : BaseIntent {

    data object OnNextClick : HomeIntent

    data object OnPickCardFilterTypeClick : HomeIntent

    data class OnConfirmCardFilterType(val type: CardFilterType) : HomeIntent

    data object OnDismissDialogRequest : HomeIntent

}