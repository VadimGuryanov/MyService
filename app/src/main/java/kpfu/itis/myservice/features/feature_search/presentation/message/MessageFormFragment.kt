package kpfu.itis.myservice.features.feature_search.presentation.message

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_send_message.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.SearchInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.Notification
import javax.inject.Inject

class MessageFormFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator: INavigation
    private lateinit var viewModel: MessageFormViewModel
    private lateinit var toolbar: Toolbar
    private lateinit var message: Notification
    private var serviceId: Long? = null
    private var toUserId: Long? = null
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        SearchInjector.plusMessageFormComponent().inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_send_message, container, false)
        navigator = activity as MainActivity
        toolbar =  requireActivity().findViewById(R.id.toolbar_action)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
                setDisplayHomeAsUpEnabled(true)
            }
        }
        toolbar.title = getString(R.string.title_send_message)
        activity?.apply { nav_view.menu.getItem(0).isChecked = true }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        serviceId = arguments?.getLong(ARG_SERVICE_ID) ?: -1
        toUserId = arguments?.getLong(ARG_TO_USER_ID) ?: -1
        title = arguments?.getString(ARG_TITLE) ?: ""
        tv_title_value.text = if (title?.length ?: -1 > 30) {
            title?.substring(0, 27) +  "..."
        } else {
            title ?: ""
        }
        message = Notification(
            ser_id = serviceId ?: -1,
            to_user_id = toUserId ?: -1,
            title = title ?: ""
        )
        getCurrantUser()
        getUser(toUserId ?: -1)
    }

    private fun getUser(toUserId: Long) {
        viewModel.getUser(toUserId).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    it.getOrNull().let {user ->
                        if (user != null) {
                            tv_to_value.text = ("${user.name} ${user.lastName}")
                        } else {
                            toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
                            navigator.navigateBack()
                        }
                    }
                }
                it.isFailure -> {
                    toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
                    navigator.navigateBack()
                }
            }
        })
    }

    private fun getCurrantUser() {
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    it.getOrNull().let {user ->
                        if (user != null) {
                            tv_from_value.text = ("${user.name} ${user.lastName}")
                            message.name = user.name
                            message.lastName = user.lastName
                            message.photoURL = user.photoURL
                        } else {
                            toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
                            navigator.navigateBack()
                        }
                    }
                }
                it.isFailure -> {
                    toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
                    navigator.navigateBack()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_send_message, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.tb_send_message -> {
                sendMessage()
                true
            }
            else -> {
                navigator.navigateBack()
                super.onOptionsItemSelected(item)
            }

        }

    private fun sendMessage() {
        viewModel.sendMessage(getMessage()).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let {
                        Log.e("mess", "send")
                        navigator.navigateBack()
                    } ?: toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
                }
                it.isFailure -> toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")

            }
        })
    }

    private fun toast(mess: String?) {
        helperToastSnackbar.toast(
            activity,
            mess ?: getString(R.string.service_err)
        )
    }

    private fun getMessage() : Notification {
        message.message = et_message.text.toString()
        return message
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(MessageFormViewModel::class.java)

    override fun onDestroy() {
        SearchInjector.clearMessageFormComponent()
        (activity as AppCompatActivity).apply {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(false)
            }
        }
        super.onDestroy()
    }

    companion object {

        private const val ARG_SERVICE_ID = "serviceId"
        private const val ARG_TO_USER_ID = "toUserId"
        private const val ARG_TITLE = "title"

        fun newInstance(serviceId: Long, toUserId: Long, title: String) : MessageFormFragment =
            MessageFormFragment().apply {
                Bundle().apply {
                    putLong(ARG_SERVICE_ID, serviceId)
                    putLong(ARG_TO_USER_ID, toUserId)
                    putString(ARG_TITLE, title)
                    arguments = this
                }
            }
    }


}