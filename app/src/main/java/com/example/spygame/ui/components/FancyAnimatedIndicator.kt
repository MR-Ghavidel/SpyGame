package com.example.spygame.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.spygame.ui.theme.gold
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabIndicatorScope.FancyAnimatedIndicatorWithModifier(index: Int) {

    var startAnimatable by remember { mutableStateOf<Animatable<Dp, AnimationVector1D>?>(null) }
    var endAnimatable by remember { mutableStateOf<Animatable<Dp, AnimationVector1D>?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val layoutDirection = LocalLayoutDirection.current // تشخیص LTR یا RTL

    Box(
        Modifier
            .tabIndicatorLayout { measurable: Measurable, constraints: Constraints, tabPositions: List<TabPosition> ->
                val tabPosition = tabPositions[index]

                // اصلاح جهت چپ و راست در حالت RTL
                val (newStart, newEnd) = if (layoutDirection == LayoutDirection.Ltr) {
                    tabPosition.left to tabPosition.right
                } else {
                    tabPosition.right to tabPosition.left
                }

                val startAnim = startAnimatable ?: Animatable(newStart, Dp.VectorConverter).also { startAnimatable = it }
                val endAnim = endAnimatable ?: Animatable(newEnd, Dp.VectorConverter).also { endAnimatable = it }

                // محاسبه مدت زمان انیمیشن متناسب با فاصله
                val distance = (endAnim.targetValue - startAnim.targetValue).toPx().absoluteValue
                val animationDuration = when {
                    distance > 300f -> 500 // وقتی مسافت زیاد است، انیمیشن طولانی‌تر باشد
                    distance > 150f -> 300 // فاصله متوسط، انیمیشن متوسط
                    else -> 200 // فاصله کم، انیمیشن سریع
                }

                // بهبود انیمیشن در راست‌چین (RTL)
                if (endAnim.targetValue != newEnd) {
                    coroutineScope.launch {
                        endAnim.animateTo(
                            newEnd,
                            animationSpec = tween(
                                durationMillis = animationDuration.toInt(), // استفاده از tween برای تنظیم مدت زمان
                                easing = FastOutSlowInEasing // اعمال ease
                            )
                        )
                    }
                }

                if (startAnim.targetValue != newStart) {
                    coroutineScope.launch {
                        startAnim.animateTo(
                            newStart,
                            animationSpec = tween(
                                durationMillis = animationDuration.toInt(), // استفاده از tween برای تنظیم مدت زمان
                                easing = FastOutSlowInEasing // اعمال ease
                            )
                        )
                    }
                }

                val indicatorStart = startAnim.value.roundToPx()
                val indicatorEnd = endAnim.value.roundToPx()
                val indicatorWidth = (indicatorEnd - indicatorStart).absoluteValue.coerceAtLeast(1)

                val placeable =
                    measurable.measure(
                        constraints.copy(
                            maxWidth = indicatorWidth,
                            minWidth = indicatorWidth,
                        )
                    )

                layout(constraints.maxWidth, constraints.maxHeight) {
                    placeable.placeRelative(if (layoutDirection == LayoutDirection.Ltr) indicatorStart else indicatorEnd, 0)
                }
            }
            .padding(5.dp)
            .fillMaxSize()
            .drawWithContent {
                drawRoundRect(
                    color = gold, // رنگ تب فعال
                    cornerRadius = CornerRadius(8.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
    )
}
//کد اولیه که فقط چپ چینش درست کار میکنه
/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabIndicatorScope.FancyAnimatedIndicatorWithModifier(index: Int) {

    var startAnimatable by remember { mutableStateOf<Animatable<Dp, AnimationVector1D>?>(null) }
    var endAnimatable by remember { mutableStateOf<Animatable<Dp, AnimationVector1D>?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier
            .tabIndicatorLayout { measurable: Measurable,
                                  constraints: Constraints,
                                  tabPositions: List<TabPosition> ->
                val newStart = tabPositions[index].left
                val newEnd = tabPositions[index].right
                val startAnim =
                    startAnimatable
                        ?: Animatable(newStart, Dp.VectorConverter).also {
                            startAnimatable = it
                        }

                val endAnim =
                    endAnimatable
                        ?: Animatable(newEnd, Dp.VectorConverter).also { endAnimatable = it }

                if (endAnim.targetValue != newEnd) {
                    coroutineScope.launch {
                        endAnim.animateTo(
                            newEnd,
                            animationSpec =
                            if (endAnim.targetValue < newEnd) {
                                spring(dampingRatio = 1f, stiffness = 1000f)
                            } else {
                                spring(dampingRatio = 1f, stiffness = 50f)
                            }
                        )
                    }
                }

                if (startAnim.targetValue != newStart) {
                    coroutineScope.launch {
                        startAnim.animateTo(
                            newStart,
                            animationSpec =
                            // Handle directionality here, if we are moving to the right, we
                            // want the right side of the indicator to move faster, if we are
                            // moving to the left, we want the left side to move faster.
                            if (startAnim.targetValue < newStart) {
                                spring(dampingRatio = 1f, stiffness = 50f)
                            } else {
                                spring(dampingRatio = 1f, stiffness = 1000f)
                            }
                        )
                    }
                }

                val indicatorEnd = endAnim.value.roundToPx()
                val indicatorStart = startAnim.value.roundToPx()

                // Apply an offset from the start to correctly position the indicator around the tab
                val placeable =
                    measurable.measure(
                        constraints.copy(
                            maxWidth = indicatorEnd - indicatorStart,
                            minWidth = indicatorEnd - indicatorStart,
                        )
                    )
                layout(constraints.maxWidth, constraints.maxHeight) {
                    placeable.place(indicatorStart, 0)
                }
            }
            .padding(5.dp)
            .fillMaxSize()
            .drawWithContent {
                drawRoundRect(
                    color = gold,//indicatorColor,
                    cornerRadius = CornerRadius(8.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
    )
}*/

//انیمیشن چپ چین اکیه، راستچین هم بد نیست، خوبه
/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabIndicatorScope.FancyAnimatedIndicatorWithModifier(index: Int) {

    var startAnimatable by remember { mutableStateOf<Animatable<Dp, AnimationVector1D>?>(null) }
    var endAnimatable by remember { mutableStateOf<Animatable<Dp, AnimationVector1D>?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val layoutDirection = LocalLayoutDirection.current // تشخیص LTR یا RTL

    Box(
        Modifier
            .tabIndicatorLayout { measurable: Measurable, constraints: Constraints, tabPositions: List<TabPosition> ->
                val tabPosition = tabPositions[index]

                // اصلاح جهت چپ و راست در حالت RTL
                val (newStart, newEnd) = if (layoutDirection == LayoutDirection.Ltr) {
                    tabPosition.left to tabPosition.right
                } else {
                    tabPosition.right to tabPosition.left
                }

                val startAnim = startAnimatable ?: Animatable(newStart, Dp.VectorConverter).also { startAnimatable = it }
                val endAnim = endAnimatable ?: Animatable(newEnd, Dp.VectorConverter).also { endAnimatable = it }

                // بهبود انیمیشن در راست‌چین (RTL)
                if (endAnim.targetValue != newEnd) {
                    coroutineScope.launch {
                        endAnim.animateTo(
                            newEnd,
                            animationSpec = if (endAnim.targetValue < newEnd) {
                                spring(dampingRatio = 1f, stiffness = 1000f)
                            } else {
                                spring(dampingRatio = 1f, stiffness = 100f) // برای راست‌چین حساسیت بیشتر
                            }
                        )
                    }
                }

                if (startAnim.targetValue != newStart) {
                    coroutineScope.launch {
                        startAnim.animateTo(
                            newStart,
                            animationSpec = if (startAnim.targetValue < newStart) {
                                spring(dampingRatio = 1f, stiffness = 100f) // برای راست‌چین سرعت بیشتر
                            } else {
                                spring(dampingRatio = 1f, stiffness = 1000f)
                            }
                        )
                    }
                }

                val indicatorStart = startAnim.value.roundToPx()
                val indicatorEnd = endAnim.value.roundToPx()
                val indicatorWidth = (indicatorEnd - indicatorStart).absoluteValue.coerceAtLeast(1)

                val placeable =
                    measurable.measure(
                        constraints.copy(
                            maxWidth = indicatorWidth,
                            minWidth = indicatorWidth,
                        )
                    )

                layout(constraints.maxWidth, constraints.maxHeight) {
                    placeable.placeRelative(if (layoutDirection == LayoutDirection.Ltr) indicatorStart else indicatorEnd, 0)
                }
            }
            .padding(5.dp)
            .fillMaxSize()
            .drawWithContent {
                drawRoundRect(
                    color = gold, // رنگ تب فعال
                    cornerRadius = CornerRadius(8.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
    )
}
*/

