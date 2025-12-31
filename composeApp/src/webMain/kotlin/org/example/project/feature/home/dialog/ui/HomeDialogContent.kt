package org.example.project.feature.home.dialog.ui

import androidx.compose.runtime.Composable
import org.example.project.feature.home.dialog.model.HomeDialogState
import org.example.project.feature.home.model.CardFilterType

@Composable
fun HomeDialogContent(
    dialogState: HomeDialogState,
    onConfirmCardType: (CardFilterType) -> Unit,
    onDismissRequest: () -> Unit
) {
    when (dialogState) {
        is HomeDialogState.PickCardType -> OnPickCardType(
            dialogState = dialogState,
            onConfirm = onConfirmCardType,
            onDismissRequest = onDismissRequest
        )
        is HomeDialogState.Init -> Unit
    }
}

@Composable
private fun OnPickCardType(
    dialogState: HomeDialogState.PickCardType,
    onConfirm: (CardFilterType) -> Unit,
    onDismissRequest: () -> Unit
) {
    PickCardTypeDialog(
        currentType = dialogState.currentType,
        onConfirm = onConfirm,
        onDismissRequest = onDismissRequest
    )
}