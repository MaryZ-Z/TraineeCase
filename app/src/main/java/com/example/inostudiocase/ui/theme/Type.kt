package com.example.inostudiocase.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    // описание фильма
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.DarkGray
    ),

    //название фильма
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = GreenText
    ),

    //для экрана ошибок
    body2 = TextStyle(
        color = Color.Gray,
        textAlign = TextAlign.Center
    ),

    //для даты и ролей актеров
    overline = TextStyle(
        color = Color.Gray
    ),

    //для описания фильмов и имен актеров
    subtitle1 = TextStyle(
        color = Color.DarkGray
    ),

    //для кнопок
    h5 = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Bold
    ),

    h6 = TextStyle(
        color = Color.LightGray
    )
)

/* Other default text styles to override
button = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp
),
caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
*/