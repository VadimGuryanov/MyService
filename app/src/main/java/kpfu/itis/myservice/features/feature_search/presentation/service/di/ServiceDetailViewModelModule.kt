package kpfu.itis.myservice.features.feature_search.presentation.service.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.features.feature_search.presentation.service.ServiceDetailViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class ServiceDetailViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(ServiceDetailViewModel::class)
    abstract fun bindAddSearchViewModel(
        serviceDetailViewModel: ServiceDetailViewModel
    ): ViewModel

}
