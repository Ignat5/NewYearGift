package org.example.project.feature.home.components

import org.example.project.core.presentation.components.BaseSideEffect

sealed interface HomeSideEffect : BaseSideEffect {
    sealed interface Navigation : HomeSideEffect {
        data object Statistics : Navigation
    }
}