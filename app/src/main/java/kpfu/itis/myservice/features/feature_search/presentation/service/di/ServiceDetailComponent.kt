package kpfu.itis.myservice.features.feature_search.presentation.service.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_search.presentation.service.ServiceDetailFragment

@ServiceDetailScope
@Subcomponent(modules = [ServiceDetailViewModelModule::class])
interface ServiceDetailComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : ServiceDetailComponent
    }

    fun inject(serviceDetailFragment: ServiceDetailFragment)

}
