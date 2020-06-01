package kpfu.itis.myservice.features.feature_profile.presentation.profile

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_contacts.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_profile_description.*
import kotlinx.android.synthetic.main.item_profile_description_empty.*
import kotlinx.android.synthetic.main.item_profile_header.*
import kpfu.itis.myservice.R
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.ViewModelFactory
import kpfu.itis.myservice.app.di.injectors.ProfileInjector
import kpfu.itis.myservice.app.navigation.INavigation
import kpfu.itis.myservice.common.HelperToastSnackbar
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_profile.presentation.about_me.AboutMeFragment
import kpfu.itis.myservice.features.feature_profile.presentation.auth.AuthFragment
import kpfu.itis.myservice.features.feature_profile.presentation.edit.EditFragment
import kpfu.itis.myservice.features.feature_profile.presentation.services.ProfileServicesFragment
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helperToastSnackbar: HelperToastSnackbar

    private lateinit var navigator : INavigation
    private var profileMenu : Menu? = null
    private var bottomNavigationView : BottomNavigationView? = null
    private lateinit var toolbar : Toolbar
    private lateinit var viewModel : ProfileViewModel
    private var id : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ProfileInjector.plusProfileComponent().inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        toolbar =  requireActivity().findViewById(R.id.toolbar_action)
        bottomNavigationView = activity?.findViewById(R.id.nav_view)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        navigator = (activity as MainActivity)
        viewModel = getViewModel().run {
            profileLiveData.observe(viewLifecycleOwner, userObserver)
            loadingLiveData.observe(viewLifecycleOwner, loadingObserver)
            this
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView?.visibility = View.VISIBLE
        id =  arguments?.getLong(ARG_ID)
        arguments?.getInt(ARG_NAV_VIEW_POS).let {
            if (it != 4) {
                (activity as AppCompatActivity).apply {
                    supportActionBar?.apply {
                        setDisplayHomeAsUpEnabled(true)
                        setDisplayShowHomeEnabled(true)
                    }
                }
                toolbar.setNavigationOnClickListener {
                    navigator.navigateBack()
                }
            } else {
                (activity as AppCompatActivity).apply {
                    supportActionBar?.apply {
                        setDisplayHomeAsUpEnabled(false)
                        setDisplayShowHomeEnabled(false)
                    }
                }
            }
            activity?.apply { nav_view.menu.getItem(it ?: 4).isChecked = true }
        }
        id?.let {
            initProfile()
        } ?: navigator.navigateTo(NotFoundFragment.newInstance())
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = requireActivity().menuInflater
        inflater.inflate(R.menu.menu_description, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tb_edit -> {
                bottomNavigationView?.visibility = View.GONE
                navigator.navigateTo(AboutMeFragment.newInstance(tv_description.text.toString()))
                true
            }
            R.id.tb_delete -> {
                viewModel.deleteDescription().observe(viewLifecycleOwner, deleteDescriptionObserver)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        profileMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.tb_exit -> {
                AlertDialog.Builder(activity)
                    .setNegativeButton("Нет") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Да") { dialog, _ ->
                        exit()
                        dialog.dismiss()
                    }
                    .setMessage("Вы действительно хотите выйти?")
                    .create()
                    .show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    private fun initProfile() {
        id?.let {
            if (it < 0) {
                navigator.navigateTo(NotFoundFragment.newInstance())
            } else {
                viewModel.getUserProfile(it)
            }
        }
    }

    private fun exit() {
        getViewModel().exit().observe(viewLifecycleOwner, Observer {
            when {
                it.isSuccess -> {
                    navigator.navigateBack()
                    navigator.navigateTo(AuthFragment.newInstance())
                }
                it.isFailure -> helperToastSnackbar.toast(
                    activity as MainActivity,
                    it.exceptionOrNull()?.message ?: "Ошибка"
                )
            }
        })
    }

    private val userObserver = Observer<Result<User>> {
        when  {
            it.isSuccess -> {
                it.getOrNull()?.also { data ->
                    viewModel.download(iv_profile, data.photoURL)
                    setUserView(data)
                }
            }
            it.isFailure -> {
                    helperToastSnackbar.toast(
                        activity as MainActivity,
                        it.exceptionOrNull()?.message ?: "Ошибка"
                    )
            }
        }
    }

    private val loadingObserver =  Observer<Boolean> {
        if (it && it != null) {
            sv_profile.visibility = View.INVISIBLE
            pb_loading.visibility = View.VISIBLE
        } else {
            sv_profile.visibility = View.VISIBLE
            pb_loading.visibility = View.INVISIBLE
        }
    }

    private val deleteDescriptionObserver =  Observer<Result<Boolean>> {
        when {
            it.isSuccess -> {
                item_profile_description_empty.visibility = View.VISIBLE
                item_profile_description.visibility = View.GONE
            }
            it.isFailure -> {
                item_profile_description_empty.visibility = View.GONE
                item_profile_description.visibility = View.VISIBLE
                helperToastSnackbar.toast(
                    activity as MainActivity,
                    it.exceptionOrNull()?.message ?: "Ошибка")
            }
            else -> helperToastSnackbar.toast(activity as MainActivity, "Произошла ошибка")
        }
    }

    private fun setUserView(user: User) {
        user.apply {
            toolbar.title = "$name $lastName"
            tv_name.text = name
            tv_surname.text = lastName
            visibility(tv_home, iv_home, city)
            visibility(tv_experience_value, tv_experience,university)
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
            initBottomSheetView(user)
            if (viewModel.getId() == vk_id) {
                btn_say.setOnClickListener {
                    navigator.navigateTo(AboutMeFragment.newInstance(null))
                }
                btn_action.let {
                    it.text = getString(R.string.edit)
                    it.setOnClickListener {
                        navigator.navigateTo(EditFragment.newInstance())
                    }
                }
                registerForContextMenu(iv_actions)
                profileMenu?.findItem(R.id.tb_exit)?.isVisible = true
            } else {
                profileMenu?.findItem(R.id.tb_exit)?.isVisible = false
                iv_actions.visibility = View.GONE
                item_profile_description_empty.visibility = View.GONE
                btn_action.let {
                    it.text = getString(R.string.service)
                    it.setOnClickListener {
                        navigator.navigateTo(ProfileServicesFragment.newInstance(vk_id))
                    }
                }
            }
        }
    }

    private fun visibleDescriptionLayout(description: String) {
        item_profile_description_empty.visibility = View.GONE
        item_profile_description.visibility = View.VISIBLE
        tv_description.text = description.let {
            if (it.length > 100) {
                tv_description.textSize = 16f
                it
            } else it
        }
    }

    private fun visibleDescriptionEmptyLayout() {
        item_profile_description_empty.visibility = View.VISIBLE
        item_profile_description.visibility = View.GONE
    }

    private fun visibility(tv : TextView, iv: ImageView, name: String?) =
        if (name == null || name.isEmpty()) {
            tv.visibility = View.GONE
            iv.visibility = View.GONE
            false
        } else {
            tv.visibility = View.VISIBLE
            iv.visibility = View.VISIBLE
            tv.text = name
            true
        }

    private fun initBottomSheetView(user: User) {
        var bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        btn_contact.setOnClickListener {
            val bottomSheetView = LayoutInflater.from(activity?.applicationContext)
                .inflate(
                    R.layout.bottom_sheet_contacts,
                    activity?.findViewById(R.id.bs_container)
                )
            bottomSheetView.let {
                user.apply {
                    visibility(it.tv_city, it.iv_city, city)
                    visibility(it.tv_experience_value, it.tv_experience, university)
                    visibility(it.tv_work, it.iv_work, job)
                    if (faculty == null) {
                        it.tv_fac.visibility = View.GONE
                    } else {
                        it.tv_fac.visibility = View.VISIBLE
                        it.tv_fac.text = faculty
                    }

                    if (!visibility(it.tv_mobile, it.iv_mobile, mobilePhone)) {
                        it.v_line_2.visibility = View.GONE
                    } else {
                        var mobileIntent = Intent(Intent.ACTION_VIEW,Uri.parse("tel:$mobilePhone"))
                        var mobileListener = View.OnClickListener {
                            startActivity(mobileIntent)
                        }
                        it.tv_mobile.setOnClickListener(mobileListener)
                        it.iv_mobile.setOnClickListener(mobileListener)
                    }

                    if (!visibility(it.tv_url, it.iv_url, socialUrl)) {
                        it.v_line_4.visibility = View.GONE
                    } else {
                        var browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(socialUrl)))
                        var urlListener = View.OnClickListener {
                            startActivity(browserIntent)
                        }
                        it.tv_url.setOnClickListener(urlListener)
                        it.iv_url.setOnClickListener(urlListener)
                    }

                    it.tv_tel.text = (getString(R.string.vk_url) + vk_id.toString())
                    var vkIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vkontakte://profile/$vk_id"))
                    var vkListener = View.OnClickListener {
                        startActivity(vkIntent)
                    }
                    it.tv_tel.setOnClickListener(vkListener)
                    it.iv_tel.setOnClickListener(vkListener)
                }
            }
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
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

    private fun getViewModel() = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

    override fun onDestroy() {
        ProfileInjector.apply {
            clearReposioryComponent()
            clearDomainComponent()
            clearProfileComponent()
        }
        (activity as AppCompatActivity).apply {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(false)
            }
        }
        super.onDestroy()
    }

    companion object {

        private const val ARG_ID = "id"
        private const val ARG_NAV_VIEW_POS = "position"

        fun newInstance(id: Long, position: Int?) : ProfileFragment =
            ProfileFragment().apply {
                Bundle().apply {
                    putLong(ARG_ID, id)
                    putInt(ARG_NAV_VIEW_POS, position ?: 4)
                    arguments = this
                }
            }
    }

}
