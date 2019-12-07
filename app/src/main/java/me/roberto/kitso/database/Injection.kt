package me.roberto.kitso.database

import android.content.Context
import me.roberto.kitso.network.KitsoService
import me.roberto.kitso.ui.ViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Injection {

    fun provideUserDataSource(context: Context): BookItemDao {
        val database = KitsoDatabase.getInstance(context)
        return database.KitsoDatabase_Impl()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideUserDataSource(context)
        return ViewModelFactory(dataSource)
    }

    fun provideKitsoService() : KitsoService {
        val converter = MoshiConverterFactory.create()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.bitso.com/v3/")
                .addConverterFactory(converter)
                .build()

        return retrofit.create(KitsoService::class.java)
    }
}