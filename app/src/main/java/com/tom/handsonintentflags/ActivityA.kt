package com.tom.handsonintentflags

import android.graphics.Color

class ActivityA : BaseChildActivity() {
    override val activityLabel = "Activity A"
    override val bannerColor = Color.parseColor("#1976D2")   // Blue
    override val selfClass: Class<*> = ActivityA::class.java
}
