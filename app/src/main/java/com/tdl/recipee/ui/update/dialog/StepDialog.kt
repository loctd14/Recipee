package com.tdl.recipee.ui.update.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tdl.recipee.R
import com.tdl.recipee.data.model.Step
import com.tdl.recipee.extension.textChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.dialog_step.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by loc.ta on 2/23/2020.
 */
class StepDialog : BottomSheetDialogFragment() {

    private var listener: ((Pair<Step, Step?>) -> Unit)? = null

    private var state: Int = ADD_STEP_STATE

    private var disposable: Disposable? = null

    private var selectedStep: Step? = null

    companion object {
        const val ADD_STEP_STATE = 0
        const val EDIT_STEP_STATE = 1

        operator fun invoke(
            step: Step? = null,
            listener: ((Pair<Step, Step?>) -> Unit)
        ): StepDialog =
            StepDialog().apply {
                this.listener = listener
                selectedStep = step
                this.state = when (step) {
                    null -> ADD_STEP_STATE
                    else -> EDIT_STEP_STATE
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
        return inflater.inflate(R.layout.dialog_step, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView_close?.setOnClickListener { dismiss() }

        if (state == EDIT_STEP_STATE) {
            textView_title?.text = requireContext().getString(R.string.text_edit_step)
            editText_step?.setText(selectedStep?.name)
            editText_description?.setText(selectedStep?.description)
            button_action?.text = requireContext().getString(R.string.text_save_action)
            button_action?.setOnClickListener {
                listener?.invoke(
                    Step(
                        name = editText_step.text.toString().trim(),
                        description = editText_description.text.toString().trim()
                    ) to selectedStep
                )
                dismiss()
            }
        } else {
            textView_title?.text = requireContext().getString(R.string.text_add_step)
            button_action?.text = requireContext().getString(R.string.text_add_action)
            button_action?.setOnClickListener {
                listener?.invoke(
                    Step(
                        name = editText_step.text.toString().trim(),
                        description = editText_description.text.toString().trim()
                    ) to null
                )
                dismiss()
            }
        }

        disposable = Observable.combineLatest(
            editText_step.textChanges()
                .debounce(250, TimeUnit.MILLISECONDS)
                .map { it }
                .startWith(editText_step.text.toString()),
            editText_description
                .textChanges()
                .debounce(250, TimeUnit.MILLISECONDS)
                .map { it }
                .startWith(editText_description.text.toString()),
            BiFunction { step: String, amount: String ->
                step.isNotBlank() && amount.isNotBlank()
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