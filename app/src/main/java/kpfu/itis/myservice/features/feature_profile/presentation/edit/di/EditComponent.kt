package kpfu.itis.myservice.features.feature_profile.presentation.edit.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_profile.presentation.edit.EditFragment

@EditScope
@Subcomponent(modules = [EditModule::class])
interface EditComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : EditComponent
    }

    fun inject(editFragment: EditFragment)

}
