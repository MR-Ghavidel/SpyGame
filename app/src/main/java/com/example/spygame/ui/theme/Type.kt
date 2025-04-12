package com.example.spygame.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.spygame.R



val IranYekan = FontFamily(
    Font(R.font.iran_yekan_mobile_regular, FontWeight.Normal),
    Font(R.font.iran_yekan_mobile_medium, FontWeight.Medium),
    Font(R.font.iran_yekan_mobile_bold, FontWeight.Bold),
    Font(R.font.iran_yekan_mobile_extra_bold, FontWeight.ExtraBold)
)

// Typography برای فارسی
val PersianTypography = Typography(
    bodySmall = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleLarge = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp
    ),
    labelSmall = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = IranYekan,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp
    ),
)

// Typography برای انگلیسی (استفاده از فونت پیش‌فرض سیستم)
val EnglishTypography = Typography(
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp
    ),
)