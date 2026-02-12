package com.tom.handsonintentflags

import android.graphics.Color

class ActivityC : BaseChildActivity() {
    override val activityLabel = "Activity C"
    override val bannerColor = Color.parseColor("#E64A19")   // Deep Orange
    override val selfClass: Class<*> = ActivityC::class.java
}
