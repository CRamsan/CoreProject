package com.cramsan.framework.logging

/**
 * Severity used for logging purposes.
 */
enum class Severity {
    /**
     * Log everything. This is only to be used in extreme cases. It can easily produce too much
     * output.
     */
    VERBOSE,

    /**
     * Log some extra information to help with debugging problems.
     */
    DEBUG,

    /**
     * Regular log level.
     */
    INFO,

    /**
     * Only log potentially problematic situations.
     */
    WARNING,

    /**
     * Log problems that directly impact functionality.
     */
    ERROR,

    /**
     * Do not log anything.
     */
    DISABLED,
}
