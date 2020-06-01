package kpfu.itis.myservice.features.feature_profile.presentation.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_auth.*
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.ProfileInjector
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.features.feature_profile.presentation.profile.ProfileFragment
import javax.inject.Inject

class AuthFragment : Fragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigation : INavigation
    private lateinit var viewModel : AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        ProfileInjector.plusAuthComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_auth, container, false)
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_action)
        (activity as AppCompatActivity).apply {
            supportActionBar?.apply {
                setSupportActionBar(toolbar)
                setDisplayHomeAsUpEnabled(false)
                setDisplayShowHomeEnabled(false)
            }
        }
        toolbar.title = "Авторизация"
        navigation = activity as MainActivity
        viewModel = getViewModel()
        Log.e("auth", viewModel.isAuth().toString())
        if (viewModel.isAuth()) {
            (activity as MainActivity).listenMessages()
            navigateTo(viewModel.getID())
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_auth.setOnClickListener {
            VK.login(this@AuthFragment.requireActivity(), arrayListOf(VKScope.WALL, VKScope.PHOTOS,VKScope.EMAIL))
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.apply { nav_view.menu.getItem(4).isChecked = true }
        var id = viewModel.getID()
        var err : Long = -1
        when(id) {
            err -> {}
            null -> {}
            else -> {
                getViewModel().authUserProfile().observe(viewLifecycleOwner, Observer {
                    when  {
                        it.isSuccess -> {
                            var res = it.getOrNull() ?: false
                            if (res) {
                                navigateTo(id)
                            } else {
                                helperToastSnackbar.toast(
                                    this.context,
                                    "Ошибка авторизации")
                            }
                        }
                        it.isFailure -> {
                            helperToastSnackbar.toast(
                                this.context,
                                it.exceptionOrNull()?.message ?: "Ошибка авторизации")
                        }
                    }
                })
            }
        }
    }

    private fun navigateTo(id: Long) {
        navigation.navigateBack()
        navigation.navigateTo(ProfileFragment.newInstance(id, null))
    }

    override fun onDestroy() {
        ProfileInjector.clearAuthComponent()
        super.onDestroy()
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)

    companion object {
        fun newInstance() : AuthFragment = AuthFragment()
    }

}
