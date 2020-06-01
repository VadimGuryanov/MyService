package kpfu.itis.myservice.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.*
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import kotlinx.android.synthetic.main.activity_main.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.di.injectors.ProfileInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.features.feature_add_service.presentation.service_list.ServicesFragment
import kpfu.itis.myservice.features.feature_favorite_service.presenter.favorites.FavoritesFragment
import kpfu.itis.myservice.features.feature_message_service.service.worker.ListenMessageWorker
import kpfu.itis.myservice.features.feature_message_service.service.worker.ListenMessageWorker.Companion.ARG_USER_ID
import kpfu.itis.myservice.features.feature_message_service.service.worker.ListenMessageWorker.Companion.TAG
import kpfu.itis.myservice.features.feature_notification.presentation.notification.NotificationDetailFragment
import kpfu.itis.myservice.features.feature_notification.presentation.notifications.NotificationsFragment
import kpfu.itis.myservice.features.feature_profile.presentation.auth.AuthFragment
import kpfu.itis.myservice.features.feature_profile.presentation.profile.ProfileFragment
import kpfu.itis.myservice.features.feature_search.presentation.search.SearchFragment
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity(), INavigation, MessageListener {

    @Inject
    lateinit var helper: HelperSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar =  findViewById<Toolbar>(R.id.toolbar_action)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.title_search)
        ProfileInjector.appComponent.inject(this)
        listenMessages()
        if (savedInstanceState == null) {
            var id = intent.getLongExtra(KEY_MESSAGE, -1)
            if (id < 0) {
                nav_view.menu.getItem(0).isChecked = true
                supportFragmentManager.beginTransaction().apply {
                    add(R.id.container_main, SearchFragment())
                    commit()
                }
            } else {
                nav_view.menu.getItem(4).isChecked = true
                supportFragmentManager.beginTransaction().apply {
                    add(R.id.container_main, NotificationDetailFragment.newInstance(id))
                    commit()
                }
            }
        }
        nav_view.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.nav_search -> {
                    if (!nav_view.menu.getItem(0).isChecked) {
                        navigateTo(SearchFragment())
                    }
                    true
                }
                R.id.nav_favorite_services -> {
                    if (!nav_view.menu.getItem(1).isChecked) {
                        navigateTo(FavoritesFragment())
                    }
                    true
                }
                R.id.nav_add_service -> {
                    if (!nav_view.menu.getItem(2).isChecked) {
                        navigateTo(ServicesFragment())
                    }
                    true
                }
                R.id.nav_notifications -> {
                    if (!nav_view.menu.getItem(3).isChecked) {
                        navigateTo(NotificationsFragment())
                    }
                    true
                }
                R.id.nav_profile -> {
                    if (!nav_view.menu.getItem(4).isChecked) {
                        helper.readID()?.let {
                            helper.readSession()?.apply {
                                navigateTo(ProfileFragment.newInstance(it.toLong(), null))
                            }  ?: navigateTo(AuthFragment())
                        } ?: navigateTo(AuthFragment())
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.apply {
            when {
                backStackEntryCount > 0 -> popBackStack()
                else -> super.onBackPressed()
            }
        }
    }

    override fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main, fragment, fragment::class.java.name)
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

    override fun navigateBack() {
        supportFragmentManager.popBackStack()
    }

    //Попытка слушать сообщения
    override fun listenMessages() {
        helper.readID()?.toLong()?.let {
            val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            val inputData = Data.Builder()
                .putLong(ARG_USER_ID, it)
                .build()
            val listenerMessages =
                PeriodicWorkRequestBuilder<ListenMessageWorker>(
                1, TimeUnit.MINUTES)
                    .addTag(TAG)
                    .setConstraints(constraints)
                    .setInputData(inputData)
                    .build()
            WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                listenerMessages
            )
            WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(listenerMessages.id)
                .observe(this, Observer { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                        Log.e("workmanager","Work finished!")
                    } else {
                        Log.e("workmanager","Work")
                    }
                })
        }
    }

    //for vk
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private var callback =  object : VKAuthCallback {
        override fun onLogin(token: VKAccessToken) {
            helper.editId(token.userId?.toString() ?: "-1")
        }

        override fun onLoginFailed(errorCode: Int) {

        }
    }

    companion object {
        private const val KEY_MESSAGE = "message"

        fun createIntent(context: Context, id: Long) =
            Intent(context, MainActivity::class.java).apply {
                putExtra(KEY_MESSAGE, id)
            }
    }

}
