package kpfu.itis.myservice.features.feature_search.presentation.message.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.features.feature_search.presentation.message.MessageFormViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class MessageFormViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(MessageFormViewModel::class)
    abstract fun bindMessageFormViewModel(
        messageFormViewModel: MessageFormViewModel
    ): ViewModel

}
