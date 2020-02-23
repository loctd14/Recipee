package com.tdl.recipee.ui.update

import android.os.Bundle
import android.view.View
import com.tdl.recipee.R
import com.tdl.recipee.data.model.Ingredient
import com.tdl.recipee.data.model.Recipe
import com.tdl.recipee.data.model.Step
import com.tdl.recipee.extension.clicks
import com.tdl.recipee.extension.textChanges
import com.tdl.recipee.ui.base.adapter.BaseAdapter
import com.tdl.recipee.ui.base.mvi.BaseMviFragment
import com.tdl.recipee.ui.base.mvi.NavigationAction
import com.tdl.recipee.ui.base.mvi.Navigator
import com.tdl.recipee.ui.update.dialog.IngredientDialog
import com.tdl.recipee.ui.update.dialog.SearchImageDialog
import com.tdl.recipee.ui.update.dialog.StepDialog
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeAction.*
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeReducer
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeViewState
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_add_or_edit_recipe.*
import kotlinx.android.synthetic.main.simple_recyclerview_item.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by loc.ta on 2/23/2020.
 */
class AddOrEditRecipeFragment : BaseMviFragment<AddOrEditRecipeView, AddOrEditRecipePresenter>(),
    AddOrEditRecipeView, Navigator<AddOrEditRecipeViewState> {

    @Inject
    lateinit var interactor: AddOrEditRecipeInteractor

    @Inject
    lateinit var reducer: AddOrEditRecipeReducer

    private lateinit var selectedRecipe: Recipe

    private lateinit var searchDialog: SearchImageDialog

    private lateinit var ingredientDialog: IngredientDialog

    private lateinit var stepDialog: StepDialog

    private val loadImageSubject = PublishSubject.create<String>()

    private val persistRecipeSubject = PublishSubject.create<Recipe>()

    private val updateIngredientSubject = PublishSubject.create<Pair<Ingredient, Ingredient?>>()

    private val updateStepSubject = PublishSubject.create<Pair<Step, Step?>>()

    private val removeIngredientSubject = PublishSubject.create<Ingredient>()

    private val removeStepSubject = PublishSubject.create<Step>()

    private var ingredientAdapter: BaseAdapter<Ingredient>? = null
        get() {
            if (field == null) {
                field = BaseAdapter(mutableListOf(),
                    R.layout.simple_recyclerview_item,
                    { ingredient ->
                        textView_name?.text = ingredient.name
                        textView_details?.text = ingredient.amount
                        imageView_remove?.setOnClickListener {
                            removeIngredientSubject.onNext(ingredient)
                        }
                    },
                    {
                        ingredientDialog = IngredientDialog.invoke(this) {
                            updateIngredientSubject.onNext(it)
                        }
                        ingredientDialog.show(
                            childFragmentManager,
                            ingredientDialog::class.java.name
                        )
                    }
                )
            }
            return field
        }

    private var stepAdapter: BaseAdapter<Step>? = null
        get() {
            if (field == null) {
                field = BaseAdapter(mutableListOf(),
                    R.layout.simple_recyclerview_item,
                    { step ->
                        textView_name?.text = step.name
                        textView_details?.text = step.description
                        imageView_remove?.setOnClickListener {
                            removeStepSubject.onNext(step)
                        }
                    },
                    {
                        stepDialog = StepDialog.invoke(this) {
                            updateStepSubject.onNext(it)
                        }
                        stepDialog.show(
                            childFragmentManager,
                            stepDialog::class.java.name
                        )
                    }
                )
            }
            return field
        }

    private var currentTab = INGREDIENT_TAB

    companion object {
        private const val INGREDIENT_TAB = 0
        private const val STEP_TAB = 1

        operator fun invoke(recipe: Recipe): AddOrEditRecipeFragment =
            AddOrEditRecipeFragment().apply {
                selectedRecipe = recipe
            }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_add_or_edit_recipe

    override fun createPresenter(): AddOrEditRecipePresenter = AddOrEditRecipePresenter(
        interactor,
        reducer,
        !::selectedRecipe.isInitialized
    ).apply { navigator = this@AddOrEditRecipeFragment }

    override fun onResume() {
        super.onResume()
        if (::selectedRecipe.isInitialized) {
            persistRecipeSubject.onNext(selectedRecipe)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*Initial load*/
        if (::selectedRecipe.isInitialized) {
            textView_title.text = requireContext().getString(R.string.text_edit_recipe)
            editText_recipe_name.setText(selectedRecipe.name)
            editText_recipe_category.setText(selectedRecipe.category)

            if (!selectedRecipe.ingredients.isNullOrEmpty()) {
                ingredientAdapter?.set(selectedRecipe.ingredients.orEmpty().toMutableList())
            }

            if (!selectedRecipe.steps.isNullOrEmpty()) {
                stepAdapter?.set(selectedRecipe.steps.orEmpty().toMutableList())
            }
        }
        recyclerView_content.adapter = ingredientAdapter

        imageView_recipe?.setOnClickListener {
            searchDialog = SearchImageDialog.invoke {
                loadImageSubject.onNext(it)
            }
            searchDialog.show(childFragmentManager, searchDialog::class.java.name)
        }

        textView_add?.setOnClickListener {
            if (currentTab == INGREDIENT_TAB) {
                ingredientDialog = IngredientDialog.invoke {
                    updateIngredientSubject.onNext(it)
                }
                ingredientDialog.show(childFragmentManager, ingredientDialog::class.java.name)
            } else if (currentTab == STEP_TAB) {
                stepDialog = StepDialog.invoke {
                    updateStepSubject.onNext(it)
                }
                stepDialog.show(childFragmentManager, stepDialog::class.java.name)
            }
        }

        textView_ingredient?.setOnClickListener {
            if (it.isEnabled) {
                it.isEnabled = false
                it.isSelected = true

                textView_step.apply {
                    isEnabled = true
                    isSelected = false
                }
                textView_name?.text = requireContext().getString(R.string.text_ingredient)
                textView_description?.text =
                    requireContext().getString(R.string.text_ingredient_amount)
                currentTab = INGREDIENT_TAB
                recyclerView_content?.adapter = ingredientAdapter
            }
        }

        textView_step?.setOnClickListener {
            if (it.isEnabled) {
                it.isEnabled = false
                it.isSelected = true

                textView_ingredient.apply {
                    isEnabled = true
                    isSelected = false
                }
                textView_name?.text = requireContext().getString(R.string.text_step)
                textView_description?.text =
                    requireContext().getString(R.string.text_step_description)
                currentTab = STEP_TAB
                recyclerView_content?.adapter = stepAdapter
            }
        }
    }

    override fun render(state: AddOrEditRecipeViewState) {
        textView_save?.isEnabled = state.isValid
    }

    override fun executeNavigationAction(
        action: NavigationAction,
        state: AddOrEditRecipeViewState
    ) {
        when (action) {
            is LoadImage -> {
                if (action.image != null) {
                    imageView_recipe.setImageBitmap(action.image)
                } else {
                    imageView_recipe.setImageResource(R.mipmap.background_place_holder)
                }
            }
            is AddNewIngredient, is EditIngredient, is RemoveIngredient -> {
                ingredientAdapter?.set(state.recipe?.ingredients.orEmpty().toMutableList())
            }
            is AddNewStep, is EditStep, is RemoveStep -> {
                stepAdapter?.set(state.recipe?.steps.orEmpty().toMutableList())
            }
            SaveRecipe -> {
                activity?.onBackPressed()
            }
        }
    }

    override fun loadImageIntent(): Observable<String> = loadImageSubject

    override fun updateIngredientIntent(): Observable<Pair<Ingredient, Ingredient?>> =
        updateIngredientSubject

    override fun updateStepIntent(): Observable<Pair<Step, Step?>> = updateStepSubject

    override fun validateIntent(): Observable<Boolean> = Observable.combineLatest(
        editText_recipe_category.textChanges()
            .debounce(250, TimeUnit.MILLISECONDS)
            .map { it }
            .startWith(editText_recipe_category.text.toString()),
        editText_recipe_name
            .textChanges()
            .debounce(250, TimeUnit.MILLISECONDS)
            .map { it }
            .startWith(editText_recipe_name.text.toString()),
        BiFunction { category: String, name: String ->
            category.isNotBlank() && name.isNotBlank()
        }
    )

    override fun updateNameIntent(): Observable<String> = editText_recipe_name
        .textChanges()
        .debounce(100, TimeUnit.MILLISECONDS)

    override fun updateCategoryIntent(): Observable<String> = editText_recipe_category
        .textChanges()
        .debounce(100, TimeUnit.MILLISECONDS)

    override fun saveIntent(): Observable<Boolean> = textView_save.clicks()
        .debounce(200, TimeUnit.MILLISECONDS)

    override fun persistIntent(): Observable<Recipe> = persistRecipeSubject

    override fun removeIngredientIntent(): Observable<Ingredient> = removeIngredientSubject

    override fun removeStepIntent(): Observable<Step> = removeStepSubject
}