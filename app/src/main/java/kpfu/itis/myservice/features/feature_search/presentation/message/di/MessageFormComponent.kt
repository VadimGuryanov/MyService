package kpfu.itis.myservice.features.feature_search.presentation.message.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_search.presentation.message.MessageFormFragment

@MessageFormScope
@Subcomponent(modules = [MessageFormViewModelModule::class])
interface MessageFormComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : MessageFormComponent
    }

    fun inject(messageFormFragment: MessageFormFragment)

}
