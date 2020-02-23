package com.tdl.recipee.ui.list

import com.tdl.recipee.extension.plusAssign
import com.tdl.recipee.ui.base.mvi.BaseMviPresenter
import com.tdl.recipee.ui.base.redux.Store
import com.tdl.recipee.ui.list.reducer.RecipeListAction
import com.tdl.recipee.ui.list.reducer.RecipeListReducer
import com.tdl.recipee.ui.list.reducer.RecipeListViewState
import com.tdl.recipee.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.*

/**
 * Created by loc.ta on 2/22/2020.
 */
class RecipeListPresenter(
    private val interactor: RecipeListInteractor,
    reducer: RecipeListReducer
) : BaseMviPresenter<RecipeListView, RecipeListViewState, RecipeListAction>() {

    init {
        store = Store.create(reducer, RecipeListViewState.INITIAL_STATE)
    }

    override fun bind(view: RecipeListView) {

        val recipes = view.loadInitialIntent()
            .flatMap { interactor.loadRecipes() }

        val delete = view.deleteRecipeIntent()
            .flatMap { interactor.deleteRecipe(it) }

        val reload = view.reloadIntent()
            .flatMap { interactor.reloadRecipes() }

        val search = view.searchIntent()
            .flatMap { keyword ->
                Observable.just(store.state)
                    .flatMap {
                        interactor.searchRecipe(
                            keyword.toLowerCase(Locale.getDefault()).trim(),
                            it.recipes.orEmpty()
                        )
                    }
            }

        val combinedState: Observable<RecipeListViewState> = Observable
            .merge(recipes, delete, reload, search)
            .scan(store.state, RxUtils.dispatchAction(store, navigator))
            .observeOn(AndroidSchedulers.mainThread())

        disposable += combinedState.subscribe({ view.render(it) }, { Timber.e(it) })
    }
}