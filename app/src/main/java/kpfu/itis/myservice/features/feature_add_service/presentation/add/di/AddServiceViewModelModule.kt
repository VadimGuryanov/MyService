package kpfu.itis.myservice.features.feature_add_service.presentation.add.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.features.feature_add_service.presentation.add.AddServiceViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class AddServiceViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(AddServiceViewModel::class)
    abstract fun bindAddServiceViewModel(
        addServiceViewModel: AddServiceViewModel
    ): ViewModel

}
