package com.cramsan.awslib.eventsystem.events

import com.cramsan.awslib.utils.constants.InitialValues

class NoopEvent : BaseEvent(InitialValues.INVALID_ID, EventType.NOOP)
