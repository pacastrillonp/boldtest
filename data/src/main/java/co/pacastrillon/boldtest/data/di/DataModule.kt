package co.pacastrillon.boldtest.data.di

import android.content.Context
import androidx.room.Room
import co.pacastrillon.boldtest.data.local.dao.ForecastCacheDao
import co.pacastrillon.boldtest.data.local.dao.RecentSearchDao
import co.pacastrillon.boldtest.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val DB_NAME = "bold_database"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .build()

    @Provides
    @Singleton
    fun provideRecentSearchDao(db: AppDatabase): RecentSearchDao = db.recentSearchDao()

    @Provides
    @Singleton
    fun provideForecastCacheDao(db: AppDatabase): ForecastCacheDao = db.forecastCacheDao()

}
