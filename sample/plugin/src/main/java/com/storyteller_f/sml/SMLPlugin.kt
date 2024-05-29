package com.storyteller_f.sml

import com.storyteller_f.sml.generator.Sml
import com.storyteller_f.sml.generator.SmlExtension
import com.storyteller_f.sml.generator.colorMap
import com.storyteller_f.sml.generator.colors
import com.storyteller_f.sml.generator.config.Color
import com.storyteller_f.sml.generator.config.Dimension
import com.storyteller_f.sml.generator.config.Dp
import com.storyteller_f.sml.generator.config.Line
import com.storyteller_f.sml.generator.config.Oval
import com.storyteller_f.sml.generator.config.Rectangle
import com.storyteller_f.sml.generator.config.RgbColor
import com.storyteller_f.sml.generator.config.Ring
import com.storyteller_f.sml.generator.config.rgb
import com.storyteller_f.sml.generator.dimenMap
import com.storyteller_f.sml.generator.dimens
import com.storyteller_f.sml.generator.reference
import com.storyteller_f.sml.generator.tasks.DrawableDomain
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.com.google.common.base.CaseFormat

class SMLPlugin : Plugin<Project> {
    private val sml = Sml()
    override fun apply(target: Project) {
        sml.apply(
            target,
            SmlExtension(Theme1::class.colors(), DefaultDevice::class.dimens(), buildList {
                Theme1::class.colorMap().forEach { cp ->
                    DefaultDevice::class.dimenMap().forEach { dp ->
                        add(DrawableDomain().apply(fun DrawableDomain.() {
                            Rectangle {
                                solid(cp.reference())
                                corners(dp.reference())
                            }
                        }).apply {
                            name = "drawable_${cp.name}_${
                                CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE)
                                    .convert(dp.name)
                            }"
                        })
                    }
                }
                add(DrawableDomain().apply(fun DrawableDomain.() {
                    Oval {
                        solid(RgbColor("#00ff00"))
                    }
                }).apply {
                    name = "test"
                })
                add(DrawableDomain().apply(fun DrawableDomain.() {
                    Ring("10dp", "1dp") {
                        ring(RgbColor("#00ff00"), Dp(10f))
                    }
                }).apply {
                    name = "test1"
                })
                add(DrawableDomain().apply(fun DrawableDomain.() {
                    Line {
                        line(RgbColor("#00ff00"), Dp(10f))
                    }
                }).apply {
                    name = "test2"
                })
            })
        )
    }

}


interface DD {
    val rectRadius: Dimension

    val compatRectRadius: Dimension

    val bigRectRadius: Dimension
}

class DefaultDevice : DD {
    override val rectRadius: Dimension = Dp(2f)
    override val compatRectRadius: Dimension = Dp(4f)
    override val bigRectRadius: Dimension = Dp(8f)
}

class Theme1 {
    val color1: Color = "#496989".rgb()
    val color2: Color = "#58A399".rgb()
    val color3: Color = "#A8CD9F".rgb()
    val color4: Color = "#E2F4C5".rgb()
}
