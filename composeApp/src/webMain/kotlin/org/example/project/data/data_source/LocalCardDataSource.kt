package org.example.project.data.data_source

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.card.Card

interface LocalCardDataSource {

    fun readCards(): Flow<List<Card>>
    fun readCardById(id: String): Flow<Card?>

}