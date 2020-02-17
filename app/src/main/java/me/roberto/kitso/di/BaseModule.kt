package me.roberto.kitso.di

import android.content.Context
import dagger.Module
import dagger.Provides
import me.roberto.kitso.database.BookItemDao
import me.roberto.kitso.database.KitsoDatabase
import me.roberto.kitso.network.KitsoService
import me.roberto.kitso.repository.KitsoRepository
import me.roberto.kitso.ui.ViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class BaseModule {

    @Singleton
    @Provides
    fun provideUserDataSource(context: Context): BookItemDao {
        val database = KitsoDatabase.getInstance(context)
        return database.KitsoDatabase_Impl()
    }


    @Provides
    fun provideViewModelFactory(datasource: BookItemDao, kitsoRepository: KitsoRepository): ViewModelFactory {
        return ViewModelFactory(datasource,kitsoRepository)
    }

    @Singleton
    @Provides
    fun provideKitsoService() : KitsoService {
        val converter = MoshiConverterFactory.create()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.bitso.com/v3/")
                .addConverterFactory(converter)
                .build()

        return retrofit.create(KitsoService::class.java)
    }

    @Provides
    fun provideKitsoRepository(kitsoService: KitsoService, datasource: BookItemDao) = KitsoRepository(kitsoService, datasource)
}