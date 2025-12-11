package org.example.project.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.example.project.data.data_source.LocalCardDataSource
import org.example.project.domain.model.card.Card
import org.example.project.domain.model.card.CardType

class FakeCardRepository(
    private val localDataSource: LocalCardDataSource
) : CardRepository {

    private val cardsFlow get() = localDataSource.readCards()

    private fun readCards(): Flow<List<Card>> = cardsFlow
    override fun readCards(isDone: Boolean): Flow<List<Card>> = cardsFlow.map { allCards ->
        allCards.filter { it.isDone == isDone }
    }

    override fun readCardsByType(type: CardType): Flow<List<Card>> = cardsFlow.map { allCards ->
        allCards.filter { it.type == type }
    }

    override fun readCardsByType(type: CardType, isDone: Boolean): Flow<List<Card>> = cardsFlow.map { allCards ->
        allCards.filter { it.type == type && it.isDone == isDone }
    }

    suspend fun updateCardIsDone(id: String, isDone: Boolean) {
        val card = localDataSource.readCardById(id).firstOrNull() ?: return
        localDataSource.updateCard(card.copy(isDone = isDone))
    }

}