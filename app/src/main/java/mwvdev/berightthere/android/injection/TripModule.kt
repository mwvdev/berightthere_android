package mwvdev.berightthere.android.injection

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import mwvdev.berightthere.android.TripActivity
import mwvdev.berightthere.android.TripOngoingFragment
import mwvdev.berightthere.android.TripPreviewChildFragment
import mwvdev.berightthere.android.viewmodel.TripViewModel


@Module
abstract class TripModule {

    @Binds
    @IntoMap
    @ViewModelKey(TripViewModel::class)
    abstract fun bindViewModel(viewmodel: TripViewModel): ViewModel

    @ContributesAndroidInjector(modules = [RepositoryModule::class])
    abstract fun contributeTripActivityAndroidInjector(): TripActivity

    @ContributesAndroidInjector(modules = [RepositoryModule::class])
    abstract fun contributeTripOngoingFragmentAndroidInjector(): TripOngoingFragment

    @ContributesAndroidInjector
    abstract fun contributeTripPreviewChildFragmentAndroidInjector(): TripPreviewChildFragment

}