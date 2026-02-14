package co.pacastrillon.boldtest.data.di

import android.content.Context
import androidx.room.Room
import co.pacastrillon.boldtest.data.local.db.BoldDatabase
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
    ): BoldDatabase =
        Room.databaseBuilder(context, BoldDatabase::class.java, DB_NAME)
            .build()

//    @Provides
//    fun provideCharacterDao(db: BoldDatabase): CharacterDao = db.characterDao()
}
