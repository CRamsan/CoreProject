import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace

fun main() {
    val character = Character(
        "1234567890",
        namespace = Namespace.PS2PC,
        cached = false
    )
    println(greeting(character.toString()))
}

fun greeting(name: String) =
    "Hello, $name"