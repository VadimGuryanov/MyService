package kpfu.itis.myservice.features.feature_profile.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kpfu.itis.myservice.R

class NotFoundFragment : Fragment() {

    companion object {
        fun newInstance() : NotFoundFragment = NotFoundFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_not_found, container, false)

}
