package org.example.project.data

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.card.Card
import org.example.project.domain.model.card.CardType

interface CardRepository {

    fun readCards(isDone: Boolean): Flow<List<Card>>
    fun readCardsByType(type: CardType): Flow<List<Card>>
    fun readCardsByType(type: CardType, isDone: Boolean): Flow<List<Card>>

}