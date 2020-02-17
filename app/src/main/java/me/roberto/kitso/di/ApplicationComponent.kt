package me.roberto.kitso.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import me.roberto.kitso.ui.MarketFragment
import javax.inject.Singleton

@Singleton
@Component(modules=[BaseModule::class])
interface ApplicationComponent {

    fun inject(marketFragment: MarketFragment)


    @Component.Builder
    interface Builder{

        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): ApplicationComponent

    }


}