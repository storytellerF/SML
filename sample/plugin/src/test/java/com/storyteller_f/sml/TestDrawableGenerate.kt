package com.storyteller_f.sml

import com.storyteller_f.sml.generator.config.ClipDrawable
import com.storyteller_f.sml.generator.config.Dp
import com.storyteller_f.sml.generator.config.DrawableReference
import com.storyteller_f.sml.generator.config.LineShapeDrawable
import com.storyteller_f.sml.generator.config.OvalShapeDrawable
import com.storyteller_f.sml.generator.config.RectangleShapeDrawable
import com.storyteller_f.sml.generator.config.RgbColor
import com.storyteller_f.sml.generator.config.RingShapeDrawable
import com.storyteller_f.sml.generator.config.RippleDrawable
import kotlin.test.Test
import kotlin.test.assertEquals

class TestDrawableGenerate {
    @Test
    fun testOvalDrawable() {
        val output = OvalShapeDrawable().apply {
            solid(RgbColor("#bbffaa"))
        }.output()
        assertEquals(
            """<?xml version="1.0" encoding="utf-8"?>
                        |<shape android:shape="oval"
                        |    android:dither="false"
                        |    android:visible="true"
                        |    xmlns:android="http://schemas.android.com/apk/res/android">
                        |
                        |    <solid android:color="#bbffaa"/>
                        |</shape>""".trimMargin(), output
        )
    }

    @Test
    fun testRectDrawable() {
        val output = RectangleShapeDrawable().apply {
            solid(RgbColor("#bbffaa"))
        }.output()
        assertEquals(
            """<?xml version="1.0" encoding="utf-8"?>
                        |<shape android:shape="rectangle"
                        |    android:dither="false"
                        |    android:visible="true"
                        |    xmlns:android="http://schemas.android.com/apk/res/android">
                        |
                        |    <solid android:color="#bbffaa"/>
                        |</shape>""".trimMargin(), output
        )
    }

    @Test
    fun testRingDrawable() {
        val output = RingShapeDrawable("10dp", "10px", false).apply {
            ring(RgbColor("#ffffff"), Dp(10f))
        }.output()
        assertEquals(
            """<?xml version="1.0" encoding="utf-8"?>
|<shape android:shape="ring"
|    android:dither="false"
|    android:visible="true"
|    android:innerRadius="10dp"
|    android:thickness="10px"
|    xmlns:android="http://schemas.android.com/apk/res/android">
|
|</shape>""".trimMargin(), output
        )
    }

    @Test
    fun testLineDrawable() {
        val output = LineShapeDrawable().apply {
            line(RgbColor("#ffffff"), Dp(10f))
        }.output()
        assertEquals(
            """<?xml version="1.0" encoding="utf-8"?>
|<shape android:shape="line"
|    android:dither="false"
|    android:visible="true"
|    xmlns:android="http://schemas.android.com/apk/res/android">
|
|    <stroke
|        android:width="10dp"
|        android:color="#ffffff"/>
|</shape>""".trimMargin(), output
        )
    }

    @Test
    fun testClipDrawable() {
        val output = ClipDrawable(DrawableReference("test"), "center", "vertical").apply {
        }.output()
        assertEquals(
            """<?xml version="1.0" encoding="utf-8"?>

|<clip 
|    android:drawable="@drawable/test" 
|    android:gravity="center" 
|    android:clipOrientation="vertical"
|    xmlns:android="http://schemas.android.com/apk/res/android" />
|</clip>""".trimMargin(), output
        )
    }

    @Test
    fun testRippleDrawable() {
        val output = RippleDrawable(RgbColor("#FFFFFF"), Dp(10f)).apply {
        }.output()
        assertEquals(
            """<?xml version="1.0" encoding="utf-8"?>

|<ripple
|    android:color="#ff0000"
|    android:radius="10dp"
|    android:effectColor="#FFFFFF"
|    xmlns:android="http://schemas.android.com/apk/res/android">
|</ripper>""".trimMargin(), output
        )
    }
}