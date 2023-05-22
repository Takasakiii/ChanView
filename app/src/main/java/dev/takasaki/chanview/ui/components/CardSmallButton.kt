package dev.takasaki.chanview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun CardSmallButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier) {
        Text(text, fontSize = 10.sp,
            fontWeight = FontWeight.Light, modifier = Modifier.clickable { onClick() })
    }
}

@Preview
@Composable
fun CardSmallButtonPreview() {
    CardSmallButton("Share Test", onClick = {}, Modifier.background(Color.Red))
}