package kpfu.itis.myservice.app.navigation

import androidx.fragment.app.Fragment

interface INavigation {

    fun navigateTo(fragment: Fragment)

    fun navigateToNotAdd(fragment: Fragment)

    fun popBackStack()

//    fun navigateToActivity()

}