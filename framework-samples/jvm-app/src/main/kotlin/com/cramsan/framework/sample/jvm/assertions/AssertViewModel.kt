package com.cramsan.framework.sample.jvm.assertions

import com.cramsan.framework.assertlib.assert
import com.cramsan.framework.assertlib.assertFailure
import com.cramsan.framework.assertlib.assertFalse
import com.cramsan.framework.assertlib.assertNotNull
import com.cramsan.framework.assertlib.assertNull

class AssertViewModel : AssertScreenEventHandler {

    override fun tryAssert() {
        assert(false, TAG, "This is an assertion failure")
    }

    override fun tryAssertFalse() {
        assertFalse(true, TAG, "Asserting due to value being true")
    }

    override fun tryAssertNull() {
        assertNull("", TAG, "Asserting due to value not null")
    }

    override fun tryAssertNotNull() {
        assertNotNull(null, TAG, "Asserting due to value being null")
    }

    override fun tryAssertFailure() {
        assertFailure(TAG, "Asserting due to failure")
    }

    companion object {
        private const val TAG = "AssertViewModel"
    }
}
