package com.edosoft.vertexengine.editor.store.type

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface BaseState

interface BaseAction

abstract class BaseStore<StateType: BaseState, ActionType: BaseAction>(initialState: StateType) {

    var state by mutableStateOf(initialState)
        protected set

    abstract fun onAction(action: ActionType)

    protected fun setState(reducer: StateType.() -> StateType) {

        state = state.reducer()
    }
}