package com.tdl.recipee.extension

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * Created by loc.ta on 2/22/2020.
 */
fun AppCompatActivity.commitTransaction(fragment: Fragment,
                                        @IdRes containerId: Int,
                                        addToBackStack: Boolean = true,
                                        allowStateLoss: Boolean = false) {
    val ft = supportFragmentManager
        .beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .replace(containerId, fragment, fragment::class.java.simpleName)
    if (addToBackStack) {
        ft.addToBackStack(null)
    }
    if (!supportFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}