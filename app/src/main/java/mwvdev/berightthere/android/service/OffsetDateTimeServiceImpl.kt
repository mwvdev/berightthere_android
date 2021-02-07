package mwvdev.berightthere.android.service

import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OffsetDateTimeServiceImpl @Inject constructor() : OffsetDateTimeService {

    override fun now(): OffsetDateTime {
        return OffsetDateTime.now()
    }

}