package ua.kuk.common.logger

import android.util.Log.ERROR
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
/**
 * Custom Timber tree for logging crash reports to Firebase Crashlytics.
 *
 * Logs all messages to Crashlytics. Records exceptions as crash reports if the log priority is ERROR.
 * If no Throwable is provided for an ERROR priority log, a new Throwable is created with the message.
 */

class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log(message)

        if (priority == ERROR) {
            if (t == null) crashlytics.recordException(Throwable(message))
            else crashlytics.recordException(t)
        }
    }
}