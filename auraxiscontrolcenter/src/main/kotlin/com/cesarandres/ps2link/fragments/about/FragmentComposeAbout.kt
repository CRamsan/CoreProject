package com.cesarandres.ps2link.fragments.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.framework.core.NoopViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment is very static, it has all the buttons for most of the main
 * fragments. It will also display the Preferred Character and Preferred Outfit
 * buttons if those have been set.
 */
@AndroidEntryPoint
class FragmentComposeAbout : BaseComposePS2Fragment<NoopViewModel>(), AboutEventHandler {

    override val logTag = "FragmentComposeMainMenu"
    override val viewModel: NoopViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        AboutCompose(
            eventHandler = this,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        requireActivity().title = getString(R.string.app_name_capital)
        return view
    }

    override fun onAboutClick() {
        TODO("Not yet implemented")
    }
}
