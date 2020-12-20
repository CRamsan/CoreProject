package com.cesarandres.ps2link.fragments.holders

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentProfilePagerBinding
import com.cesarandres.ps2link.fragments.FragmentFriendList
import com.cesarandres.ps2link.fragments.FragmentKillList
import com.cesarandres.ps2link.fragments.FragmentProfile
import com.cesarandres.ps2link.fragments.FragmentStatList
import com.cesarandres.ps2link.fragments.FragmentWeaponList
import com.cesarandres.ps2link.module.ButtonSelectSource
import com.cramsan.framework.core.NoopViewModel
import java.util.HashMap

/**
 * This fragment holds a view pager for all the profile related fragments
 */
class FragmentProfilePager : BasePS2Fragment<NoopViewModel, FragmentProfilePagerBinding>() {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    private var profileId: String? = null
    private var namespace: String? = null
    private var profileName: String? = null

    private var selectionButton: ButtonSelectSource? = null

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mSectionsPagerAdapter = SectionsPagerAdapter(requireActivity().supportFragmentManager)
        mViewPager = requireActivity().findViewById<View>(R.id.profilePager) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        var extras = requireActivity().intent.extras
        if (extras == null) {
            extras = arguments
        }
        profileId = extras!!.getString("PARAM_0")
        this.namespace = extras.getString("PARAM_1")

        /*
        this.fragmentUpdate.setOnClickListener(
            View.OnClickListener {
                metrics.log(TAG, "Update")
                val selectedFragment = mSectionsPagerAdapter!!.getFragment(mViewPager!!.currentItem)
                    ?: return@OnClickListener
                when (mViewPager!!.currentItem) {
                    PROFILE -> (selectedFragment as FragmentProfile).downloadProfiles(profileId)
                    FRIENDS -> (selectedFragment as FragmentFriendList).downloadFriendsList(profileId)
                    STATS -> (selectedFragment as FragmentStatList).downloadStatList(profileId)
                    KILLBOARD -> (selectedFragment as FragmentKillList).downloadKillList(profileId)
                    WEAPONS -> (selectedFragment as FragmentWeaponList).downloadWeaponList(profileId)
                    else -> {
                    }
                }
            }
        )
         */
    }

    /**
     * This pager will hold all the fragments that are displayed
     */
    inner class SectionsPagerAdapter
    /**
     * @param fm Fragment manager that will hold all the fragments within
     * the pager
     */
    @SuppressLint("UseSparseArrays")
    constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private val mMap: HashMap<Int, Fragment>

        init {
            this.mMap = HashMap()
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
         */
        override fun getItem(position: Int): Fragment {
            var fragment: Fragment? = null
            when (position) {
                PROFILE -> fragment = FragmentProfile()
                FRIENDS -> fragment = FragmentFriendList()
                KILLBOARD -> fragment = FragmentKillList()
                STATS -> fragment = FragmentStatList()
                WEAPONS -> fragment = FragmentWeaponList()
                else -> {
                }
            }
            val args = Bundle()
            args.putString("PARAM_0", profileId)
            if (namespace != null) {
                args.putString("PARAM_1", namespace)
            }
            fragment!!.arguments = args
            mMap[position] = fragment
            return fragment
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * android.support.v4.app.FragmentStatePagerAdapter#destroyItem(android
         * .view.ViewGroup, int, java.lang.Object)
         */
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
            mMap.remove(position)
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.view.PagerAdapter#getCount()
         */
        override fun getCount(): Int {
            return 5
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
         */
        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                PROFILE -> return resources.getString(R.string.text_profile_pager_title_overview)
                FRIENDS -> return resources.getString(R.string.text_profile_pager_title_friends)
                KILLBOARD -> return resources.getString(R.string.text_profile_pager_title_killboard)
                STATS -> return resources.getString(R.string.text_profile_pager_title_stats)
                WEAPONS -> return resources.getString(R.string.text_profile_pager_title_weapons)
                DIRECTIVES -> return resources.getString(R.string.text_profile_pager_title_directives)
                else -> return resources.getString(R.string.text_profile_pager_title_overview)
            }
        }

        /**
         * @param key integer that identifies the fragment
         * @return the fragment that is associated with the key
         */
        fun getFragment(key: Int): Fragment? {
            return mMap[key]
        }
    }

    companion object {
        const val TAG = "FragmentProfilePager"
        private val PROFILE = 0
        private val FRIENDS = 1
        private val STATS = 2
        private val KILLBOARD = 3
        private val WEAPONS = 4
        private val DIRECTIVES = 5
    }

    override val logTag = "FragmentProfilePager"
    override val contentViewLayout = R.layout.fragment_profile_pager
}
