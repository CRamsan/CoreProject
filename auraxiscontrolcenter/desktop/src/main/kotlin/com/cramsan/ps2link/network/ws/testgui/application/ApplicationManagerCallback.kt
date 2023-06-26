package com.cramsan.ps2link.network.ws.testgui.application

import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.messages.ServerEventPayload
import com.cramsan.ps2link.network.ws.testgui.filelogger.FileLog

interface ApplicationManagerCallback {

    fun onServerEventPayload(payload: ServerEventPayload)

    fun onProgramModeChanged(programMode: ProgramMode)

    fun onCharacterSelected(characterId: String, namespace: Namespace)

    fun onOutfitSelected(outfitId: String, namespace: Namespace)

    fun onTrackedCharacterSelected(characterId: String, namespace: Namespace)
    fun onFileLogActive(fileLog: FileLog)
}
