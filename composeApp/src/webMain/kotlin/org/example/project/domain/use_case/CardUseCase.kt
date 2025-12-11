package org.example.project.domain.use_case

import kotlinx.coroutines.flow.map
import org.example.project.data.CardRepository
import org.example.project.domain.model.card.CardType

class CardUseCase(private val repository: CardRepository) {

    fun readCards(
        isDone: Boolean,
        isRandom: Boolean
    ) = repository.readCards(isDone).map { allCards ->
        if (isRandom) allCards.shuffled() else allCards
    }

    fun readCardsByType(
        type: CardType,
        isDone: Boolean,
        isRandom: Boolean
    ) = repository.readCardsByType(type, isDone).map { allCards ->
        if (isRandom) allCards.shuffled() else allCards
    }

}