package dev.takasaki.chanview.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.takasaki.chanview.core.dtos.Board

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    boards: List<Board>,
    selected: Board? = null,
    onSelect: ((Board) -> Unit)? = null
) {
    ModalDrawerSheet {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            for (board in boards) {
                NavigationDrawerItem(
                    label = { Text(text = "/${board.board} - ${board.title}", fontSize = 16.sp) },
                    selected = board == selected,
                    onClick = { onSelect?.invoke(board) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun NavigationScreenPreview() {
    val simulateBoards = listOf(Board("b", "Random"), Board("a", "Anime"))
    NavigationScreen(simulateBoards)
}