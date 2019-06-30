package com.cramsan.framework.halt.implementation

import com.cramsan.framework.halt.HaltUtilInterface

class MockHaltUtil : HaltUtilInterface {
    override fun stopThread() {
    }

    override fun stopMainThread() {
    }

    override fun crashApp() {
    }
}