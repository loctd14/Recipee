package com.tdl.recipee.ui.base.redux

/**
 * Created by loc.ta on 2/22/2020.
 */
interface Dispatcher<Action, State> {
    fun dispatch(action: Action): State
}