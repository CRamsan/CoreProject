package ui

import com.cramsan.ps2link.core.models.Character
import com.jessecorbett.diskord.api.channel.Embed
import com.jessecorbett.diskord.api.channel.EmbedField

fun Embed.fullCharacter(
    character: Character
): Embed {
    return this.apply {
        title = character.name
        fields = mutableListOf(
            EmbedField("Faction", character.faction.name, inline = false),
            EmbedField("BR", character.battleRank.toString(), inline = false),
            EmbedField("Certs", character.certs.toString(), inline = false),
            EmbedField("Status", character.loginStatus.toString(), inline = false),
            EmbedField("Last login", character.lastLogin.toString(), inline = false),
            EmbedField("Server", character.server?.serverName.toString(), inline = false),
            EmbedField("PS2 Site", "https://www.planetside2.com/players/#!/${character.characterId}", inline = false),
        )
    }
}
