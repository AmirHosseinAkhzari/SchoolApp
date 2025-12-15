package com.example.keravat.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.keravat.R

val Typography = Typography(

    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.portadaregular)),
        fontWeight = FontWeight(1000),
        fontSize = 15.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ) ,
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.portadaregular)),
        fontWeight = FontWeight(1000),
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp ,
    ) ,
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.portadaregular)),
        fontWeight = FontWeight(1000),
        fontSize = 30.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp ,
    ) ,
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.portadaregular)),
        fontWeight = FontWeight(1000),
        fontSize = 40.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp ,
    )  ,
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.portadaregular)),
        fontWeight = FontWeight(1000),
        fontSize = 60.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp ,
    ) ,
    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.portadaregular)),
        fontWeight = FontWeight(1000),
        fontSize = 15.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )




)