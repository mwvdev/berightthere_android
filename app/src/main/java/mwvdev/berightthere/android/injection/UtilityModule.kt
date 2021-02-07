package mwvdev.berightthere.android.injection

import dagger.Binds
import dagger.Module
import mwvdev.berightthere.android.service.*
import mwvdev.berightthere.android.service.mapper.TripItemMapper
import mwvdev.berightthere.android.service.mapper.TripItemMapperImpl

@Module
abstract class UtilityModule {

    @Binds
    abstract fun provideDialogService(dialogService: DialogServiceImpl): DialogService

    @Binds
    abstract fun provideDistanceService(distanceService: DistanceServiceImpl) : DistanceService

    @Binds
    abstract fun provideIntentFactory(intentFactory: IntentFactoryImpl): IntentFactory

    @Binds
    abstract fun provideOffsetDateTimeService(offsetDateTimeService: OffsetDateTimeServiceImpl): OffsetDateTimeService

    @Binds
    abstract fun provideTripItemMapper(tripItemMapper: TripItemMapperImpl): TripItemMapper

}