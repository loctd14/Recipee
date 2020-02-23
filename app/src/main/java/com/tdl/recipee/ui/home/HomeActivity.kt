package com.tdl.recipee.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tdl.recipee.R
import com.tdl.recipee.extension.commitTransaction
import com.tdl.recipee.ui.list.RecipeListFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Created by loc.ta on 2/22/2020.
 */
class HomeActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        commitTransaction(RecipeListFragment(), R.id.fragment_container, addToBackStack = false)
    }
}

fun HomeActivity.replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
    this.commitTransaction(
        fragment,
        containerId = R.id.fragment_container,
        addToBackStack = addToBackStack
    )
}