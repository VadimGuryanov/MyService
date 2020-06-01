package kpfu.itis.myservice.features.feature_add_service.presentation.service_list.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.features.feature_add_service.presentation.service_list.ServicesViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class ServicesViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(ServicesViewModel::class)
    abstract fun bindServiceViewModel(
        serviceViewModel: ServicesViewModel
    ): ViewModel

}
