package org.example.project.feature.home.dialog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import org.example.project.feature.home.model.CardFilterType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickCardTypeDialog(
    currentType: CardFilterType,
    onConfirm: (CardFilterType) -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Card {
            Box(modifier = Modifier.padding(vertical = 24.dp)) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Выбери тип",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    val allItems = remember { CardFilterType.entries }
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(
                            items = allItems,
                            key = { it.ordinal }
                        ) { item ->
                            val isCurrent = item == currentType
                            ItemType(
                                isCurrent = isCurrent,
                                item = item,
                                onClick = {
                                    onConfirm(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemType(
    isCurrent: Boolean,
    item: CardFilterType,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (isCurrent) 1f else 0.5f)
            )
        }
    }
}