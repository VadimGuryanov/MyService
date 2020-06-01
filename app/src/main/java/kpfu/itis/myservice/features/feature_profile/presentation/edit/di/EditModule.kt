package kpfu.itis.myservice.features.feature_profile.presentation.edit.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.features.feature_profile.presentation.edit.EditViewModel

@Module(includes = [ViewModelModule::class])
abstract class EditModule {

    @IntoMap
    @Binds
    @ViewModelKey(EditViewModel::class)
    abstract fun bindEditViewModel(
        editViewModel: EditViewModel
    ): ViewModel

}
