package org.example.project.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.example.project.data.FakeCardRepository
import org.example.project.data.data_source.FakeLocalCardDataSource
import org.example.project.domain.model.card.Card
import org.example.project.domain.use_case.CardUseCase
import org.example.project.feature.home.components.HomeIntent
import org.example.project.feature.home.components.HomeState

@Composable
fun HomeScreen() {
    val viewModel = viewModel {
        HomeViewModel(
            cardUseCase = CardUseCase(
                repository = FakeCardRepository(
                    localDataSource = FakeLocalCardDataSource()
                )
            )
        )
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenStateless(
        uiState = uiState,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun HomeScreenStateless(
    uiState: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar()
        },
//        containerColor = ,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp))
    ) { innerPadding ->
        if (uiState !is HomeState.Data) return@Scaffold
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeContent(
                uiState = uiState,
                onIntent = onIntent,
            )
        }
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
            beyondViewportPageCount = 0,
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
                    .padding(vertical = 16.dp)
                ,
                onClick = {
                    val lastIndex = pagerState.pageCount - 1
                    val nextPageIndex = (pagerState.currentPage + 1).coerceAtMost(lastIndex)
                    scope.launch {
                        pagerState.animateScrollToPage(nextPageIndex)
                    }
                }
            ) {
                Text(
                    text = "Дальше",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
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
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(fraction = 0.75f)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = card.type.name,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = card.content,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar() {
    TopAppBar(
        title = { Text("E&I") }
    )
}