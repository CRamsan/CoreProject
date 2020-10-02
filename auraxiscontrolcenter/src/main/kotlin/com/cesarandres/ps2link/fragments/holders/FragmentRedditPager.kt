package com.cesarandres.ps2link.fragments.holders

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.fragments.FragmentReddit
import java.util.HashMap

/**
 * This fragment holds a view pager for all the profile related fragments
 */
class FragmentRedditPager : BaseFragment() {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    private var goToReddit: Button? = null

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onCreate(android.os.Bundle)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
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
        return inflater.inflate(R.layout.fragment_reddit_pager, container, false)
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        activityContainer.activityMode = ActivityMode.ACTIVITY_REDDIT

        this.fragmentUpdate.visibility = View.VISIBLE
        val params = goToReddit!!.layoutParams as LayoutParams
        if (params != null) {
            params.gravity = Gravity.CENTER_VERTICAL
            goToReddit!!.layoutParams = params
        }
    }

    override fun onStart() {
        super.onStart()

        val titleLayout = activity!!.findViewById<View>(R.id.linearLayoutTitle) as LinearLayout
        if (titleLayout.findViewById<View>(R.id.buttonFragmentGoToReddit) != null) {
            goToReddit = titleLayout.findViewById<View>(R.id.buttonFragmentGoToReddit) as Button
        } else {
            goToReddit = View.inflate(activity, R.layout.layout_go_to_reddit, null) as Button
        }

        goToReddit!!.setOnClickListener {
            val intentUri: String
            when (mViewPager!!.currentItem) {
                PC -> intentUri = FragmentReddit.REDDIT_URL + PS2_PC
                PS4 -> intentUri = FragmentReddit.REDDIT_URL + PS2_PS4
                else -> intentUri = FragmentReddit.REDDIT_URL + PS2_PC
            }
            metrics.log(TAG, "Open Reddit URL", mapOf("Site" to intentUri))
            val openRedditIntent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri))
            startActivity(openRedditIntent)
        }

        if (titleLayout.findViewById<View>(R.id.buttonFragmentGoToReddit) == null) {
            titleLayout.addView(goToReddit)
        }
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
        mViewPager = activity!!.findViewById<View>(R.id.redditPager) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        var extras = activity!!.intent.extras
        if (extras == null) {
            extras = arguments
        }

        this.fragmentUpdate.visibility = View.VISIBLE
        this.fragmentUpdate.setOnClickListener(
            View.OnClickListener {
                metrics.log(TAG, "Update")
                val selectedFragment = mSectionsPagerAdapter!!.getFragment(mViewPager!!.currentItem)
                    ?: return@OnClickListener
                when (mViewPager!!.currentItem) {
                    PC, PS4 -> (selectedFragment as FragmentReddit).updatePosts()
                    else -> {
                    }
                }
            }
        )

        mViewPager!!.setOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageSelected(arg0: Int) {
                    metrics.log(TAG, "OnFragmentSelected", mapOf("Activity" to "Reddit", "Fragment" to arg0.toString()))
                }

                override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
                }

                override fun onPageScrollStateChanged(arg0: Int) {
                }
            }
        )
    }

    override fun onStop() {
        super.onStop()
        val titleLayout = activity!!.findViewById<View>(R.id.linearLayoutTitle) as LinearLayout
        if (titleLayout.findViewById<View>(R.id.buttonFragmentGoToReddit) != null) {
            titleLayout.removeView(goToReddit)
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
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
            fragment = FragmentReddit()
            val args = Bundle()
            when (position) {
                PC -> args.putString("PARAM_0", PS2_PC)
                PS4 -> args.putString("PARAM_0", PS2_PS4)
                else -> {
                }
            }
            fragment.arguments = args
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
            return 2
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
         */
        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                PC -> return "r/$PS2_PC"
                PS4 -> return "r/$PS2_PS4"
                else -> return PS2_PC
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
        const val TAG = "FragmentRedditPager"
        val PS2_PC = "Planetside"
        val PS2_PS4 = "PS4Planetside2"
        private val PC = 0
        private val PS4 = 1
    }
}
