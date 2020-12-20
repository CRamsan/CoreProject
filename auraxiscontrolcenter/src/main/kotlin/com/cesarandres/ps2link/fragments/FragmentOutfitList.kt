package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentOutfitBinding
import com.cramsan.framework.core.NoopViewModel

/**
 * This fragment will read and display all the outfits that have been set as
 * non-temporary in the database.
 */
class FragmentOutfitList : BasePS2Fragment<NoopViewModel, FragmentOutfitBinding>() {

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val listRoot = requireActivity().findViewById<View>(R.id.listViewOutfitList) as ListView
        listRoot.onItemClickListener = OnItemClickListener { myAdapter, myView, myItemInt, mylng ->
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
        const val TAG = "FragmentOutfitList"
    }

    override val logTag = "FragmentOutfitList"
    override val contentViewLayout: Int
        get() = TODO("Not yet implemented")
}
