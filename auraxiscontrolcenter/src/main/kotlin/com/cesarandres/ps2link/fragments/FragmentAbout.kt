package com.cesarandres.ps2link.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentAboutBinding
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.framework.metrics.logMetric

/**
 *
 */
class FragmentAbout : BasePS2Fragment<NoopViewModel, FragmentAboutBinding>() {

    override val viewModel: NoopViewModel by viewModels()

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.buttonAboutHomepage.setOnClickListener {
            logMetric(TAG, "Go To Website")
            val url = requireActivity().resources.getString(R.string.url_homepage)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    companion object {
        const val TAG = "FragmentAbout"
    }

    override val logTag = "FragmentAbout"
    override val contentViewLayout = R.layout.fragment_about
}
