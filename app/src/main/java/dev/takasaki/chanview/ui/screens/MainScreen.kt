package dev.takasaki.chanview.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import dev.takasaki.chanview.core.dtos.Post
import dev.takasaki.chanview.ui.components.PostCard

@Composable
fun MainScreen(modifier: Modifier = Modifier, posts: List<Post> = listOf()) {
    Column(modifier) {

        LazyColumn(content = {
            itemsIndexed(posts) { index, post ->
                Box(
                    Modifier.padding(
                        start = 16.dp, end = 16.dp, top = if (index == 0) {
                            16.dp
                        } else {
                            0.dp
                        }
                    )
                ) {
                    PostCard(post = post)
                }
            }
        }, verticalArrangement = Arrangement.spacedBy(16.dp), state = LazyListState())
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    val sample = listOf(
        Post("12/31/18(Mon)17:05:48", LoremIpsum(500).values.first(), null, ""),
        Post("12/31/18(Mon)17:05:48", LoremIpsum(500).values.first(), null, ""),
        Post("12/31/18(Mon)17:05:48", LoremIpsum(500).values.first(), null, "")
    )

    MainScreen(posts = sample)
}