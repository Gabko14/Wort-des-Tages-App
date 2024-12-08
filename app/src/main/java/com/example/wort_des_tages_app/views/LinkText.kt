package com.example.wort_des_tages_app.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun LinkText(text: String?, url: String?, context: Context, modifier: Modifier = Modifier) {
    if (text == null || url == null) return

    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = text, annotation = url)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(text)
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 42.sp, lineHeight = 35.sp),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = text, start = offset, end = offset).
            firstOrNull()?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
    )
}