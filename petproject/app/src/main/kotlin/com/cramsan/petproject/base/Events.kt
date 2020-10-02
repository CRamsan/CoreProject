package com.cramsan.petproject.base

abstract class BaseEvent

class SimpleEvent : BaseEvent()

class StringEvent(val value: String) : BaseEvent()
