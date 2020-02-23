package com.tdl.recipee.ui.update.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tdl.recipee.R
import kotlinx.android.synthetic.main.dialog_search_image.*

/**
 * Created by loc.ta on 2/23/2020.
 */
class SearchImageDialog : BottomSheetDialogFragment() {

    private var listener: ((String) -> Unit)? = null

    companion object {
        operator fun invoke(listener: ((String) -> Unit)): SearchImageDialog =
            SearchImageDialog().apply {
                this.listener = listener
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_search_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText_search?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(content: Editable?) {
                button_continue?.isEnabled = content?.toString()?.isNotBlank() == true
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        button_continue?.setOnClickListener {
            listener?.invoke(editText_search.text.toString().trim())
            dismiss()
        }

        imageView_close?.setOnClickListener {
            dismiss()
        }
    }
}