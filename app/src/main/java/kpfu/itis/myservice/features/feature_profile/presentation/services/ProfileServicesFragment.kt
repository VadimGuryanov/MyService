package kpfu.itis.myservice.features.feature_profile.presentation.services

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_internet_problem.*
import kotlinx.android.synthetic.main.item_search_service.view.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.ProfileInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.features.feature_profile.presentation.profile.NotFoundFragment
import kpfu.itis.myservice.features.feature_search.presentation.message.MessageFormFragment
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceAdapter
import kpfu.itis.myservice.features.feature_search.presentation.service.ServiceDetailFragment
import javax.inject.Inject

class ProfileServicesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator: INavigation
    private lateinit var viewModel: ProfileServicesViewModel
    private var bottomNavigationView: BottomNavigationView? = null
    private var searchAdapter : SearchServiceAdapter? = null
    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ProfileInjector.plusProfileServicesComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        navigator = activity as MainActivity
        val toolbar =  activity?.findViewById<Toolbar>(R.id.toolbar_action)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_services)
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
        activity?.apply { nav_view.menu.getItem(0).isChecked = true }
        bottomNavigationView = activity?.findViewById(R.id.nav_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView?.visibility = View.VISIBLE
        viewModel = getViewModel()
        id = arguments?.getLong(ARG_ID) ?: -1
        if (id ?: -1 > 0) {
            observeLoading()
            observeServices()
            viewModel.getServices(id ?: -1)
            sr_list.setOnRefreshListener {
                viewModel.getServices(id ?: -1)
            }
            btn_repeat.setOnClickListener {
                viewModel.getServices(id ?: -1)
            }
        } else {
            navigator.navigateBack()
            navigator.navigateTo(NotFoundFragment())
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
        viewModel.services().observe(viewLifecycleOwner, Observer {
            sr_list.isRefreshing = false
            when {
                it.isSuccess ->
                    it.getOrNull()?.let { service ->
                        viewModel.getFavorites().observe(viewLifecycleOwner, Observer { favorite ->
                            when {
                                it.isSuccess -> {
                                    initList(service, favorite.getOrNull())
                                }
                                it.isFailure -> toast(it.exceptionOrNull()?.message)
                            }
                        })
                    } ?: toast(null)
                it.isFailure -> {
                    toast(it.exceptionOrNull()?.message)
                }
            }
        })
    }

    private fun initList(services: List<Service>, favorites: List<Favorite>?) {
        if (services.isEmpty()) {
            item_internet_problem.visibility = View.VISIBLE
            rv_service.visibility = View.GONE
        } else {
            item_internet_problem.visibility = View.GONE
            val servicesList = services.map { service ->
                favorites?.map { it.ser_id }?.let {
                    if (it.contains(service.ser_id)) {
                        service.isFavorite = true
                    }
                }
                service
            }
            rv_service.visibility = View.VISIBLE
            if (searchAdapter == null) {
                rv_service.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = SearchServiceAdapter(
                        servicesList,
                        viewModel.getId(),
                        { id, user_id, pos -> addFavorite(id, user_id, pos) },
                        { id, user_id, pos -> deleteFavorite(id, user_id, pos) },
                        {ser_id, user_id, title ->
                            bottomNavigationView?.visibility = View.GONE
                            navigator.navigateTo(MessageFormFragment.newInstance(ser_id, user_id, title))
                        })
                    { id, user_id, isFavorite ->
                        navigator.navigateTo(ServiceDetailFragment.newInstance(id, user_id, isFavorite))
                    }
                    searchAdapter = adapter as SearchServiceAdapter
                }
            } else {
                searchAdapter?.update(services)
            }
        }
    }

    private fun deleteFavorite(id : Long, userId: Long, position: Int) {
        if (!viewModel.isAuth()) {
            toast("Вы не авторизованы")
            rv_service.layoutManager?.findViewByPosition(position)?.also { view ->
                view.iv_star_empty.visibility = View.GONE
                view.iv_star.visibility = View.VISIBLE
            }
            searchAdapter?.notifyItemChanged(position)
            return
        }
        viewModel.deleteFavorite(id, userId).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {}
                it.isFailure -> {
                    rv_service.layoutManager?.findViewByPosition(position)?.also { view ->
                        view.iv_star_empty.visibility = View.GONE
                        view.iv_star.visibility = View.VISIBLE
                    }
                    searchAdapter?.notifyItemChanged(position)
                    toast("Произошла ошибка при удалении из понравившихся")
                }
            }
        })
    }

    private fun addFavorite(id : Long, userId: Long, position: Int) {
        if (!viewModel.isAuth()) {
            toast("Вы не авторизованы")
            rv_service.layoutManager?.findViewByPosition(position)?.also { view ->
                view.iv_star_empty.visibility = View.VISIBLE
                view.iv_star.visibility = View.GONE
            }
            searchAdapter?.notifyItemChanged(position)
            return
        }
        viewModel.addFavorite(id, userId).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {}
                it.isFailure -> {
                    rv_service.layoutManager?.findViewByPosition(position)?.also { view ->
                        view.iv_star_empty.visibility = View.VISIBLE
                        view.iv_star.visibility = View.GONE
                    }
                    searchAdapter?.notifyItemChanged(position)
                    toast("Произошла ошибка при добавлении в понравившиеся")
                }
            }
        })
    }

    private fun toast(mess: String?) {
        helperToastSnackbar.toast(
            activity,
            mess ?: getString(R.string.service_err)
        )
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(ProfileServicesViewModel::class.java)

    override fun onPause() {
        searchAdapter = null
        super.onPause()
    }

    override fun onDestroy() {
        ProfileInjector.clearProfileServicesComponent()
        (activity as AppCompatActivity).apply {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(false)
            }
        }
        super.onDestroy()
    }

    companion object {
        private const val ARG_ID = "id"

        fun newInstance(id: Long) : ProfileServicesFragment =
            ProfileServicesFragment().apply {
                Bundle().apply {
                    putLong(ARG_ID, id)
                    arguments = this
                }
            }
    }

}
