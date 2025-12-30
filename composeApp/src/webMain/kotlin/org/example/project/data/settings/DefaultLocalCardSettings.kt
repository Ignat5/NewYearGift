package org.example.project.data.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toSuspendSettings
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSettingsApi::class)
class DefaultLocalCardSettings : LocalCardSettings {

    private val settings = Settings().toSuspendSettings()
    private val json = Json { prettyPrint = true }

    override suspend fun getDoneCardIds(): Set<String> = getDoneIdsSet().also {
        println("Done card ids:")
        println(it)
    }

    override suspend fun markCardAsDone(id: String) {
        val jsonStr = buildSet {
            addAll(getDoneIdsSet())
            add(id)
        }.let { json.encodeToString(it) }
        settings.putString(DONE_SET_KEY, jsonStr)
    }

    override suspend fun clear() = settings.clear()

    private suspend fun getDoneIdsSet(): Set<String> {
        return settings.getStringOrNull(DONE_SET_KEY)?.let { jsonStr ->
            json.decodeFromString<Set<String>>(jsonStr)
        } ?: emptySet()
    }

    private companion object Companion {
        private const val DONE_SET_KEY = "DONE_SET_KEY"
    }

}