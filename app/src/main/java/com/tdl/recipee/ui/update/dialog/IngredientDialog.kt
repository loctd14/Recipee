package com.tdl.recipee.ui.update.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tdl.recipee.R
import com.tdl.recipee.data.model.Ingredient
import com.tdl.recipee.extension.textChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.dialog_ingredient.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by loc.ta on 2/23/2020.
 */
class IngredientDialog : BottomSheetDialogFragment() {

    private var listener: ((Pair<Ingredient, Ingredient?>) -> Unit)? = null

    private var state: Int = ADD_INGREDIENT_STATE

    private var disposable: Disposable? = null

    private var selectedIngredient: Ingredient? = null

    companion object {
        private const val ADD_INGREDIENT_STATE = 0
        private const val EDIT_INGREDIENT_STATE = 1

        operator fun invoke(
            ingredient: Ingredient? = null,
            listener: ((Pair<Ingredient, Ingredient?>) -> Unit)
        ): IngredientDialog =
            IngredientDialog().apply {
                this.listener = listener
                selectedIngredient = ingredient
                this.state = when (ingredient) {
                    null -> ADD_INGREDIENT_STATE
                    else -> EDIT_INGREDIENT_STATE
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_ingredient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView_close?.setOnClickListener { dismiss() }

        if (state == EDIT_INGREDIENT_STATE) {
            textView_title?.text = requireContext().getString(R.string.text_edit_ingredient)
            editText_ingredient?.setText(selectedIngredient?.name)
            editText_amount?.setText(selectedIngredient?.amount)
            button_action?.text = requireContext().getString(R.string.text_save_action)
            button_action?.setOnClickListener {
                listener?.invoke(
                    Ingredient(
                        name = editText_ingredient.text.toString().trim(),
                        amount = editText_amount.text.toString().trim()
                    ) to selectedIngredient
                )
                dismiss()
            }
        } else {
            textView_title?.text = requireContext().getString(R.string.text_add_ingredient)
            button_action?.text = requireContext().getString(R.string.text_add_action)
            button_action?.setOnClickListener {
                listener?.invoke(
                    Ingredient(
                        name = editText_ingredient.text.toString().trim(),
                        amount = editText_amount.text.toString().trim()
                    ) to null
                )
                dismiss()
            }
        }

        disposable = Observable.combineLatest(
            editText_ingredient.textChanges()
                .debounce(250, TimeUnit.MILLISECONDS)
                .map { it }
                .startWith(editText_ingredient.text.toString()),
            editText_amount
                .textChanges()
                .debounce(250, TimeUnit.MILLISECONDS)
                .map { it }
                .startWith(editText_amount.text.toString()),
            BiFunction { ingredient: String, amount: String ->
                ingredient.isNotBlank() && amount.isNotBlank()
            }
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ button_action.isEnabled = it }, { Timber.w(it) })
    }

    override fun onDestroy() {
        disposable?.dispose()
        disposable = null
        super.onDestroy()
    }
}