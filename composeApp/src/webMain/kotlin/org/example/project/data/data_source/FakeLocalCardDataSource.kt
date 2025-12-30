package org.example.project.data.data_source

import androidx.compose.runtime.key
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toSuspendSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.example.project.domain.model.card.Card
import org.example.project.domain.model.card.CardType

@OptIn(ExperimentalSettingsApi::class)
class FakeLocalCardDataSource : LocalCardDataSource {

    private val settings = Settings().toSuspendSettings()

    private val allCardsState = MutableStateFlow(getInitCards()).map { allCards ->
        allCards.map { card ->
            val key = card.id
            val isDone = settings.getBooleanOrNull(key)
            println("$key -> $isDone")
            Card(
                id = card.id,
                type = card.type,
                content = card.content,
                isDone = isDone ?: false
            )
        }
    }

    override fun readCards(): Flow<List<Card>> = allCardsState.onEach { allCards ->
        println("All cards...")
        allCards.forEach(::println)
        println("\n")
        println("Printing out keys...")
        settings.keys().forEach { key ->
            println("$key -> ${settings.getBooleanOrNull(key)}")
        }
    }

    override fun readCardById(id: String): Flow<Card?> = allCardsState.map { allCards ->
        allCards.find { it.id == id }
    }

    override suspend fun updateCardIsDone(id: String, isDone: Boolean) {
        settings.putBoolean(id, isDone)
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