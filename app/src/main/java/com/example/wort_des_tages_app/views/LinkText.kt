package com.example.wort_des_tages_app.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun LinkText(
    text: String?, 
    url: String?, 
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    fontSize: Int = 36
) {
    // Early return if text or url is null
    if (text.isNullOrEmpty() || url.isNullOrEmpty()) return

    // Get theme color outside the remember block
    val primaryColor = MaterialTheme.colorScheme.primary
    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        fontSize = fontSize.sp, 
        lineHeight = (fontSize - 4).sp
    )
    
    // Creating the annotated string - memoize to prevent recreating during recompositions
    val annotatedString = remember(text, url, primaryColor) {
        buildAnnotatedString {
            pushStringAnnotation(tag = "URL", annotation = url)
            withStyle(
                style = SpanStyle(
                    color = primaryColor,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(text)
            }
            pop()
        }
    }

    ClickableText(
        text = annotatedString,
        modifier = modifier,
        style = textStyle,
        onClick = { offset ->
            annotatedString.getStringAnnotations(
                tag = "URL", 
                start = offset, 
                end = offset
            ).firstOrNull()?.let { annotation ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
    )
}