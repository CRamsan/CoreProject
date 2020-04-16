package com.cesarandres.ps2link.fragments.holders

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.fragments.FragmentDirectiveList
import com.cesarandres.ps2link.fragments.FragmentFriendList
import com.cesarandres.ps2link.fragments.FragmentKillList
import com.cesarandres.ps2link.fragments.FragmentProfile
import com.cesarandres.ps2link.fragments.FragmentStatList
import com.cesarandres.ps2link.fragments.FragmentWeaponList
import com.cesarandres.ps2link.module.ButtonSelectSource
import java.util.HashMap

/**
 * This fragment holds a view pager for all the profile related fragments
 */
class FragmentProfilePager : BaseFragment() {
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
     * com.cesarandres.ps2link.base.BaseFragment#onCreate(android.os.Bundle)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            this.profileName = savedInstanceState.getString("ProfileName")
        }
    }

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
        val view = inflater.inflate(R.layout.fragment_profile_pager, container, false)
        return view
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        activityContainer.activityMode = ActivityMode.ACTIVITY_PROFILE
        this.fragmentUpdate.visibility = View.VISIBLE
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

        mSectionsPagerAdapter = SectionsPagerAdapter(activity!!.supportFragmentManager)
        mViewPager = activity!!.findViewById<View>(R.id.profilePager) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        var extras = activity!!.intent.extras
        if (extras == null) {
            extras = arguments
        }
        profileId = extras!!.getString("PARAM_0")
        this.namespace = extras.getString("PARAM_1")

        this.fragmentUpdate.visibility = View.VISIBLE
        this.fragmentUpdate.setOnClickListener(View.OnClickListener {
            metrics.log(TAG, "Update")
            val selectedFragment = mSectionsPagerAdapter!!.getFragment(mViewPager!!.currentItem)
                ?: return@OnClickListener
            when (mViewPager!!.currentItem) {
                PROFILE -> (selectedFragment as FragmentProfile).downloadProfiles(profileId)
                FRIENDS -> (selectedFragment as FragmentFriendList).downloadFriendsList(profileId)
                STATS -> (selectedFragment as FragmentStatList).downloadStatList(profileId)
                KILLBOARD -> (selectedFragment as FragmentKillList).downloadKillList(profileId)
                WEAPONS -> (selectedFragment as FragmentWeaponList).downloadWeaponList(profileId)
                DIRECTIVES -> (selectedFragment as FragmentDirectiveList).downloadDirectivesList(
                    profileId
                )
                else -> {
                }
            }
        })

        if ("" != profileName && profileName != null) {
            this.fragmentTitle.text = profileName
        }

        mViewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(arg0: Int) {
                metrics.log(TAG, "OnFragmentSelected", mapOf("Activity" to "Profile", "Fragment" to arg0.toString()))
                when (arg0) {
                    PROFILE -> {
                        fragmentStar.visibility = View.VISIBLE
                        fragmentAppend.visibility = View.VISIBLE
                        fragmentMyWeapons.visibility = View.GONE
                    }
                    FRIENDS -> {
                        fragmentStar.visibility = View.GONE
                        fragmentAppend.visibility = View.GONE
                        fragmentMyWeapons.visibility = View.GONE
                    }
                    STATS -> {
                        fragmentStar.visibility = View.GONE
                        fragmentAppend.visibility = View.GONE
                        fragmentMyWeapons.visibility = View.GONE
                    }
                    KILLBOARD -> {
                        fragmentStar.visibility = View.GONE
                        fragmentAppend.visibility = View.GONE
                        fragmentMyWeapons.visibility = View.GONE
                    }
                    WEAPONS -> {
                        fragmentStar.visibility = View.GONE
                        fragmentAppend.visibility = View.GONE
                        fragmentMyWeapons.visibility = View.VISIBLE
                    }
                    DIRECTIVES -> {
                        fragmentStar.visibility = View.GONE
                        fragmentAppend.visibility = View.GONE
                        fragmentMyWeapons.visibility = View.GONE
                    }
                    else -> {
                        fragmentStar.visibility = View.GONE
                        fragmentAppend.visibility = View.GONE
                        fragmentMyWeapons.visibility = View.GONE
                    }
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
            }

            override fun onPageScrollStateChanged(arg0: Int) {
            }
        })

        this.fragmentAppend.visibility = View.VISIBLE
        this.fragmentStar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val titleLayout = activity!!.findViewById<View>(R.id.linearLayoutTitle) as LinearLayout
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        val profileName = fragmentTitle.text.toString()
        if ("" != profileName) {
            savedInstanceState.putString("ProfileName", profileName)
        }
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
                DIRECTIVES -> fragment = FragmentDirectiveList()
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
}
