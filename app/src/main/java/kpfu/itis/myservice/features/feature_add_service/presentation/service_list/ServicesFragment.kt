package kpfu.itis.myservice.features.feature_add_service.presentation.service_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_services.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.ServiceInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.features.feature_add_service.presentation.add.AddServiceFragment
import kpfu.itis.myservice.features.feature_add_service.presentation.recycler.ServiceAdapter
import kpfu.itis.myservice.features.feature_add_service.presentation.service.ServiceFragment
import kpfu.itis.myservice.features.feature_profile.presentation.auth.AuthFragment
import javax.inject.Inject

class ServicesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator: INavigation
    private lateinit var viewModel: ServicesViewModel
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ServiceInjector.plusServicesComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_services, container, false)
        navigator = activity as MainActivity
        var toolbar =  activity?.findViewById<Toolbar>(R.id.toolbar_action)
        toolbar?.title = getString(R.string.service)
        activity?.apply { nav_view.menu.getItem(2).isChecked = true }
        bottomNavigationView = activity?.findViewById(R.id.nav_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView?.visibility = View.VISIBLE
        viewModel = getViewModel()
        initListener()
        if (isAuth()) {
            observeLoading()
            observeServices()
        }
    }

    private fun observeLoading() {
        viewModel.isLoading().observe(viewLifecycleOwner, Observer {
            if (it) {
                cl_services.visibility = View.GONE
                pb_loading.visibility = View.VISIBLE
            } else {
                cl_services.visibility = View.VISIBLE
                pb_loading.visibility = View.GONE
            }
        })
    }

    private fun observeServices() {
        viewModel.getServices().observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess ->
                    it.getOrNull()?.let {
                        initList(it)
                    } ?: toast(null)
                it.isFailure -> toast(it.exceptionOrNull()?.message)
            }
        })
    }

    private fun initList(services: List<Service>) {
        sr_list.isRefreshing = false
        if (services.isEmpty()) {
            visible()
            return
        }
        rv_service.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ServiceAdapter(services) {
                navigator.navigateTo(ServiceFragment.newInstance(it))
            }
        }
    }

    private fun initListener() {
        fab_add_service.setOnClickListener {
            if (!viewModel.isAuth()) {
                navigator.navigateTo(AuthFragment.newInstance())
            } else {
                navigator.navigateTo(AddServiceFragment())
            }
        }
        sr_list.setOnRefreshListener {
            observeServices()
        }
    }

    private fun toast(mess: String?) {
        helperToastSnackbar.toast(
            activity,
            mess ?: getString(R.string.service_err)
        )
    }

    private fun isAuth() =
        if (!viewModel.isAuth()) {
            sr_list.visibility = View.GONE
            false
        } else {
            tv_empty_des.visibility = View.GONE
            tv_empty_title.visibility = View.GONE
            iv_empty.visibility = View.GONE
            true
        }

    private fun visible() {
        sr_list.visibility = View.GONE
        tv_empty_des.visibility = View.VISIBLE
        tv_empty_title.visibility = View.VISIBLE
        iv_empty.visibility = View.VISIBLE
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(ServicesViewModel::class.java)

    override fun onDestroy() {
        ServiceInjector.apply {
            clearServiceDataComponent()
            clearServiceDomainComponent()
            clearServicesComponent()
        }
        super.onDestroy()
    }

}
