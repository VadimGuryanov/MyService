package kpfu.itis.myservice.features.feature_add_service.presentation.add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import kotlinx.android.synthetic.main.fragment_service_add.*
import kotlinx.android.synthetic.main.fragment_services.pb_loading
import kotlinx.android.synthetic.main.item_internet_problem.*
import kotlinx.android.synthetic.main.item_service_add_info.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.ServiceInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import javax.inject.Inject

class AddServiceFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator: INavigation
    private lateinit var viewModel: AddServiceViewModel
    private var service: Service? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ServiceInjector.plusAddServiceComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_service_add, container, false)
        navigator = activity as MainActivity
        var toolbar =  activity?.findViewById<Toolbar>(R.id.toolbar_action)
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
        toolbar?.title = getString(R.string.service_creating)
        activity?.apply { nav_view.menu.getItem(2).isChecked = true }
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getLong(ARG_ID) ?: -1
        viewModel = getViewModel()
        initView(id)
    }

    private fun initView(id: Long) {
        initButtons(id)
        initSpinner()
        observeLoading()
        if (id > 0) {
            btn_add.text = getString(R.string.update)
            observeService(id)
        } else {
            observeUser()
        }
        listen(et_title, ti_title)
        listen(et_specialty, ti_specialty)
        listen(et_name, ti_name)
        listen(et_lastname, ti_lastname)
        listen(et_city, ti_city)
    }

    private fun observeUser() {
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let {
                        initViewWithUser(it)
                        problemInternet(false)
                    } ?: helperToastSnackbar.toast(activity, "Ошибка при получении данных")
                }
                it.isFailure -> {
                    problemInternet(true)
                    helperToastSnackbar.toast(activity, "Ошибка при получении данных")
                }
            }
        })
    }

    private fun observeService(id : Long) {
        viewModel.getService(id).observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    it.getOrNull()?.let { ser ->
                        service = ser
                        problemInternet(false)
                        initViewWithService(ser)
                    } ?: helperToastSnackbar.toast(activity, "Ошибка при получении данных")
                }
                it.isFailure -> {
                    problemInternet(true)
                    helperToastSnackbar.toast(
                        activity,
                        it.exceptionOrNull()?.message ?:"Ошибка при создании услуги"
                    )
                }
            }
        })
    }

    private fun observeLoading() {
        viewModel.isLoading().observe(viewLifecycleOwner, Observer {
            if (it) {
                sv_service_add.visibility = View.GONE
                pb_loading.visibility = View.VISIBLE
            } else {
                sv_service_add.visibility = View.VISIBLE
                pb_loading.visibility = View.GONE
            }
        })
    }

    private fun initButtons(id: Long) {
        btn_repeat.setOnClickListener {
            initView(id)
        }
        btn_add.setOnClickListener {
            getService()?.apply {
                if (id < 0) {
                    addService(this)
                } else {
                    updateService(this)
                }
            }
        }
    }

    private fun addService(service: Service) {
        viewModel.addService(service).observe(viewLifecycleOwner, Observer {
            switch(it)
        })
    }

    private fun updateService(service: Service) {
        viewModel.updateService(service).observe(viewLifecycleOwner, Observer {
            switch(it)
        })
    }

    private fun switch(it: Result<Boolean>) {
        when {
            it.isSuccess -> {
                it.getOrNull()?.let {
                    navigator.navigateBack()
                } ?: helperToastSnackbar.toast(
                    activity,
                    "Ошибка при создании услуги"
                )
            }
            it.isFailure -> helperToastSnackbar.toast(
                activity,
                it.exceptionOrNull()?.message ?:"Ошибка при создании услуги"
            )
        }
    }

    private fun getService() : Service? =
        if (!isEmptyFields()) {
            Service(
                ser_id = service?.ser_id ?: -1,
                user_id = service?.user_id ?: -1,
                title = et_title.text.toString(),
                city = et_city.text.toString(),
                mobilePhone = et_contact.text.toString(),
                specialty = et_specialty.text.toString(),
                description = et_desc.text.toString(),
                socialUrl = et_social_url.text.toString(),
                cost = et_cost.text.toString(),
                currancy = sp_currancy.selectedItem.toString(),
                experience = et_experience.text.toString(),
                name = et_name.text.toString(),
                lastName = et_lastname.text.toString()
            )
        } else { null }

    private fun isEmptyFields(): Boolean =
        isEmpty(et_title, ti_title) ||
        isEmpty(et_specialty, ti_specialty) ||
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

    private fun initViewWithService(service: Service) {
        service.apply {
            et_title.setText(title, TextView.BufferType.EDITABLE)
            et_desc.setText(description, TextView.BufferType.EDITABLE)
            et_cost.setText(cost.toString(), TextView.BufferType.EDITABLE)
            et_experience.setText(experience.toString(), TextView.BufferType.EDITABLE)
            et_specialty.setText(specialty.toString(), TextView.BufferType.EDITABLE)
            sp_currancy.setSelection(getPositionCurrancy(currancy))
            et_name.setText(name, TextView.BufferType.EDITABLE)
            et_lastname.setText(lastName, TextView.BufferType.EDITABLE)
            et_city.setText(city, TextView.BufferType.EDITABLE)
            et_contact.setText(mobilePhone, TextView.BufferType.EDITABLE)
            et_social_url.setText(socialUrl, TextView.BufferType.EDITABLE)
        }
    }

    private fun initViewWithUser(user: User) {
        user.apply {
            et_name.setText(name, TextView.BufferType.EDITABLE)
            et_lastname.setText(lastName, TextView.BufferType.EDITABLE)
            et_city.setText(city, TextView.BufferType.EDITABLE)
            et_contact.setText(mobilePhone, TextView.BufferType.EDITABLE)
            et_social_url.setText(socialUrl, TextView.BufferType.EDITABLE)
        }
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currancy_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sp_currancy.adapter = adapter
        }
    }

    private fun problemInternet(isProblem: Boolean) {
        if (isProblem) {
            item_internet_problem.visibility = View.VISIBLE
            item_service_add_info.visibility = View.GONE
        } else {
            item_internet_problem.visibility = View.GONE
            item_service_add_info.visibility = View.VISIBLE
        }
    }

    private fun getPositionCurrancy(s: String?) =
        when(s) {
            getString(R.string.rub) -> 0
            getString(R.string.dollar) -> 1
            getString(R.string.eur) -> 2
            else -> 0
        }

    override fun onDestroy() {
        ServiceInjector.clearAddServiceComponent()
        (activity as AppCompatActivity).apply {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(false)
            }
        }
        super.onDestroy()
    }

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(AddServiceViewModel::class.java)

    companion object {

        private const val ARG_ID = "id"

        fun newInstance(id: Long) : AddServiceFragment =
            AddServiceFragment().apply {
                Bundle().apply {
                    putLong(ARG_ID, id)
                    arguments = this
                }
            }
    }

}
