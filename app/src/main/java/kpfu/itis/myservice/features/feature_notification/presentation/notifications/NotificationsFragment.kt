package kpfu.itis.myservice.features.feature_notification.presentation.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.item_not_auth.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.NotificationInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.features.feature_notification.presentation.notification.NotificationDetailFragment
import kpfu.itis.myservice.features.feature_notification.presentation.recycler.NotificationAdapter
import kpfu.itis.myservice.features.feature_profile.presentation.auth.AuthFragment
import javax.inject.Inject

class NotificationsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator: INavigation
    private lateinit var viewModel: NotificationsViewModel
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        NotificationInjector.plusNotificationComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        navigator = activity as MainActivity
        val toolbar =  activity?.findViewById<Toolbar>(R.id.toolbar_action)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_notifications)
        activity?.apply { nav_view.menu.getItem(3).isChecked = true }
        bottomNavigationView = activity?.findViewById(R.id.nav_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        observeLoading()
        if (viewModel.isAuth()) {
            item_not_auth.visibility = View.GONE
            item_notification_empty.visibility = View.GONE
            sr_list.visibility = View.GONE
            observeNotification()
        } else {
            item_not_auth.visibility = View.VISIBLE
            item_notification_empty.visibility = View.GONE
            sr_list.visibility = View.GONE
        }
        sr_list.setOnRefreshListener {
            observeNotification()
        }
        btn_auth.setOnClickListener {
            navigator.navigateTo(AuthFragment.newInstance())
        }
    }

    private fun observeLoading() {
        viewModel.isLoading().observe(viewLifecycleOwner, Observer {
            if (it) {
                cl_notification.visibility = View.GONE
                pb_loading.visibility = View.VISIBLE
            } else {
                cl_notification.visibility = View.VISIBLE
                pb_loading.visibility = View.GONE
            }
        })
    }

    private fun observeNotification() {
        viewModel.getNotifications().observe(viewLifecycleOwner, Observer {
            sr_list.isRefreshing = false
            when {
                it.isSuccess ->
                    it.getOrNull()?.let { list ->
                        initList(list)
                    } ?: toast(null)
                it.isFailure -> {
                    toast(it.exceptionOrNull()?.message)
                }
            }
        })
    }

    private fun initList(notifications: List<Notification>) {
        if (notifications.isEmpty()) {
            item_not_auth.visibility = View.GONE
            item_notification_empty.visibility = View.VISIBLE
            sr_list.visibility = View.GONE
        } else {
            sr_list.visibility = View.VISIBLE
            rv_notification.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = NotificationAdapter(
                    notifications.sortedBy { it.isRead },
                    { iv, url ->  viewModel.downloadPhoto(iv, url) })
                    { id, _, _ ->
                        navigator.navigateTo(NotificationDetailFragment.newInstance(id))
                    }
            }
        }
    }

    private fun toast(mess: String?) {
        helperToastSnackbar.toast(
            activity,
            mess ?: getString(R.string.service_err)
        )
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(NotificationsViewModel::class.java)

    override fun onDestroy() {
        NotificationInjector.apply {
            clearNotificationDataComponent()
            clearNotificationDomainComponent()
            clearNotificationComponent()
        }
        super.onDestroy()
    }

}
