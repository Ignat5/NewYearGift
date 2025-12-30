package org.example.project.feature.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import newyeargift.composeapp.generated.resources.MarckScript
import org.example.project.data.DefaultCardRepository
import org.example.project.data.data_source.fake.FakeLocalCardDataSource
import org.example.project.domain.model.card.Card
import org.example.project.domain.use_case.CardUseCase
import org.example.project.feature.home.components.HomeIntent
import org.example.project.feature.home.components.HomeState
import newyeargift.composeapp.generated.resources.Res
import newyeargift.composeapp.generated.resources.ShantellSans
import org.jetbrains.compose.resources.painterResource
import newyeargift.composeapp.generated.resources.ic_menu
import newyeargift.composeapp.generated.resources.ic_more
import org.example.project.Strings
import org.example.project.data.data_source.file.FileLocalCardDataSource
import org.example.project.data.settings.DefaultLocalCardSettings
import org.example.project.domain.model.card.CardType
import org.example.project.feature.home.components.HomeSideEffect
import org.example.project.feature.home.dialog.ui.HomeDialogContent
import org.example.project.feature.home.model.CardFilterType
import org.jetbrains.compose.resources.Font
import kotlin.random.Random

@Composable
fun HomeScreen(
    onNavigateToStatistics: () -> Unit
) {
    val viewModel = viewModel {
        HomeViewModel(
            cardUseCase = CardUseCase(
                repository = DefaultCardRepository(
                    localDataSource = FileLocalCardDataSource(),
                    localSettings = DefaultLocalCardSettings()
                )
            )
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenStateless(
        uiState = uiState,
        onIntent = viewModel::onIntent
    )
    val sideEffectState by viewModel.sideEffectState.collectAsStateWithLifecycle()
    HandleSideEffects(
        sideEffectState = sideEffectState,
        onSideEffectHandled = viewModel::onSideEffectHandled,
        onNavigateToStatistics = onNavigateToStatistics
    )
}

@Composable
private fun HomeScreenStateless(
    uiState: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                onPickCardTypeClick = {
                    onIntent(HomeIntent.OnPickCardFilterTypeClick)
                },
                onStatisticsClick = {
                    onIntent(HomeIntent.OnStatisticsClick)
                }
            )
        },
        modifier = Modifier
    ) { innerPadding ->
        if (uiState !is HomeState.Data) return@Scaffold
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeContent(
                uiState = uiState,
                onIntent = onIntent,
            )
        }
        HomeDialogContent(
            dialogState = uiState.dialogState,
            onConfirmCardType = {
                onIntent(HomeIntent.OnConfirmCardFilterType(it))
            },
            onDismissRequest = {
                onIntent(HomeIntent.OnDismissDialogRequest)
            }
        )
    }
}

@Composable
private fun HomeContent(
    uiState: HomeState.Data,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { uiState.cards.size })
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            userScrollEnabled = false,
            modifier = modifier,
        ) { pageIndex ->
            Box(modifier = Modifier.fillMaxWidth()) {
                ItemCard(
                    card = uiState.cards[pageIndex],
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Box(modifier = Modifier.weight(1f)) {
            Button(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(fraction = 0.75f)
                    .padding(vertical = 16.dp),
                onClick = {
                    onIntent(HomeIntent.OnNextClick)
                }
            ) {
                Text(
                    text = "Дальше",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily(
                            Font(Res.font.ShantellSans)
                        )
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
        LaunchedEffect(uiState.currentPageIndex) {
            if (pagerState.currentPage != uiState.currentPageIndex) {
                pagerState.animateScrollToPage(uiState.currentPageIndex)
            }
        }
    }
}

@Composable
private fun ItemCard(
    card: Card,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(fraction = 0.75f)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            val typeText = remember(card) { card.type.toDisplayText() }
            Text(
                text = typeText,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = FontFamily(
                        Font(Res.font.ShantellSans)
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = card.content,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily(
                        Font(Res.font.MarckScript)
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    onPickCardTypeClick: () -> Unit,
    onStatisticsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "EI",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily(
                        Font(Res.font.ShantellSans)
                    )
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(Res.drawable.ic_menu),
                    contentDescription = null
                )
            }
        },
        actions = {
            var isDropdownExpanded by remember { mutableStateOf(false) }
            IconButton(onClick = {
                isDropdownExpanded = !isDropdownExpanded
            }) {
                Box {
                    Icon(
                        painter = painterResource(Res.drawable.ic_more),
                        contentDescription = null
                    )
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        val allItems = ItemMenu.entries
                        allItems.forEach { item ->
                            val text = remember(item) { item.toDisplayText() }
                            DropdownMenuItem(
                                text = { Text(text = text) },
                                onClick = {
                                    when (item) {
                                        ItemMenu.PickCardType -> onPickCardTypeClick()
                                        ItemMenu.Statistics -> onStatisticsClick()
                                    }
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun HandleSideEffects(
    sideEffectState: HomeSideEffect?,
    onSideEffectHandled: () -> Unit,
    onNavigateToStatistics: () -> Unit
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(sideEffectState) {
        sideEffectState?.let {
            scope.launch {
                when (sideEffectState) {
                    is HomeSideEffect.Navigation.Statistics -> onNavigateToStatistics()
                }
            }
            onSideEffectHandled()
        }
    }
}

private fun CardType.toDisplayText() = when (this) {
    CardType.Question -> Strings.QUESTIONS
    CardType.Action -> Strings.ACTIONS
    CardType.Quiz -> Strings.QUIZ
}

private enum class ItemMenu {
    PickCardType,
    Statistics
}

private fun ItemMenu.toDisplayText(): String = when(this) {
    ItemMenu.PickCardType -> "Выбрать тип"
    ItemMenu.Statistics -> "Статистика"
}