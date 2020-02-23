package com.tdl.recipee.ui.base.mvi

/**
 * Created by loc.ta on 2/22/2020.
 */
interface Navigator<State> {

    fun executeNavigationAction(action: NavigationAction, state: State)
}