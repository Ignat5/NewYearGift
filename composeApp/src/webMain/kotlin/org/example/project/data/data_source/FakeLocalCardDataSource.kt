package org.example.project.data.data_source

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.example.project.domain.model.card.Card
import org.example.project.domain.model.card.CardType

class FakeLocalCardDataSource : LocalCardDataSource {

    private val settings = Settings()

    private val allCardsState = MutableStateFlow(getInitCards())

    override fun readCards(): Flow<List<Card>> = allCardsState.map { allCards ->
        allCards.map { card ->
            card.copy(isDone = settings.getBoolean(card.id, false))
        }
    }

    override fun readCardById(id: String): Flow<Card?> = allCardsState.map { allCards ->
        allCards.find { it.id == id }
    }

    override suspend fun updateCard(card: Card) {
        val oldCards = allCardsState.value
        val newCards = buildList {
            addAll(oldCards)
            set(indexOfFirst { it.id == card.id }, card)
        }
        allCardsState.update { newCards }
    }

    suspend fun markCardAsDone(id: String) {
        settings.putBoolean(id, true)
    }

    private fun getInitCards(): List<Card> {
        return buildList {
            addAll(
                listOf(
                    Card(
                        id = "1",
                        type = CardType.Question,
                        content = "О чем ты думаешь, когда надолго остаешься одна / один?",
                        isDone = false
                    ),
                    Card(
                        id = "2",
                        type = CardType.Question,
                        content = "Что для тебя ценее всего в людях?",
                        isDone = false
                    ),
                    Card(
                        id = "3",
                        type = CardType.Question,
                        content = "В каком возрасте ты планируешь завести семью?",
                        isDone = false
                    ),
                    Card(
                        id = "4",
                        type = CardType.Action,
                        content = "Крепко обниметесь в полной тишине - 1 минута",
                        isDone = false
                    ),
                    Card(
                        id = "5",
                        type = CardType.Quiz,
                        content = "Какой любимый цвет вашего партнера?",
                        isDone = false
                    ),
                )
            )
        }
    }

}