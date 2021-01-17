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
import com.cesarandres.ps2link.databinding.FragmentOutfitPagerBinding
import com.cesarandres.ps2link.fragments.FragmentMembersList
import com.cesarandres.ps2link.fragments.FragmentMembersOnline
import com.cesarandres.ps2link.fragments.FragmentOutfit
import com.cesarandres.ps2link.module.ButtonSelectSource
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.framework.metrics.logMetric
import java.util.HashMap

/**
 * This fragment has a view pager that displays the online member next to all
 * the member.
 */
class FragmentOutfitPager : BasePS2Fragment<NoopViewModel, FragmentOutfitPagerBinding>() {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    private var outfitId: String? = null
    private var namespace: String? = null

    private var selectionButton: ButtonSelectSource? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mSectionsPagerAdapter = SectionsPagerAdapter(requireActivity().supportFragmentManager)

        mViewPager = requireActivity().findViewById<View>(R.id.outfitPager) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        var extras = requireActivity().intent.extras
        if (extras == null) {
            extras = arguments
        }

        outfitId = extras!!.getString("PARAM_0")
        this.namespace = extras.getString("PARAM_1")

        /*
        this.fragmentUpdate.setOnClickListener {
            logMetric(TAG, "Update")
            val fragment: Fragment
            try {
                when (mViewPager!!.currentItem) {
                    OUTFIT -> {
                        fragment =
                            mSectionsPagerAdapter!!.getFragment(mViewPager!!.currentItem) as FragmentOutfit
                        fragment.downloadOutfit(outfitId)
                    }
                    ONLINE -> {
                        fragment =
                            mSectionsPagerAdapter!!.getFragment(mViewPager!!.currentItem) as FragmentMembersOnline
                        fragment.downloadOutfitMembers()
                    }
                    MEMBERS -> {
                        fragment =
                            mSectionsPagerAdapter!!.getFragment(mViewPager!!.currentItem) as FragmentMembersList
                        fragment.downloadOutfitMembers()
                    }
                    else -> {
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(activity, R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT)
                    .show()
            }
        }
         */

        mViewPager!!.setOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageSelected(arg0: Int) {
                    logMetric(TAG, "OnFragmentSelected", mapOf("Activity" to "Outfit", "Fragment" to arg0.toString()))
                    /*
                    when (arg0) {
                        OUTFIT -> {
                            fragmentShowOffline.visibility = View.GONE
                            fragmentAppend.visibility = View.VISIBLE
                            fragmentStar.visibility = View.VISIBLE
                        }
                        ONLINE -> {
                            fragmentShowOffline.visibility = View.GONE
                            fragmentAppend.visibility = View.GONE
                            fragmentStar.visibility = View.GONE
                        }
                        MEMBERS -> {
                            fragmentShowOffline.visibility = View.VISIBLE
                            fragmentAppend.visibility = View.GONE
                            fragmentStar.visibility = View.GONE
                        }
                        else -> {
                            fragmentShowOffline.visibility = View.GONE
                            fragmentAppend.visibility = View.GONE
                            fragmentStar.visibility = View.GONE
                        }
                    }

                     */
                }

                override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
                }

                override fun onPageScrollStateChanged(arg0: Int) {
                }
            }
        )
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
    }

    /**
     * This pager will hold all the fragments that are displayed
     */
    private inner class SectionsPagerAdapter
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
                OUTFIT -> fragment = FragmentOutfit()
                ONLINE -> fragment = FragmentMembersOnline()
                MEMBERS -> fragment = FragmentMembersList()
                else -> {
                }
            }
            val args = Bundle()
            args.putString("PARAM_0", outfitId)
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
            return 3
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
         */
        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                OUTFIT -> return resources.getString(R.string.title_outfit)
                ONLINE -> return resources.getString(R.string.text_online_caps)
                MEMBERS -> return resources.getString(R.string.text_members)
                else -> return resources.getString(R.string.text_online_caps)
            }
        }

        /**
         * @param key integer that identifies the fragment
         * @return the fragment that is associated with the key
         */
        fun getFragment(key: Int): Fragment {
            return mMap[key]!!
        }
    }

    companion object {
        const val TAG = "FragmentOutfitPager"
        private val OUTFIT = 0
        private val ONLINE = 1
        private val MEMBERS = 2
    }

    override val logTag = "FragmentOutfitPager"
    override val contentViewLayout = R.layout.fragment_outfit_pager
}
