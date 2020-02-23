package com.tdl.recipee.ui.base.redux

/**
 * Created by loc.ta on 2/22/2020.
 */
class Store<State, Action : Any> private constructor(private val reducer: Reducer<Action, State>, private val initialState: State) : Dispatcher<Action, State> {

    companion object {
        fun <State, Action : Any> create(reducer: Reducer<Action, State>, initialState: State) = Store(reducer, initialState)
    }

    @Volatile
    var state: State = initialState
        get() = if (field != null) field else initialState

    @Suppress("UNCHECKED_CAST")
    fun <State> getCurrentState(): State? = state as? State

    private val dispatcher: Dispatcher<Action, State>

    interface Reducer<Action, State> {
        fun reduce(action: Action, currentState: State): State
    }

    init {
        dispatcher = object : Dispatcher<Action, State> {
            override fun dispatch(action: Action): State = dispatchAction(action)
        }
    }

    private fun dispatchAction(action: Action?): State {
        synchronized(this) {
            state = if (action == null) initialState else reducer.reduce(action, state)
        }
        return state
    }

    override fun dispatch(action: Action): State = dispatcher.dispatch(action)
}
