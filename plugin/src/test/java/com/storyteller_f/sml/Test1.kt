package com.storyteller_f.sml

import com.storyteller_f.sml.config.OvalShapeDrawable
import com.storyteller_f.sml.config.RgbColor
import kotlin.test.Test
import kotlin.test.assertEquals

class Test1 {
    @Test
    fun checkRect() {
        val output = OvalShapeDrawable().apply {
            solid(RgbColor("#bbffaa"))
        }.output()
        assertEquals(
            """<?xml version="1.0" encoding="utf-8"?>
                        |<shape android:shape="oval"
                        |    dither="false"
                        |    visible="true"
                        |    xmlns:android="http://schemas.android.com/apk/res/android">
                        |
                        |    <solid android:color="#bbffaa"/>
                        |</shape>""", output
        )
    }
}