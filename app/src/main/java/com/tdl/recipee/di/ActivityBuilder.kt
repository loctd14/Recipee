package com.tdl.recipee.di

import com.tdl.recipee.ui.home.HomeActivity
import com.tdl.recipee.ui.home.HomeActivityContributor
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by loc.ta on 2/22/2020.
 */
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [HomeActivityContributor::class])
    abstract fun bindHomeActivity(): HomeActivity
}