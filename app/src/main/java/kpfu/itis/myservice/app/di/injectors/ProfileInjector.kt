package kpfu.itis.myservice.app.di.injectors

import kpfu.itis.myservice.app.di.component.AppComponent
import kpfu.itis.myservice.features.feature_profile.data.di.ProfileDataComponent
import kpfu.itis.myservice.features.feature_profile.domain.di.ProfileDomainComponent
import kpfu.itis.myservice.features.feature_profile.presentation.about_me.di.AboutMeComponent
import kpfu.itis.myservice.features.feature_profile.presentation.auth.di.AuthComponent
import kpfu.itis.myservice.features.feature_profile.presentation.edit.di.EditComponent
import kpfu.itis.myservice.features.feature_profile.presentation.profile.di.ProfileComponent
import kpfu.itis.myservice.features.feature_profile.presentation.services.di.ProfileServicesComponent

object ProfileInjector {

    lateinit var appComponent: AppComponent
    private var profileDataComponent: ProfileDataComponent? = null
    private var domainComponent: ProfileDomainComponent? = null
    private var profileComponent: ProfileComponent? = null
    private var authComponent: AuthComponent? = null
    private var aboutMeComponent: AboutMeComponent? = null
    private var editComponent: EditComponent? = null
    private var servicesComponent: ProfileServicesComponent? = null

    fun init(app: AppComponent) {
        appComponent = app
    }

    fun plusProfileDataComponent(): ProfileDataComponent = profileDataComponent
        ?: appComponent
        .plusProfileDataComponent()
        .build().also {
            profileDataComponent = it
        }

    fun clearReposioryComponent() {
        profileDataComponent = null
    }

    fun plusDomainComponent(): ProfileDomainComponent = domainComponent
        ?: plusProfileDataComponent()
        .plusProfileDomainComponent()
        .build().also {
            domainComponent = it
        }

    fun clearDomainComponent() {
        domainComponent = null
    }

    fun plusProfileComponent(): ProfileComponent = profileComponent
        ?: plusDomainComponent()
        .plusProfileComponent()
        .build().also {
            profileComponent = it
        }

    fun clearProfileComponent() {
        profileComponent = null
    }

    fun plusAuthComponent(): AuthComponent = authComponent
        ?: plusDomainComponent()
        .plusAuthComponent()
        .build().also {
            authComponent = it
        }

    fun clearAuthComponent() {
        authComponent = null
    }

    fun plusAboutMeComponent(): AboutMeComponent = aboutMeComponent
        ?: plusDomainComponent()
        .plusAboutMeComponent()
        .build().also {
            aboutMeComponent = it
        }

    fun clearAboutMeComponent() {
        aboutMeComponent = null
    }

    fun plusEditComponent(): EditComponent = editComponent
        ?: plusDomainComponent()
        .plusEditComponent()
        .build().also {
            editComponent = it
        }

    fun clearEditComponent() {
        editComponent = null
    }

    fun plusProfileServicesComponent(): ProfileServicesComponent = servicesComponent
        ?: plusDomainComponent()
        .plusProfileServicesComponent()
        .build().also {
            servicesComponent = it
        }

    fun clearProfileServicesComponent() {
        servicesComponent = null
    }

}
