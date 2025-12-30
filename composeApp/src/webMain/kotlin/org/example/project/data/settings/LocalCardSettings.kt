package org.example.project.data.settings

interface LocalCardSettings {
    suspend fun getDoneCardIds(): Set<String>
    suspend fun markCardAsDone(id: String)
    suspend fun clear()
}