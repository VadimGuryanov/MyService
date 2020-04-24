package kpfu.itis.myservice.features.feature_profile.presentation.about_me

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_about_me.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.Injector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.features.feature_profile.presentation.profile.ProfileFragment
import javax.inject.Inject

class AboutMeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator: INavigation
    private lateinit var toolbar: Toolbar
    private lateinit var menu: Menu
    private var text : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Injector.plusAboutMeComponent().inject(this)
        var root = inflater.inflate(R.layout.fragment_about_me, container, false)
        navigator = activity as MainActivity
        toolbar =  requireActivity().findViewById(R.id.toolbar_action)
            (activity as AppCompatActivity).apply {
                setSupportActionBar(toolbar)
                supportActionBar?.apply {
                    setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
                    setDisplayHomeAsUpEnabled(true)
                }
            }
        toolbar.title = getString(R.string.about)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_action_menu, menu)
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.tb_action_check -> {
                Log.e("text2", text)
                getViewModel().addDescription(text).observe(viewLifecycleOwner, Observer {
                    when {
                        it.data != null && it.data -> {
                            navigator.navigateToNotAdd(ProfileFragment.newInstance())
                        }
                        it.error != null -> toast(it.error.message ?: "Произошла ошибка")
                        else -> toast("Произошла ошибка")
                    }
                })
                true
            }
            R.id.tb_action_non_check -> {
                false
            }
            else -> {
                navigator.navigateToNotAdd(ProfileFragment.newInstance())
                super.onOptionsItemSelected(item)
            }

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_about.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                text = s.toString()
                Log.e("text", text)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length != 0 && s != null) {
                    menu.findItem(R.id.tb_action_non_check).isVisible = false
                    menu.findItem(R.id.tb_action_check).isVisible = true
                } else {
                    menu.findItem(R.id.tb_action_non_check).isVisible = true
                    menu.findItem(R.id.tb_action_check).isVisible = false
                }
                if (100 <= s?.length ?: 0) {
                    et_about.textSize = 16f
                }
            }

        })
    }

    override fun onDestroy() {
        (activity as AppCompatActivity).apply {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(false)
            }
        }
        super.onDestroy()
    }

    private fun toast(mes: String) {
        helperToastSnackbar.toast(this.context, mes)
    }

    private fun getViewModel() = ViewModelProvider(this ,viewModelFactory).get(AboutMeViewModel::class.java)

    companion object {
        fun newInstance() : AboutMeFragment =
            AboutMeFragment()
    }

}
