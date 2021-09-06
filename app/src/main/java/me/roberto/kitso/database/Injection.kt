package me.roberto.kitso.database

import android.content.Context
import me.roberto.kitso.KitsoService
import me.roberto.kitso.ui.ViewModelFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Injection {

    fun provideUserDataSource(context: Context): BookItemDao = KitsoDatabase.getInstance(context).KitsoDatabase_Impl()


    fun provideViewModelFactory(context: Context): ViewModelFactory = ViewModelFactory(provideUserDataSource(context))


    fun provideKitsoService() : KitsoService{
        val converter = MoshiConverterFactory.create()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.bitso.com/v3/")
                .addConverterFactory(converter)
                .build()

        return retrofit.create(KitsoService::class.java)
    }
}