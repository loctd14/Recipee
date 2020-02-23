package com.tdl.recipee.ui.base.mvi

import androidx.annotation.CallSuper
import com.tdl.recipee.ui.base.redux.Store
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by loc.ta on 2/22/2020.
 */
abstract class BaseMviPresenter<V : MviView, VS, A : Any> : MviPresenter<V> {
    protected val disposable by lazy(LazyThreadSafetyMode.NONE) { CompositeDisposable() }
    protected lateinit var store: Store<VS, A>
    var navigator: Navigator<VS>? = null

    @CallSuper
    override fun onPause() {
        disposable.clear()
    }

    @CallSuper
    override fun unbind() {
        navigator = null
        disposable.dispose()
    }
}