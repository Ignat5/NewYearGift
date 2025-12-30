package org.example.project.domain.use_case

import kotlinx.coroutines.flow.map
import org.example.project.data.CardRepository
import org.example.project.domain.model.card.CardType
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CardUseCase(private val repository: CardRepository) {

    private val random get() = Random(Clock.System.now().toEpochMilliseconds())

    fun readCards(
        isDone: Boolean,
        isRandom: Boolean
    ) = repository.readCards(isDone).map { allCards ->
        if (isRandom) allCards.shuffled(random) else allCards
    }

    fun readCardsByType(
        type: CardType,
        isDone: Boolean,
        isRandom: Boolean
    ) = repository.readCardsByType(type, isDone).map { allCards ->
        if (isRandom) allCards.shuffled(random) else allCards
    }

    suspend fun markCardAsDone(id: String) {
        repository.updateCardIsDone(id = id)
    }

}