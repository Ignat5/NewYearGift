package org.example.project.core.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.example.project.core.presentation.components.BaseIntent
import org.example.project.core.presentation.components.BaseSideEffect
import org.example.project.core.presentation.components.BaseState

abstract class BaseViewModel<State : BaseState, Intent : BaseIntent, SideEffect : BaseSideEffect> : ViewModel() {

    abstract val uiState: StateFlow<State>
    abstract fun onIntent(intent: Intent)

    private val sideEffectState: MutableStateFlow<SideEffect?> = MutableStateFlow(null)

    protected fun sendSideEffect(sideEffect: SideEffect) = sideEffectState.update { sideEffect }
    fun onSideEffectHandled() = sideEffectState.update { null }
}