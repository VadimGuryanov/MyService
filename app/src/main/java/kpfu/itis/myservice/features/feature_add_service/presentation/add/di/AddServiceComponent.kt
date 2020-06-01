package kpfu.itis.myservice.features.feature_add_service.presentation.add.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_add_service.presentation.add.AddServiceFragment

@AddServiceScope
@Subcomponent(modules = [AddServiceViewModelModule::class])
interface AddServiceComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : AddServiceComponent
    }

    fun inject(addServiceFragment: AddServiceFragment)
}
