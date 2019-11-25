package com.cramsan.awslib.enums

/**
 * Type to represent each possible EntityType for a GameEntity.
@Deprecated("This Enum will be removed in the future",
ReplaceWith("Instead of relying on this enum, we should use the entity's own properties"),
DeprecationLevel.WARNING)
 */
enum class EntityType {
    SCIENTIST,
    PLAYER,
    DOG
}