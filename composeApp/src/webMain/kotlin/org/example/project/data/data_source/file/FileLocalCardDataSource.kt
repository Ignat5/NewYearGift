package org.example.project.data.data_source.file

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import newyeargift.composeapp.generated.resources.Res
import org.example.project.data.data_source.LocalCardDataSource
import org.example.project.domain.model.card.Card
import kotlin.text.decodeToString

class FileLocalCardDataSource : LocalCardDataSource {

    private val json = Json { prettyPrint = true }

    override fun readCards(): Flow<List<Card>> = flow {
        emit(getInitCards())
    }

    private suspend fun getInitCards(): List<Card> {
        val dataByteArray = Res.readBytes("files/data.json")
        val jsonStr = dataByteArray.decodeToString()
        return json.decodeFromString<List<Card>>(jsonStr)
    }

}