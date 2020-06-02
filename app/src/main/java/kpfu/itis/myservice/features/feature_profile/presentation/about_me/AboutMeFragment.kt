package kpfu.itis.myservice.features.feature_profile.presentation.about_me

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_about_me.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.ProfileInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
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
        ProfileInjector.plusAboutMeComponent().inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_action_menu, menu)
        this.menu = menu
        arguments?.getString(ARG_DESCRIPTION)?.also {
            if (it.length >= 100) et_about.textSize = 16f
            menu.findItem(R.id.tb_action_non_check).isVisible = false
            menu.findItem(R.id.tb_action_check).isVisible = true
            et_about.setText(it, TextView.BufferType.EDITABLE)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.tb_action_check -> {
                getViewModel().addDescription(et_about.text.toString()).observe(viewLifecycleOwner, Observer {
                    when {
                        it.isSuccess -> {
                            navigator.navigateBack()
                        }
                        it.isFailure -> toast(it.exceptionOrNull()?.message ?: "Произошла ошибка")
                        else -> toast("Произошла ошибка")
                    }
                })
                true
            }
            R.id.tb_action_non_check -> false
            else -> {
                navigator.navigateBack()
                super.onOptionsItemSelected(item)
            }

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_about.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                text = s.toString()
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
                } else {
                    et_about.textSize = 24f
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        activity?.apply { nav_view.menu.getItem(4).isChecked = true }
    }

    override fun onDestroy() {
        ProfileInjector.clearAboutMeComponent()
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

        private const val ARG_DESCRIPTION = "description"

        fun newInstance(description: String?) : AboutMeFragment =
            AboutMeFragment().apply {
                Bundle().apply {
                    putString(ARG_DESCRIPTION, description)
                    arguments = this
                }
            }
    }

}
