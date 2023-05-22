package dev.takasaki.chanview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.takasaki.chanview.core.dtos.Board
import dev.takasaki.chanview.core.dtos.Post
import dev.takasaki.chanview.core.services.ChanService
import dev.takasaki.chanview.ui.screens.MainScreen
import dev.takasaki.chanview.ui.screens.NavigationScreen
import dev.takasaki.chanview.ui.theme.AppTheme
import dev.takasaki.chanview.ui.theme.md_theme_light_primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun App() {
    val showBoardsNavigation = rememberDrawerState(initialValue = DrawerValue.Closed)
    var snackBarContent by remember {
        mutableStateOf<String?>(null)
    }
    val scope = rememberCoroutineScope()
    var boards by remember {
        mutableStateOf(listOf<Board>())
    }
    var selectedBoard by remember {
        mutableStateOf<Board?>(null)
    }
    var posts by remember {
        mutableStateOf(listOf<Post>())
    }

    LaunchedEffect(Unit) {
        boards = ChanService.getBoards()
        selectedBoard = boards.firstOrNull()
        if (selectedBoard != null) {
            posts = ChanService.getCatalog(selectedBoard!!)
        }
    }

    AppTheme(true) {
        ModalNavigationDrawer(
            drawerContent = {
                NavigationScreen(
                    boards,
                    selectedBoard,
                    onSelect = { board ->
                        selectedBoard = board
                        scope.launch {
                            if (selectedBoard != null) {
                                posts = ChanService.getCatalog(selectedBoard!!)
                            }
                        }
                    })
            },
            drawerState = showBoardsNavigation
        ) {
            Scaffold(
                snackbarHost = { if(snackBarContent != null) {
                    Snackbar {
                        Text(text = snackBarContent!!)
                    }
                } },
                topBar = {
                    TopAppBar(
                        title = { Text(text = selectedBoard?.title ?: "Carregando...") },
                        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = md_theme_light_primary),
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    showBoardsNavigation.open()
                                }
                            }) {
                                Icon(Icons.Filled.Menu, contentDescription = null)
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                scope.launch {
                                    if (selectedBoard != null) {
                                        posts = ChanService.getCatalog(selectedBoard!!)
                                        snackBarContent = "Posts Atualizado"
                                        delay(2000)
                                        snackBarContent = null
                                    }
                                }
                            }) {
                                Icon(Icons.Filled.Refresh, contentDescription = null)
                            }
                        }
                    )
                }
            ) { padding ->
                if (selectedBoard != null) {
                    MainScreen(Modifier.padding(padding), posts)
                }
            }
        }
    }

}


@Composable
@Preview(showSystemUi = true)
private fun AppPreview() {
    App()
}