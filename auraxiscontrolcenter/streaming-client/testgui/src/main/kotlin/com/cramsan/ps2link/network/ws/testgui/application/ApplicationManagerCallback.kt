package com.cramsan.ps2link.network.ws.testgui.application

import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.network.ws.messages.ServerEventPayload

interface ApplicationManagerCallback {

    fun onServerEventPayload(character: Character, payload: ServerEventPayload)

    fun onProgramModeChanged(programMode: ProgramMode)
}
