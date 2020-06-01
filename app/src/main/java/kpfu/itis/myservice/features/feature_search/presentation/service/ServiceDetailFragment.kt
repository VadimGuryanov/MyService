package kpfu.itis.myservice.features.feature_search.presentation.service

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_service.*
import kotlinx.android.synthetic.main.item_author_link.*
import kotlinx.android.synthetic.main.item_service_contacts.*
import kotlinx.android.synthetic.main.item_service_description.*
import kotlinx.android.synthetic.main.item_service_detail_info.*
import kotlinx.android.synthetic.main.item_service_header.*
import kotlinx.android.synthetic.main.item_service_header.tv_title
import kotlinx.android.synthetic.main.item_service_info.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.SearchInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.features.feature_add_service.presentation.service.ServiceFragment
import kpfu.itis.myservice.features.feature_profile.presentation.profile.NotFoundFragment
import kpfu.itis.myservice.features.feature_profile.presentation.profile.ProfileFragment
import kpfu.itis.myservice.features.feature_search.presentation.message.MessageFormFragment
import javax.inject.Inject

class ServiceDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator: INavigation
    private lateinit var viewModel: ServiceDetailViewModel
    private var bottomNavigationView: BottomNavigationView? = null
    private lateinit var menu: Menu
    private var id : Long? = null
    private var userId : Long? = null
    private var isFav : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        SearchInjector.plusServiceDetailComponent().inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_service, container, false)
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
        activity?.apply { nav_view.menu.getItem(0).isChecked = true }
        bottomNavigationView = activity?.findViewById(R.id.nav_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        id = arguments?.getLong(ARG_ID)
        userId = arguments?.getLong(ARG_USER_ID)
        isFav = arguments?.getBoolean(ARG_IS_FAVORITE) ?: false
        bottomNavigationView?.visibility = View.VISIBLE
        if (viewModel.isEqualsAuthor(userId ?: -2)) {
            navigator.navigateBack()
            navigator.navigateTo(ServiceFragment.newInstance(id ?: -1))
        } else {
            if (id ?: -1 > 0 && userId ?: -1 > 0) {
                initObservers(id ?: -1, userId ?: -1)
            } else {
                navigator.navigateBack()
                navigator.navigateTo(NotFoundFragment())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_service_detail, menu)
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.tb_star_empty -> {
                addFavorite()
                true
            }
            R.id.tb_star -> {
                deleteFavorite()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    private fun addFavorite() {
        viewModel.addFavorite(id ?: -1, userId ?: -1).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let { isAdd ->
                        if (isAdd) {
                            menu.findItem(R.id.tb_star).isVisible = true
                            menu.findItem(R.id.tb_star_empty).isVisible = false
                        }
                    } ?: toast(null)
                }
                it.isFailure -> toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
            }
        })
    }

    private fun deleteFavorite() {
        viewModel.deleteFavorite(id ?: -1, userId ?: -1).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let { isAdd ->
                        if (isAdd) {
                            menu.findItem(R.id.tb_star).isVisible = false
                            menu.findItem(R.id.tb_star_empty).isVisible = true
                        }
                    } ?: toast(null)
                }
                it.isFailure -> toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
            }
        })
    }

    private fun initObservers(id: Long, userId: Long) {
        initLoading()
        initService(id, userId)
    }

    private fun initService(id: Long, userId: Long) {
        viewModel.getService(id, userId).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let { service ->
                        initView(service)
                    } ?: toast(null)
                }
                it.isFailure -> toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
            }
        })
    }

    private fun initLoading() {
        viewModel.isLoading().observe(viewLifecycleOwner, Observer {
            if (it) {
                sv_service.visibility = View.GONE
                pb_loading.visibility = View.VISIBLE
            } else {
                sv_service.visibility = View.VISIBLE
                pb_loading.visibility = View.GONE
            }
        })
    }

    private fun initView(service: Service) {
        service.apply {
            tv_title.text = title
            tv_city.text = city ?: "Не указано"
            tv_cost.text = if (cost != null && cost != "") {
                "$cost $currancy"
            } else {
                "не указано"
            }
            if (name == null || lastName == null || name == "" || lastName == "") {
                tv_author_value.text = "не указано"
            } else {
                visible(tv_author, tv_author_value, false, "$name $lastName")
            }
            if (specialty == null || specialty == "") {
                visible(tv_specialty, tv_specialty_value, true, null)
            } else {
                visible(tv_specialty, tv_specialty_value, false, specialty)
            }
            if (experience == null || experience == "") {
                visible(tv_experience, tv_experience_value, true, null)
            } else {
                visible(tv_experience, tv_experience_value, false, experience)
            }
            if (mobilePhone == null || mobilePhone == "") {
                visible(tv_tel, iv_tel, true, null)
            } else {
                visible(tv_tel, iv_tel, false, mobilePhone)
                var mobileIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$mobilePhone"))
                var mobileListener = View.OnClickListener {
                    startActivity(mobileIntent)
                }
                tv_tel.setOnClickListener(mobileListener)
                iv_tel.setOnClickListener(mobileListener)
            }
            if (socialUrl == null || socialUrl == "") {
                visible(tv_url, iv_url, true, null)
            } else {
                visible(tv_url, iv_url, false, socialUrl)
                var browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(socialUrl)))
                var urlListener = View.OnClickListener {
                    startActivity(browserIntent)
                }
                tv_url.setOnClickListener(urlListener)
                iv_url.setOnClickListener(urlListener)
            }
            tv_description.text = "$description"
            tv_data_value.text = "$date"
            if (!viewModel.isAuth()) {
                btn_hire.visibility = View.GONE
                view_line.visibility = View.GONE
                menu.findItem(R.id.tb_star).isVisible = false
                menu.findItem(R.id.tb_star_empty).isVisible = false
            } else if (isFav) {
                btn_hire.visibility = View.VISIBLE
                menu.findItem(R.id.tb_star).isVisible = true
                menu.findItem(R.id.tb_star_empty).isVisible = false
            } else {
                btn_hire.visibility = View.VISIBLE
                menu.findItem(R.id.tb_star).isVisible = false
                menu.findItem(R.id.tb_star_empty).isVisible = true
            }
            btn_hire.setOnClickListener {
                bottomNavigationView?.visibility = View.GONE
                navigator.navigateTo(MessageFormFragment.newInstance(ser_id, userId ?: -1, title ?: ""))
            }
            id_author_link.setOnClickListener {
                navigator.navigateTo(ProfileFragment.newInstance(user_id, 0))
            }
        }
    }

    private fun toast(mess: String?) {
        helperToastSnackbar.toast(
            activity,
            mess ?: getString(R.string.service_err)
        )
    }

    private fun visible(tv: TextView, iv: ImageView, isNull: Boolean, str: String?) {
        if (isNull) {
            tv.visibility = View.GONE
            iv.visibility = View.GONE
        } else {
            tv.text = "$str"
            tv.visibility = View.VISIBLE
            iv.visibility = View.VISIBLE
        }
    }

    private fun visible(tv_title: TextView, tv: TextView, isNull: Boolean, str: String?) {
        if (isNull) {
            tv_title.visibility = View.GONE
            tv.visibility = View.GONE
        } else {
            tv.text = "$str"
            tv_title.visibility = View.VISIBLE
            tv.visibility = View.VISIBLE
        }
    }

    private fun getUrl(socialUrl: String?) =
        socialUrl?.run {
            if (!startsWith("http://") && !startsWith("https://")) {
                "http://$this"
            } else {
                this
            }
        }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(ServiceDetailViewModel::class.java)

    override fun onDestroy() {
        SearchInjector.clearServiceDetailComponent()
        (activity as AppCompatActivity).apply {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(false)
            }
        }
        super.onDestroy()
    }

    companion object {

        private const val ARG_ID = "id"
        private const val ARG_USER_ID = "user_id"
        private const val ARG_IS_FAVORITE = "isFavorite"

        fun newInstance(id: Long, userId: Long, isFavorite: Boolean) : ServiceDetailFragment =
            ServiceDetailFragment().apply {
                Bundle().apply {
                    putLong(ARG_ID, id)
                    putLong(ARG_USER_ID, userId)
                    putBoolean(ARG_IS_FAVORITE, isFavorite)
                    arguments = this
                }
            }
    }

}
