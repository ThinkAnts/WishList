package com.example.wishlistapp

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wishlistapp.Data.DummyWish
import com.example.wishlistapp.Data.Wish

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: WishViewModel
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = { AppBar(title = "WishList")},
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.padding(all = 20.dp),
                                contentColor = Color.White,
                                containerColor = Color.DarkGray,
                onClick = {
                    Toast.makeText(context, "FAButton Clicked", Toast.LENGTH_LONG).show()
                    navController.navigate(Screen.AddScreen.route + "/0L")
                }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }) {
        val wishList = viewModel.getAllWishes.collectAsState(initial = listOf())

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
              items(wishList.value, key = {wish->wish.id}){
                  wish ->
                    val dismissState = rememberDismissState(
                        confirmValueChange = {
                            if(it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart){
                                viewModel.deleteWish(wish)
                            }
                            true
                        }
                    )
                    SwipeToDismiss(state = dismissState,
                                   background = {
                                         val color by animateColorAsState(
                                            if (dismissState.dismissDirection == DismissDirection.EndToStart) Color.Red else Color.Transparent,
                                             label = ""
                                         )
                                       val alignment = Alignment.Center
                                       Box(
                                           Modifier.fillMaxSize().background(color).padding(horizontal = 20.dp)
                                       ) {
                                           Icon(Icons.Default.Delete, contentDescription = "Delete Icon", tint = Color.White)
                                       }
                                   },
                                   directions = setOf(DismissDirection.EndToStart),
                                   dismissContent = {
                                       WishItem(wish = wish) {
                                       val id = wish.id
                                       navController.navigate(Screen.AddScreen.route + "/$id")
                                       }
                                   }
                        )
              }
        }
    }
}

@Composable
fun WishItem(wish: Wish, onClick: () ->Unit) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .clickable {
                onClick()
            }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = wish.title, fontWeight = FontWeight.ExtraBold)
                Text(text = wish.description, fontWeight = FontWeight.Light)
            }
        }
}