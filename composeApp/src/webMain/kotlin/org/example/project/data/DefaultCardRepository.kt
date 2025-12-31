package org.example.project.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.example.project.data.data_source.LocalCardDataSource
import org.example.project.data.settings.LocalCardSettings
import org.example.project.domain.model.card.Card
import org.example.project.domain.model.card.CardType
import org.example.project.toggle.Toggles

class DefaultCardRepository(
    private val localDataSource: LocalCardDataSource,
    private val localSettings: LocalCardSettings
) : CardRepository {

    private val cardsFlow get() = localDataSource.readCards().map { allCards ->
        val allDoneIds = localSettings.getDoneCardIds()
        if (Toggles.isFilterByDoneEnabled) {
            allCards.map { card ->
                val isDone = allDoneIds.contains(card.id)
                card.copy(isDone = isDone)
            }
        } else allCards
    }.onEach { allCards ->
        println("All cards:")
        println(allCards)
    }

    override fun readCards(): Flow<List<Card>> = cardsFlow

    override fun readCards(isDone: Boolean): Flow<List<Card>> = cardsFlow.map { allCards ->
        allCards.filter { it.isDone == isDone }
    }

    override fun readCardsByType(type: CardType): Flow<List<Card>> = cardsFlow.map { allCards ->
        allCards.filter { it.type == type }
    }

    override fun readCardsByType(type: CardType, isDone: Boolean): Flow<List<Card>> = cardsFlow.map { allCards ->
        allCards.filter { it.type == type && it.isDone == isDone }
    }

    override suspend fun updateCardIsDone(id: String) {
        localSettings.markCardAsDone(id)
    }

    override suspend fun resetProgress() = localSettings.clear()

}