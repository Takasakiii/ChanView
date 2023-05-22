package dev.takasaki.chanview.ui.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import dev.takasaki.chanview.R
import dev.takasaki.chanview.core.dtos.Post
import dev.takasaki.chanview.core.services.ChanService
import dev.takasaki.chanview.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun PostCard(post: Post, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isCropped by remember {
        mutableStateOf(true)
    }
    val scope = rememberCoroutineScope()

    Card(modifier) {
        Column {
            val imageModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1F, false)
                .clickable {
                    isCropped = !isCropped
                }

            if (post.image != null) {
                AsyncImage(
                    model = post.image,
                    contentDescription = "Imagem do post",
                    imageModifier,
                    alignment = Alignment.Center,
                    contentScale = if (isCropped)
                        ContentScale.Crop
                    else {
                        ContentScale.Fit
                    },
                    fallback = painterResource(id = R.drawable.ic_launcher_background)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Placeholder...",
                    imageModifier, alignment = Alignment.Center,
                    contentScale = if (isCropped)
                        ContentScale.Crop
                    else {
                        ContentScale.Fit
                    }
                )
            }
            Column(
                Modifier
                    .padding(8.dp)
            ) {
                if (post.comment != null) {
                    Surface(shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = post.comment,
                            Modifier
                                .height(200.dp)
                                .verticalScroll(
                                    rememberScrollState()
                                )
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        CardSmallButton("Share", onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, post.postUrl)
                                type = "text/plain"
                            }

                            val shareIntent = Intent.createChooser(sendIntent, null)
                            ContextCompat.startActivity(context, shareIntent, null)
                        })
                        CardSmallButton(text = "Share Raw", onClick = {
                            scope.launch {
                                val imageUri = ChanService.getImageAndroidUri(post, context)
                                val shareIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_STREAM, imageUri)
                                    type = "image/*"
                                }

                                ContextCompat.startActivity(context, shareIntent, null)
                            }
                        })
                        CardSmallButton(text = "Save", onClick = {
                            scope.launch {
                                val file = ChanService.saveRawImage(post)
                                Toast.makeText(context, "Imagem salva ${file.name}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    Text(
                        text = post.createdAt,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostCardPreview() {
    val post = Post("12/31/18(Mon)17:05:48", LoremIpsum(500).values.first(), null, "")
    AppTheme(true) {
        PostCard(post)
    }
}