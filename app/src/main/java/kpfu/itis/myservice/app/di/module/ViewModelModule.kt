package kpfu.itis.myservice.app.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import kpfu.itis.myservice.app.ViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

}
