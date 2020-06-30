package com.junmeng.retrofitdemo.app

import android.app.Activity
import android.content.Intent
import android.util.Log
import java.lang.ref.WeakReference

/**
 * 用于保存当前的activity
 */
class CurrentActivityManager private constructor() {
    val TAG = this.javaClass.name
    private var sCurrentActivityWeakRef: WeakReference<Activity>? = null
    private val activityUpdateLock = Any()

    open var currentActivity: Activity?
        get() {
            var currentActivity: Activity? = null
            synchronized(activityUpdateLock) {
                if (sCurrentActivityWeakRef != null) {
                    currentActivity = sCurrentActivityWeakRef!!.get()
                }
            }
            return currentActivity
        }
        set(activity) {
            synchronized(
                activityUpdateLock
            ) {
                sCurrentActivityWeakRef = WeakReference<Activity>(activity)
                Log.i(TAG, "current activity is ${activity?.componentName}")
            }
        }

    companion object {
        val instance = CurrentActivityManager()
    }

    /**
     * 跳转到指定activity
     */
    open fun turnToActivity(clazz: Class<*>?, finishCurrent: Boolean = false) {
        currentActivity?.let {
            var intent = Intent(currentActivity, clazz)
            it.startActivity(intent)
            if (finishCurrent) {
                it.finish()
            }
        }

    }


}