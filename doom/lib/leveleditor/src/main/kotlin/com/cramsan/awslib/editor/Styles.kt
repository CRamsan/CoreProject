package com.cramsan.awslib.editor

import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class Styles : Stylesheet() {
    companion object {
        val loginScreen by cssclass()
    }

    init {
        loginScreen {
            padding = box(15.px)
            vgap = 7.px
            hgap = 10.px
        }
    }
}
