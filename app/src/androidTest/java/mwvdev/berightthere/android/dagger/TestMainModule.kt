package mwvdev.berightthere.android.dagger

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import mwvdev.berightthere.android.MainActivity
import mwvdev.berightthere.android.MainCheckingInFragment
import mwvdev.berightthere.android.MainOverviewFragment
import mwvdev.berightthere.android.injection.ViewModelKey
import mwvdev.berightthere.android.viewmodel.MainViewModel

@Module
abstract class TestMainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(viewmodel: MainViewModel): ViewModel

    @ContributesAndroidInjector(modules = [TestRepositoryModule::class])
    abstract fun contributeMainActivityAndroidInjector(): MainActivity

    @ContributesAndroidInjector(modules = [TestRepositoryModule::class])
    abstract fun contributeMainOverviewFragmentAndroidInjector(): MainOverviewFragment

    @ContributesAndroidInjector(modules = [TestRepositoryModule::class])
    abstract fun contributeMainCheckingInFragmentAndroidInjector(): MainCheckingInFragment

}