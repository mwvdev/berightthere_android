package mwvdev.berightthere.android.injection

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import mwvdev.berightthere.android.BuildConfig
import mwvdev.berightthere.android.service.BeRightThereService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
class NetworkModule {

    @Provides
    fun provideBeRightThereService(): BeRightThereService {
        val gson = Converters.registerOffsetDateTime(GsonBuilder()).create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BERIGHTTHERE_API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create()
    }

}