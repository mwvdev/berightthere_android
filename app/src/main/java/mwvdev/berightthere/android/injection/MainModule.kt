package mwvdev.berightthere.android.injection

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import mwvdev.berightthere.android.*
import mwvdev.berightthere.android.viewmodel.MainViewModel

@Module
abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(viewmodel: MainViewModel): ViewModel

    @ContributesAndroidInjector(modules = [RepositoryModule::class])
    abstract fun contributeMainActivityAndroidInjector(): MainActivity

    @ContributesAndroidInjector(modules = [RepositoryModule::class])
    abstract fun contributeMainOverviewFragmentAndroidInjector(): MainOverviewFragment

    @ContributesAndroidInjector(modules = [RepositoryModule::class])
    abstract fun contributeMainPermissionRationaleFragmentAndroidInjector(): MainPermissionRationaleFragment

    @ContributesAndroidInjector(modules = [RepositoryModule::class])
    abstract fun contributeMainCheckingInFragmentAndroidInjector(): MainCheckingInFragment

}