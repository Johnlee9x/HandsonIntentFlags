package com.tom.handsonintentflags

import android.graphics.Color

class ActivityB : BaseChildActivity() {
    override val activityLabel = "Activity B"
    override val bannerColor = Color.parseColor("#388E3C")   // Green
    override val selfClass: Class<*> = ActivityB::class.java
}
