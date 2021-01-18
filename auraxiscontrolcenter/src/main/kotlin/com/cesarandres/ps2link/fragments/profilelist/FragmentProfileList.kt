package com.cesarandres.ps2link.fragments.profilelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentProfileListBinding
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.ps2link.appcore.dbg.content.CharacterProfile

/**
 * Fragment that reads the profiles from the database that have been set as not
 * temporary
 */
class FragmentProfileList : BasePS2Fragment<NoopViewModel, FragmentProfileListBinding>() {

    override val viewModel: NoopViewModel by viewModels()

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_profile_list, container, false)
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val listRoot = requireActivity().findViewById<View>(R.id.listViewProfileList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            val profile = (myAdapter.getItemAtPosition(myItemInt) as CharacterProfile)
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val TAG = "FragmentProfileList"
    }

    override val logTag: String
        get() = TODO("Not yet implemented")
    override val contentViewLayout: Int
        get() = TODO("Not yet implemented")
}
