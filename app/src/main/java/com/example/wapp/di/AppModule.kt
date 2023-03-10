package com.example.wapp.di

import android.app.Application
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.example.wapp.data.remote.WeatherApi
import com.example.wapp.database.CityDao
import com.example.wapp.database.CityDb
import com.example.wapp.database.CityRepositoryImpl
import com.example.wapp.repository.CityRepository
import com.example.wapp.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi) = WeatherRepository(api)

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.weatherapi.com/v1/")
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVideoPlayer(app: Application): Player {
        return ExoPlayer.Builder(app)
            .build()
    }


    @Provides
    fun provideCityDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        CityDb::class.java,
        "city_table"
    ).build()

    @Provides
    fun provideCityDao(
        cityDb: CityDb
    ) = cityDb.cityDao()

    @Provides
    fun provideCityRepository(cityDao: CityDao) = CityRepository(cityDao)

//    @Provides
//    fun provideCityRepository(
//        cityDao: CityDao
//    ): CityRepository = CityRepositoryImpl(
//        cityDao = cityDao
//    )
}