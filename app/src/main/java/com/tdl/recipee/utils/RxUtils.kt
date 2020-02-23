package com.tdl.recipee.utils

import com.tdl.recipee.BuildConfig
import com.tdl.recipee.ui.base.mvi.NavigationAction
import com.tdl.recipee.ui.base.mvi.Navigator
import com.tdl.recipee.ui.base.redux.Store
import io.reactivex.functions.BiFunction

/**
 * Created by loc.ta on 2/22/2020.
 */
object RxUtils {

    fun <S, A : Any> dispatchAction(store: Store<S, A>, navigator: Navigator<S>? = null) =
        BiFunction<S, A, S> { _, action ->
            return@BiFunction store.dispatch(action).also {
                if (action is NavigationAction) {
                    navigator?.executeNavigationAction(action, it)
                }
                if (BuildConfig.DEBUG) {
                    println("Mvi: Dispatching $action with view state $it")
                }
            }
        }
}