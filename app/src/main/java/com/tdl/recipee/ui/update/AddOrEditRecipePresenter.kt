package com.tdl.recipee.ui.update

import com.tdl.recipee.extension.plusAssign
import com.tdl.recipee.ui.base.mvi.BaseMviPresenter
import com.tdl.recipee.ui.base.redux.Store
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeAction
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeAction.*
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeReducer
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeViewState
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeViewState.AddRecipeViewState
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeViewState.EditRecipeViewState
import com.tdl.recipee.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

/**
 * Created by loc.ta on 2/23/2020.
 */
class AddOrEditRecipePresenter(
    private val interactor: AddOrEditRecipeInteractor,
    reducer: AddOrEditRecipeReducer,
    isAddNewRecipe: Boolean
) : BaseMviPresenter<AddOrEditRecipeView, AddOrEditRecipeViewState, AddOrEditRecipeAction>() {

    init {
        store = Store.create(reducer, AddOrEditRecipeViewState.invoke(isAddNewRecipe))
    }

    override fun bind(view: AddOrEditRecipeView) {

        val image = view.loadImageIntent()
            .flatMap { interactor.loadImage(it) }

        val ingredient = view.updateIngredientIntent()
            .map<AddOrEditRecipeAction> { (ingredient, oldIngredient) ->
                return@map if (oldIngredient != null) {
                    EditIngredient(ingredient, oldIngredient)
                } else {
                    AddNewIngredient(ingredient)
                }
            }

        val step = view.updateStepIntent()
            .map<AddOrEditRecipeAction> { (step, oldStep) ->
                return@map if (oldStep != null) {
                    EditStep(step, oldStep)
                } else {
                    AddNewStep(step)
                }
            }

        val validate = view.validateIntent()
            .map { Validate(it) }

        val save = view.saveIntent()
            .map { store.state }
            .flatMap {
                when (it) {
                    is AddRecipeViewState -> interactor.addRecipe(requireNotNull(it.recipe))
                    is EditRecipeViewState -> interactor.editRecipe(requireNotNull(it.recipe))
                }
            }

        val name = view.updateNameIntent()
            .map { UpdateName(it) }
            .observeOn(AndroidSchedulers.mainThread())

        val category = view.updateCategoryIntent()
            .map { UpdateCategory(it) }
            .observeOn(AndroidSchedulers.mainThread())

        val persist = view.persistIntent()
            .flatMap<AddOrEditRecipeAction> {
                interactor.loadImage(it.imageUrl.toString())
                    .startWith(Persist(it))
            }

        val removeIngredient = view.removeIngredientIntent()
            .map { RemoveIngredient(it) }

        val removeStep = view.removeStepIntent()
            .map { RemoveStep(it) }

        val combinedState: Observable<AddOrEditRecipeViewState> = Observable
            .merge(
                listOf(
                    image,
                    ingredient,
                    step,
                    validate,
                    save,
                    name,
                    category,
                    persist,
                    removeIngredient,
                    removeStep
                )
            )
            .scan(store.state, RxUtils.dispatchAction(store, navigator))
            .observeOn(AndroidSchedulers.mainThread())

        disposable += combinedState.subscribe({ view.render(it) }, { Timber.e(it) })
    }
}