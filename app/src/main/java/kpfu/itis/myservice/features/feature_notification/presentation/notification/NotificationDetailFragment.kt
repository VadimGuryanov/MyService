package kpfu.itis.myservice.features.feature_notification.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.NotificationInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.features.feature_add_service.presentation.service.ServiceFragment
import kpfu.itis.myservice.features.feature_profile.presentation.profile.NotFoundFragment
import kpfu.itis.myservice.features.feature_profile.presentation.profile.ProfileFragment
import javax.inject.Inject

class NotificationDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator: INavigation
    private lateinit var viewModel: NotificationDetailViewModel
    private var bottomNavigationView: BottomNavigationView? = null
    private var id : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        NotificationInjector.plusNotificationDetailComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notification, container, false)
        navigator = activity as MainActivity
        val toolbar =  activity?.findViewById<Toolbar>(R.id.toolbar_action)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
        toolbar?.setNavigationOnClickListener {
            navigator.navigateBack()
        }
        toolbar?.title = getString(R.string.service_name)
        activity?.apply { nav_view.menu.getItem(3).isChecked = true }
        bottomNavigationView = activity?.findViewById(R.id.nav_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        id = arguments?.getLong(ARG_ID)
        if (id ?: -1 > 0) {
            initNotification(id ?: -1)
            initRead(id ?: -1)
        } else {
            navigator.navigateBack()
            navigator.navigateTo(NotFoundFragment())
        }
    }

    private fun initRead(id: Long) {
        viewModel.read(id).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {}
                it.isFailure -> {
                    toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
                }
            }
        })
    }

    private fun initNotification(id: Long) {
        viewModel.getNotifications(id).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let { notification ->
                        initView(notification)
                    } ?: toast(null)
                }
                it.isFailure -> {
                    toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
                    navigator.navigateBack()
                }
            }
        })
    }

    private fun initView(notification: Notification) {
       notification.apply {
           tv_from_value.text = "$name $lastName"
           tv_title_value.text = if (title.length > 30) {
               title.substring(0, 27) + "..."
           } else {
               title
           }
           tv_data_value.text = data
           tv_message.text = if (message.isNotEmpty()) message else "*Пусто*"
           tv_title_value.setOnClickListener {
               navigator.navigateTo(ServiceFragment.newInstance(ser_id))
           }
           tv_from_value.setOnClickListener {
               navigator.navigateTo(ProfileFragment.newInstance(from_user_id, 3))
           }
       }
    }

    private fun toast(mess: String?) {
        helperToastSnackbar.toast(
            activity,
            mess ?: getString(R.string.service_err)
        )
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(NotificationDetailViewModel::class.java)

    override fun onDestroy() {
        NotificationInjector.clearNotificationDetailComponent()
        (activity as AppCompatActivity).apply {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(false)
                setDisplayShowHomeEnabled(false)
            }
        }
        super.onDestroy()
    }

    companion object {

        private const val ARG_ID = "id"

        fun newInstance(id: Long) : NotificationDetailFragment =
            NotificationDetailFragment().apply {
                Bundle().apply {
                    putLong(ARG_ID, id)
                    arguments = this
                }
            }
    }

}
