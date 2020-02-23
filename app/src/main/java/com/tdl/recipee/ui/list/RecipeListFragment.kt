package com.tdl.recipee.ui.list

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.tdl.recipee.R
import com.tdl.recipee.data.model.Recipe
import com.tdl.recipee.extension.forceReload
import com.tdl.recipee.extension.textChanges
import com.tdl.recipee.ui.base.adapter.BaseAdapter
import com.tdl.recipee.ui.base.mvi.BaseMviFragment
import com.tdl.recipee.ui.base.mvi.NavigationAction
import com.tdl.recipee.ui.base.mvi.Navigator
import com.tdl.recipee.ui.list.reducer.RecipeListAction.*
import com.tdl.recipee.ui.list.reducer.RecipeListViewState
import com.tdl.recipee.ui.update.AddOrEditRecipeFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_recipe_list.*
import kotlinx.android.synthetic.main.item_recipe.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by loc.ta on 2/22/2020.
 */
class RecipeListFragment : BaseMviFragment<RecipeListView, RecipeListPresenter>(), RecipeListView,
    Navigator<RecipeListViewState> {

    @Inject
    lateinit var presenter: RecipeListPresenter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var adapter: BaseAdapter<Pair<Recipe, Bitmap?>>

    private val deleteRecipeSubject = PublishSubject.create<Pair<Recipe, Bitmap?>>()

    private val reloadSubject = PublishSubject.create<Boolean>()

    override fun getLayoutResId(): Int = R.layout.fragment_recipe_list

    override fun createPresenter(): RecipeListPresenter = presenter.apply {
        navigator = this@RecipeListFragment
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferences.forceReload) {
            reloadSubject.onNext(true)
            sharedPreferences.forceReload = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_add?.setOnClickListener {
            replaceFragment(AddOrEditRecipeFragment())
        }
    }

    override fun render(state: RecipeListViewState) {
        if (state.recipes != null) {
            if (recyclerView.adapter == null) {
                this.adapter = BaseAdapter(
                    layoutResId = R.layout.item_recipe,
                    list = state.recipes.toMutableList(),
                    bindHolder = { recipe ->
                        imageView_recipe?.setImageBitmap(recipe.second)
                        textView_recipe_name?.text = recipe.first.name
                        textView_recipe_category?.text = recipe.first.category
                        imageView_more?.setOnClickListener {
                            val popup = PopupMenu(requireContext(), imageView_more)
                            popup.menuInflater.inflate(R.menu.menu_recipe_item, popup.menu)
                            popup.setOnMenuItemClickListener { menu ->
                                return@setOnMenuItemClickListener when (menu.itemId) {
                                    R.id.menu_delete -> {
                                        deleteRecipeSubject.onNext(recipe)
                                        true
                                    }
                                    else -> true
                                }
                            }
                            popup.show()
                        }
                        textView_details?.setOnClickListener {
                            replaceFragment(AddOrEditRecipeFragment.invoke(recipe.first))
                        }
                    })
                recyclerView.adapter = this.adapter
            }
        }
    }

    override fun executeNavigationAction(action: NavigationAction, state: RecipeListViewState) {
        when (action) {
            is DeleteRecipe, is ReloadRecipes -> {
                adapter.set(state.recipes.orEmpty().toMutableList())
            }
            is SearchRecipes -> adapter.set(action.recipe.toMutableList())
        }
    }

    override fun loadInitialIntent(): Observable<Boolean> = Observable.just(true)

    override fun deleteRecipeIntent(): Observable<Pair<Recipe, Bitmap?>> = deleteRecipeSubject

    override fun reloadIntent(): Observable<Boolean> = reloadSubject

    override fun searchIntent(): Observable<String> = editText_search
        .textChanges()
        .filter { editText_search.isFocused }
        .debounce(300, TimeUnit.MILLISECONDS)
}