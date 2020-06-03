package kpfu.itis.myservice.features.feature_profile.presentation.edit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.item_profile_edit_info.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.ProfileInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.features.feature_profile.presentation.edit.dto.UserDto
import javax.inject.Inject

class EditFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    @Inject
    lateinit var helperSharedPreferences: HelperSharedPreferences

    private lateinit var navigator: INavigation
    private lateinit var toolbar: Toolbar
    private lateinit var menu: Menu
    private lateinit var viewModel: EditViewModel
    private lateinit var user : UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        ProfileInjector.plusEditComponent().inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        toolbar =  requireActivity().findViewById(R.id.toolbar_action)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
                setDisplayHomeAsUpEnabled(true)
            }
        }
        navigator = activity as MainActivity
        viewModel = getViewModel()
        toolbar.title = getString(R.string.edit)
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id = helperSharedPreferences.readID()?.toLong()
        listen(et_name, ti_name)
        listen(et_lastname, ti_lastname)
        listen(et_city, ti_city)
        id?.also {
            observes(id)
        }
    }

    private fun observes(id: Long) {
        observeProgressBar()
        viewModel.getUserProfile(id).observe(viewLifecycleOwner, Observer { res ->
            when {
                res.isSuccess -> {
                    res.getOrNull()?.apply {
                        Log.e("name-user", name)
                        user = UserDto()
                        user.name = name
                        user.lastName = lastName
                        user.photoUrl = photoURL
                        user.city = city
                        user.socialUrl = socialUrl
                        user.university = university
                        user.faculty = faculty
                        user.mobilePhone = mobilePhone
                        user.job = job
                        user.description = description
                        initEditText()
                    } ?:
                    toast("Произошла ошибка")
                }
                res.isFailure -> toast(res.exceptionOrNull()?.message ?: "Произошла ошибка")
                else -> toast("Произошла ошибка")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_action_edit_menu, menu)
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.tb_action_check -> {
                if (!isEmptyFields()) {
                    var userDto = getUserDto()
                    viewModel.updateUserProfile(userDto).observe(viewLifecycleOwner, Observer {
                        when {
                            it.isSuccess -> {
                                navigator.navigateBack()
                            }
                            it.isFailure -> toast(
                                it.exceptionOrNull()?.message ?: "Произошла ошибка"
                            )
                            else -> toast("Произошла ошибка")
                        }
                    })
                }
                true
            }
            else -> {
                navigator.navigateBack()
                super.onOptionsItemSelected(item)
            }

        }

    override fun onResume() {
        super.onResume()
        activity?.apply { nav_view.menu.getItem(4).isChecked = true }
    }

    override fun onDestroy() {
        ProfileInjector.clearEditComponent()
        (activity as AppCompatActivity).apply {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(false)
            }
        }
        super.onDestroy()
    }

    private fun observeProgressBar() {
        viewModel.isLoading().observe(viewLifecycleOwner, Observer {
            if (it && it != null) {
                sv_profile_edit.visibility = View.INVISIBLE
                pb_loading.visibility = View.VISIBLE
            } else {
                sv_profile_edit.visibility = View.VISIBLE
                pb_loading.visibility = View.INVISIBLE
            }
        })
    }

    private fun getUserDto() : UserDto =
        user.apply {
            Log.e("photoUrl", photoUrl)
            name = et_name.text.toString()
            lastName = et_lastname.text.toString()
            city = et_city.text.toString()
            socialUrl = et_social_url.text.toString()
            mobilePhone = et_contact.text.toString()
            university = et_study.text.toString()
            faculty = et_fac.text.toString()
            job = et_work.text.toString()
        }

    private fun initEditText() {
        user?.apply {
            et_name.setText(name, TextView.BufferType.EDITABLE)
            et_lastname.setText(lastName, TextView.BufferType.EDITABLE)
            et_city.setText(city, TextView.BufferType.EDITABLE)
            et_study.setText(university, TextView.BufferType.EDITABLE)
            et_fac.setText(faculty, TextView.BufferType.EDITABLE)
            et_work.setText(job, TextView.BufferType.EDITABLE)
            et_contact.setText(mobilePhone, TextView.BufferType.EDITABLE)
            et_social_url.setText(socialUrl, TextView.BufferType.EDITABLE)
        }
    }

    private fun isEmptyFields(): Boolean =
        isEmpty(et_name, ti_name) ||
        isEmpty(et_lastname, ti_lastname) ||
        isEmpty(et_city, ti_city)

    private fun isEmpty(et: EditText, ti: TextInputLayout) : Boolean =
        et.text.toString().run {
            if (isEmpty()) {
                ti.error = getString(R.string.field_empty)
                true
            } else {
                false
            }
        }

    private fun listen(et: EditText, ti : TextInputLayout) {
        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ti.error = ""
            }
        })
    }

    private fun toast(mes: String) {
        helperToastSnackbar.toast(this.context, mes)
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(EditViewModel::class.java)


    companion object {
        fun newInstance() : EditFragment = EditFragment()
    }

}
