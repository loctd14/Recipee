package com.tdl.recipee.ui.base.mvi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tdl.recipee.ui.home.HomeActivity
import com.tdl.recipee.ui.home.replaceFragment
import dagger.android.support.AndroidSupportInjection

/**
 * Created by loc.ta on 2/22/2020.
 */
abstract class BaseMviFragment<V : MviView, P : MviPresenter<V>> : Fragment() {

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    protected abstract fun createPresenter(): P

    private var presenter: P? = null
        get() {
            if (field == null) {
                field = createPresenter()
            }
            return field
        }

    @Suppress("UNCHECKED_CAST")
    override fun onStart() {
        super.onStart()
        presenter?.bind(this as V)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onPause() {
        super.onPause()
        presenter?.onPause()
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter?.unbind()
        presenter = null
        super.onDestroy()
    }

    protected fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) =
        (activity as? HomeActivity)?.replaceFragment(fragment, addToBackStack)

    private fun hideKeyboard() {
        activity?.let {
            val view = it.currentFocus
            val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            if (view != null) {
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}