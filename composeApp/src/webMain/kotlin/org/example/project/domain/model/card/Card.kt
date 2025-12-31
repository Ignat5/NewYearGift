package org.example.project.domain.model.card

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val id: String,
    val type: CardType,
    val content: String,
    val isDone: Boolean
)