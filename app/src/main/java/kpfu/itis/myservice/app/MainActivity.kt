package kpfu.itis.myservice.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import kotlinx.android.synthetic.main.activity_main.*
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.di.Injector
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.features.feature_notification.presentation.NotificationsFragment
import kpfu.itis.myservice.features.feature_profile.presentation.auth.AuthFragment
import kpfu.itis.myservice.features.feature_profile.presentation.profile.ProfileFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity(), INavigation{

    @Inject
    lateinit var helper: HelperSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Injector.appComponent.inject(this)
//        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_search,
//                R.id.nav_favorite_services,
//                R.id.nav_add_service,
//                R.id.nav_notifications,
//                R.id.nav_profile
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        nav_view.setupWithNavController(navController)
        if (savedInstanceState == null) {
            nav_view.menu.getItem(2).isChecked = true
            supportFragmentManager.beginTransaction().apply {
                add(R.id.container_main, NotificationsFragment.newInstance())
                commit()
            }
        }
        nav_view.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.nav_profile -> {
                    helper.readSession()?.apply {
                        navigateTo(ProfileFragment.newInstance())
                    }
                        ?: navigateTo(AuthFragment.newInstance())
                    true
                }
                R.id.nav_search -> {
                    navigateTo(NotificationsFragment.newInstance())
                    true
                }
                R.id.nav_favorite_services -> {
                    navigateTo(NotificationsFragment.newInstance())
                    true
                }
                R.id.nav_add_service -> {
                    navigateTo(NotificationsFragment.newInstance())
                    true
                }
                R.id.nav_notifications -> {
                    navigateTo(NotificationsFragment.newInstance())
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        when {
            fragmentManager.backStackEntryCount > 0 -> fragmentManager.popBackStack()
            else -> super.onBackPressed()
        }
    }

    override fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main, fragment)
            addToBackStack(fragment::class.java.name)
            commit()
        }
    }

    override fun navigateToNotAdd(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main, fragment)
            commit()
        }
    }

    override fun popBackStack() {
        supportFragmentManager.popBackStack()
    }


    //for vk
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        Log.e("resssult", "$data")
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private var callback =  object : VKAuthCallback {
        override fun onLogin(token: VKAccessToken) {
            Log.e("user id", token.userId?.toString() ?: "-1")
            helper.editId(token.userId?.toString() ?: "-1")
        }

        override fun onLoginFailed(errorCode: Int) {

        }
    }

}
