package kpfu.itis.myservice.features.feature_add_service.presentation.service.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.features.feature_add_service.presentation.service.ServiceViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class ServiceViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(ServiceViewModel::class)
    abstract fun bindAddServiceViewModel(
        addServiceViewModel: ServiceViewModel
    ): ViewModel

}
