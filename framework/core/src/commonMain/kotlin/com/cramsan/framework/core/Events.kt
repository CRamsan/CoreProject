package com.cramsan.framework.core

/**
 * An Event represents a signal that flows from the a ViewModel up to the UI layer.
 * This class by itself does not provide anything. For different uses-cases, this
 * class can be inherited as to define the specific use-cases.
 */
abstract class BaseEvent

/**
 * Event to represent a non-action.
 */
object NoopEvent : BaseEvent()

/**
 * Simple event to be used for cases when a viewModel will only send a single type of signal. It is
 * up to each [BaseFragment]/[BaseActivity] to define how this event will be handled.
 */
class SimpleEvent : BaseEvent()

/**
 * Event that passes a single [value] argument.
 */
class StringEvent(val value: String) : BaseEvent()
