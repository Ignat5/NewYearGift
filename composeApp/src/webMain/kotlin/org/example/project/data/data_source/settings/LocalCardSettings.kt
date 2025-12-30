package org.example.project.data.data_source.settings

interface LocalCardSettings {
    suspend fun getDoneCardIds(): Set<String>
    suspend fun markCardAsDone(id: String)
    suspend fun clear()
}