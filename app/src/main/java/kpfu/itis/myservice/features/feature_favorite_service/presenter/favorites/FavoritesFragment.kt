package kpfu.itis.myservice.features.feature_favorite_service.presenter.favorites

import android.content.Intent
import android.net.Uri
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
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.FavoritesInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.features.feature_favorite_service.presenter.recycler.FavoriteAdapter
import kpfu.itis.myservice.features.feature_profile.presentation.auth.AuthFragment
import kpfu.itis.myservice.features.feature_search.presentation.service.ServiceDetailFragment
import javax.inject.Inject

class FavoritesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator: INavigation
    private lateinit var viewModel: FavoritesViewModel
    private var bottomNavigationView: BottomNavigationView? = null
    private var favoriteAdapter : FavoriteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        FavoritesInjector.plusFavoritesComponent().inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        navigator = activity as MainActivity
        val toolbar =  activity?.findViewById<Toolbar>(R.id.toolbar_action)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_favorite_services)
        activity?.apply { nav_view.menu.getItem(1).isChecked = true }
        bottomNavigationView = activity?.findViewById(R.id.nav_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        observeLoading()
        if (viewModel.isAuth()) {
            item_not_auth.visibility = View.GONE
            item_favorites_empty.visibility = View.GONE
            sr_list.visibility = View.GONE
            observeServices()
        } else {
            item_not_auth.visibility = View.VISIBLE
            item_favorites_empty.visibility = View.GONE
            sr_list.visibility = View.GONE
        }
        sr_list.setOnRefreshListener {
            observeServices()
        }
        btn_auth.setOnClickListener {
            navigator.navigateTo(AuthFragment.newInstance())
        }
    }

    private fun observeLoading() {
        viewModel.isLoading().observe(viewLifecycleOwner, Observer {
            if (it) {
                cl_favorites.visibility = View.GONE
                pb_loading.visibility = View.VISIBLE
            } else {
                cl_favorites.visibility = View.VISIBLE
                pb_loading.visibility = View.GONE
            }
        })
    }

    private fun observeServices() {
        viewModel.getFavorites().observe(viewLifecycleOwner, Observer {
            sr_list.isRefreshing = false
            when {
                it.isSuccess ->
                    it.getOrNull()?.let { favorite ->
                        initList(favorite)
                    } ?: toast(null)
                it.isFailure -> {
                    toast(it.exceptionOrNull()?.message)
                }
            }
        })
    }

    private fun initList(favorites: List<Favorite>) {
        if (favorites.isEmpty()) {
            item_not_auth.visibility = View.GONE
            item_favorites_empty.visibility = View.VISIBLE
            sr_list.visibility = View.GONE
        } else {
            sr_list.visibility = View.VISIBLE
            if (favoriteAdapter == null) {
                rv_service.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = FavoriteAdapter(
                        favorites,
                        {id, pos -> deleteFavorite(id, pos)},
                        {tel -> startTel(tel)},
                        {url -> startUrl(url)})
                        { id, user_id, isFavorite ->
                            navigator.navigateTo(ServiceDetailFragment.newInstance(id, user_id, isFavorite))
                        }
                    favoriteAdapter = adapter as FavoriteAdapter
                }
            } else {
                favoriteAdapter?.update(favorites)
            }
        }
    }

    private fun deleteFavorite(id : Long, position: Int) {
        viewModel.deleteFavorite(id).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    favoriteAdapter?.let {
                        it.delete(position)
                        if (it.getSize() == 0) {
                            item_not_auth.visibility = View.GONE
                            item_favorites_empty.visibility = View.VISIBLE
                            sr_list.visibility = View.GONE
                        }
                    } ?: toast("Произошла ошибка при удалении из понравившихся")

                }
                it.isFailure -> {
                    toast("Произошла ошибка при удалении из понравившихся")
                }
            }
        })
    }

    private fun startTel(mobilePhone : String) {
        var mobileIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$mobilePhone"))
        startActivity(mobileIntent)
    }

    private fun startUrl(socialUrl : String) {
        var browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(socialUrl)))
        startActivity(browserIntent)
    }

    private fun toast(mess: String?) {
        helperToastSnackbar.toast(
            activity,
            mess ?: getString(R.string.service_err)
        )
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(FavoritesViewModel::class.java)

    private fun getUrl(socialUrl: String?) =
        socialUrl?.run {
            if (!startsWith("http://") && !startsWith("https://")) {
                "http://$this"
            } else {
                this
            }
        }

    override fun onPause() {
        favoriteAdapter = null
        super.onPause()
    }

    override fun onDestroy() {
        FavoritesInjector.apply {
            clearFavoritesDataComponent()
            clearFavoritesDomainComponent()
            clearFavoritesComponent()
        }
        super.onDestroy()
    }

}
