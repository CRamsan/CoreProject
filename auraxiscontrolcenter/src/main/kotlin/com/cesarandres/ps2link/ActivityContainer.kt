package com.cesarandres.ps2link

import com.cesarandres.ps2link.base.BasePS2Activity
import com.cramsan.framework.core.NoopViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Class that will hold the current fragments. It behaves differently if it is
 * run on a table or a phone. On a phone, every time a new fragment needs to be
 * created, a new instance of this activity will be created. If this activity is
 * running on a table, this tablet will keep a main menu on the left side while
 * new fragments will be swapped on the right side.
 *
 *
 * This activity will also use the @activityMode variable to keep track of the
 * current fragment on top of the stack. This works correctly in phone mode, it
 * has not been tested in tablets yet.
 */
@AndroidEntryPoint
class ActivityContainer : BasePS2Activity<NoopViewModel>() {

    override val contentViewLayout = R.layout.activity_single_pane
    override val toolbarViewId = R.id.main_menu_toolbar
    override val logTag = "ActivityContainer"
}
