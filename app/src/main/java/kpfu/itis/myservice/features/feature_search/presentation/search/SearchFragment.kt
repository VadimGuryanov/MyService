package kpfu.itis.myservice.features.feature_search.presentation.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import kpfu.itis.myservice.app.di.injectors.SearchInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.features.feature_search.presentation.message.MessageFormFragment
import kpfu.itis.myservice.features.feature_search.presentation.recycler.SearchServiceAdapter
import kpfu.itis.myservice.features.feature_search.presentation.service.ServiceDetailFragment
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator: INavigation
    private lateinit var viewModel: SearchViewModel
    private var bottomNavigationView: BottomNavigationView? = null
    private var searchAdapter : SearchServiceAdapter? = null
    private var searchText: String? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        SearchInjector.plusSearchComponent().inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        toolbar?.title = getString(R.string.title_search)
        activity?.apply { nav_view.menu.getItem(0).isChecked = true }
        bottomNavigationView = activity?.findViewById(R.id.nav_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView?.visibility = View.VISIBLE
        viewModel = getViewModel()
        observeLoading()
        observeServices()
        viewModel.getServices()
        sr_list.setOnRefreshListener {
            searchView?.setQuery("", false)
            searchView?.clearFocus()
            searchText = null
            viewModel.getServices()
        }
        btn_repeat.setOnClickListener {
            searchView?.setQuery("", false)
            searchView?.clearFocus()
            searchText = null
            viewModel.getServices()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_view, menu)
        val searchItem: MenuItem = menu.findItem(R.id.tb_search)
        searchView = searchItem.actionView as SearchView
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView?.maxWidth = Integer.MAX_VALUE
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getServices(query ?: "")
                searchText = query
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean = false
        })
        return super.onCreateOptionsMenu(menu, inflater)
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
                                    Log.e("favorite", favorite.getOrNull()?.size.toString())
                                    initList(service, favorite.getOrNull())
                                }
                                it.isFailure -> toast(it.exceptionOrNull()?.message)
                            }
                        })
                    } ?: toast(null)
                it.isFailure -> {
                    Log.e("searcherrror", it.exceptionOrNull()?.message ?: "")
                    toast(it.exceptionOrNull()?.message)
                }
            }
        })
    }

    private fun initList(services: List<Service>, favorites: List<Favorite>?) {
        if (services.isEmpty()) {
            if (searchText == null) {
                item_internet_problem.visibility = View.VISIBLE
                rv_service.visibility = View.GONE
                item_search_empty.visibility = View.GONE
            } else {
                item_search_empty.visibility = View.VISIBLE
                item_internet_problem.visibility = View.GONE
                rv_service.visibility = View.GONE
            }
        } else {
            item_internet_problem.visibility = View.GONE
            item_search_empty.visibility = View.GONE
            val servicesList = services.map { service ->
                favorites?.map { it.ser_id }?.let {
                    if (it.contains(service.ser_id)) {
                        service.isFavorite = true
                    }
                }
                Log.e(service.specialty, service.isFavorite.toString())
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
                Log.e("update", services.size.toString())
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
                    Log.e("failer", it.exceptionOrNull()?.message ?: "")
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
                    Log.e("failer", it.exceptionOrNull()?.message ?: "")
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

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

    override fun onPause() {
        searchAdapter = null
        super.onPause()
    }

    override fun onDestroy() {
        SearchInjector.apply {
            clearSearchDataComponent()
            clearSearchDomainComponent()
            clearSearchComponent()
        }
        super.onDestroy()
    }

}
