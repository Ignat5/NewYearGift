package org.example.project.feature.statistics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import newyeargift.composeapp.generated.resources.Res
import newyeargift.composeapp.generated.resources.ic_back
import newyeargift.composeapp.generated.resources.ic_more
import org.example.project.Strings
import org.example.project.data.DefaultCardRepository
import org.example.project.data.data_source.file.FileLocalCardDataSource
import org.example.project.data.settings.DefaultLocalCardSettings
import org.example.project.domain.use_case.CardUseCase
import org.example.project.feature.statistics.components.StatisticsIntent
import org.example.project.feature.statistics.components.StatisticsState

@Composable
fun StatisticsScreen(
    onBackClick: () -> Unit
) {
    val viewModel = viewModel {
        StatisticsViewModel(
            cardUseCase = CardUseCase(
                repository = DefaultCardRepository(
                    localSettings = DefaultLocalCardSettings(),
                    localDataSource = FileLocalCardDataSource()
                )
            )
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StatisticsContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onBackClick = onBackClick
    )
}

@Composable
private fun StatisticsContent(
    uiState: StatisticsState,
    onIntent: (StatisticsIntent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            StatisticsTopBar(
                onResetProgressClick = {
                    onIntent(StatisticsIntent.OnResetProgressClick)
                },
                onBackClick = onBackClick
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        if (uiState !is StatisticsState.Data) return@Scaffold
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { uiState.progress },
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Пройдено ${uiState.doneCardsCount} карточек из ${uiState.allCardsCount}!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsTopBar(
    onResetProgressClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text("Статистика") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = {
                expanded = !expanded
            }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_more),
                    contentDescription = null
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ActionType.entries.forEach { action ->
                        val text = action.toDisplayText()
                        DropdownMenuItem(
                            onClick = {
                                when (action) {
                                    ActionType.ResetProgress -> onResetProgressClick()
                                }
                                expanded = false
                            },
                            text = {
                                Text(text = text)
                            }
                        )
                    }
                }
            }
        }
    )
}

private fun ActionType.toDisplayText() = when (this) {
    ActionType.ResetProgress -> Strings.RESET_PROGRESS
}

private enum class ActionType {
    ResetProgress
}