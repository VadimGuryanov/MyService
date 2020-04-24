package kpfu.itis.myservice.features.feature_profile.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_profile_description.*
import kotlinx.android.synthetic.main.item_profile_description_empty.*
import kotlinx.android.synthetic.main.item_profile_header.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.Injector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.UserLocal
import kpfu.itis.myservice.features.feature_profile.presentation.about_me.AboutMeFragment
import javax.inject.Inject


class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigation : INavigation
    private var bottomNavigationView : BottomNavigationView? = null
    private lateinit var toolbar : Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Injector.plusProfileComponent().inject(this)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        toolbar =  requireActivity().findViewById(R.id.toolbar_action)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        navigation = (activity as MainActivity)
        initProfile()
        bottomNavigationView = activity?.findViewById(R.id.nav_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_say.setOnClickListener {
            bottomNavigationView?.visibility = View.GONE
            navigation.navigateToNotAdd(AboutMeFragment.newInstance())
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView?.visibility = View.VISIBLE
    }

    private fun initProfile() {
        getViewModel().let {
            observeProgressBar(it)
            observeGetUser(it)
        }
    }

    private fun observeGetUser(viewModel: ProfileViewModel) {
        viewModel.getUserProfile().observe(viewLifecycleOwner, Observer {
            Log.e("user", it.toString())
            when  {
                it.data != null -> {
                    viewModel.download(iv_profile, it.data.photoURL)
                    setUserView(it.data)
                }
                it.error != null -> {
                    helperToastSnackbar.snackbar(
                        activity as MainActivity,
                        it.error.message ?: "Ошибка получшения данных")
                }
                else -> {
                    helperToastSnackbar.snackbar(
                        activity as MainActivity,
                        "Ошибка получшения данных")
                }
            }
        })
    }

    private fun observeProgressBar(viewModel: ProfileViewModel) {
        viewModel.isLoading().observe(viewLifecycleOwner, Observer {
            Log.e("pb","$it")
            if (it && it != null) {
                sv_profile.visibility = View.INVISIBLE
                pb_loading.visibility = View.VISIBLE
            } else {
                sv_profile.visibility = View.VISIBLE
                pb_loading.visibility = View.INVISIBLE
            }
        })
    }

    private fun setUserView(user: UserLocal) {
        user.apply {
            toolbar.title = "$name $lastName"
            tv_name.text = name
            tv_surname.text = lastName
            tv_home.text = city
            visibility(tv_study, iv_study,university)
            visibility(tv_work, iv_work, job)
            if (faculty == null) {
                tv_fac.visibility = View.GONE
            } else {
                tv_fac.visibility = View.VISIBLE
                tv_fac.text = faculty
            }
            description?.let {
                visibleDescriptionLayout(it)
            } ?: visibleDescriptionEmptyLayout()
        }
    }

    private fun visibleDescriptionLayout(description: String) {
        item_profile_description_empty.visibility = View.GONE
        item_profile_description.visibility = View.VISIBLE
        tv_about_me_description.text = description
    }

    private fun visibleDescriptionEmptyLayout() {
        item_profile_description_empty.visibility = View.VISIBLE
        item_profile_description.visibility = View.GONE
    }

    private fun visibility(tv : TextView, iv: ImageView, name: String?) {
        if (name == null) {
            tv.visibility = View.GONE
            iv.visibility = View.GONE
        } else {
            tv.visibility = View.VISIBLE
            iv.visibility = View.VISIBLE
            tv.text = name
        }
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

    override fun onDestroy() {
        Injector.clearProfileComponent()
        super.onDestroy()
    }

    companion object {
        fun newInstance() : ProfileFragment = ProfileFragment()
    }

}
