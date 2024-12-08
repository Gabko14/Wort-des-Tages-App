//package com.example.wort_des_tages_app.shared
//
//import androidx.compose.foundation.text.ClickableText
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.AnnotatedString
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.sp
//
//@Preview
//@Composable
//fun LinkAnnotationTest() {
//    val annotatedLinkString: AnnotatedString = remember {
//        buildAnnotatedString {
//
//            val style = SpanStyle(
//                color = Color.Black,
//                fontSize = 18.sp,
//            )
//
//            val styleCenter = SpanStyle(
//                color = Color(0xff64B5F6),
//                fontSize = 20.sp,
//                textDecoration = TextDecoration.Underline
//            )
//
//            withStyle(
//                style = style
//            ) {
//                append("Click this ")
//            }
//
//            withLink(LinkAnnotation.Url(url = "https://github.com")) {
//                withStyle(
//                    style = styleCenter
//                ) {
//                    append("link")
//                }
//            }
//
//            withStyle(
//                style = style
//            ) {
//                append(" to go to website")
//            }
//        }
//    }
//
//    Column(
//        modifier = Modifier.padding(16.dp)
//    ) {
//        Text(annotatedLinkString)
//    }
//}