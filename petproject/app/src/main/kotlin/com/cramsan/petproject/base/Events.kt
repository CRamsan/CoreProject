package com.cramsan.petproject.base

abstract class BaseEvent

@Deprecated("Use the framework version of this class")
class SimpleEvent : BaseEvent()

@Deprecated("Use the framework version of this class")
class StringEvent(val value: String) : BaseEvent()
