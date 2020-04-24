package kpfu.itis.myservice.features.feature_profile.presentation.auth

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_auth.*
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.Injector
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.features.feature_profile.presentation.profile.ProfileFragment
import javax.inject.Inject

class AuthFragment : Fragment(){

    @Inject
    lateinit var helperSharedPreference: HelperSharedPreferences

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigation : INavigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Injector.plusAuthComponent().inject(this)
        val root = inflater.inflate(R.layout.fragment_auth, container, false)
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_action)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = "Авторизация"
        navigation = activity as MainActivity
        helperSharedPreference.readSession()?.apply {
            navigateTo()
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
        var id = helperSharedPreference.readID()
        when(id) {
            "-1" -> {}
            null -> {}
            else -> {
                getViewModel().authUserProfile().observe(viewLifecycleOwner, Observer {
                    when  {
                        it.data != null && it.data -> {
                            navigateTo()
                        }
                        it.error != null -> {
                            helperToastSnackbar.toast(
                                this.context,
                                it.error.message?: "Ошибка авторизации")
                        }
                        else -> {
                            helperToastSnackbar.toast(
                                this.context,
                                "Ошибка авторизации")
                        }
                    }
                })
            }
        }
    }

    private fun navigateTo() {
        navigation.popBackStack()
        navigation.navigateTo(ProfileFragment.newInstance())
//        findNavController().navigate(R.id.action_navigation_auth_to_navigation_profile, null)
//        Navigation.createNavigateOnClickListener(R.id.action_navigation_auth_to_navigation_profile)
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)

    companion object {
        fun newInstance() : AuthFragment = AuthFragment()
    }

}
