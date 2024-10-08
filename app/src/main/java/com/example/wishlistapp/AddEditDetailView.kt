package com.example.wishlistapp

import android.widget.NumberPicker.OnValueChangeListener
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wishlistapp.Data.Wish
import kotlinx.coroutines.launch

@Composable
fun AddEditDetailView(id: Long, viewModel: WishViewModel, navController: NavController) {

    val snackMessage = remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    if (id != 0L) {
        val wish = viewModel.getWishById(id).collectAsState(initial = Wish(0L, "", ""))
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
    } else {
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = { AppBar(title =
            if(id!= 0L) stringResource(id = R.string.update_wish) else stringResource(id = R.string.add_wish)) { navController.navigateUp()} },
    ){ it ->
        Column(modifier = Modifier
            .padding(it)
            .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.heightIn(10.dp))
            WishTextField(label = "Title", value = viewModel.wishTitleState, onValueChanged = {
                viewModel.onWishTitleChanged(it)
            })
            Spacer(modifier = Modifier.heightIn(10.dp))
            WishTextField(label = "Description", value = viewModel.wishDescriptionState, onValueChanged = {
                viewModel.onWishDesciptionChanged(it)
            })

            Spacer(modifier = Modifier.heightIn(10.dp))
            
            Button(onClick = {
                if (viewModel.wishTitleState.isNotEmpty() &&
                viewModel.wishDescriptionState.isNotEmpty()) {
                if(id != 0L) {
                    viewModel.updateWish(
                        Wish(
                            id = id,
                            title = viewModel.wishTitleState.trim(),
                            description = viewModel.wishDescriptionState.trim()
                        )
                    )
                } else {
                    viewModel.addWish(
                        Wish(
                            title = viewModel.wishTitleState.trim(),
                            description = viewModel.wishDescriptionState.trim()
                        )
                    )
                    snackMessage.value = "Wish has been created"
                }
            } else {
                snackMessage.value = "Enter fields to create a  wish "
            }
                scope.launch {
                    snackbarHostState.showSnackbar(snackMessage.value)
                    navController.navigateUp()
                }

            }) {
                Text(
                    text = if(id != 0L) "Update Wish"
                    else "Add Wish"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
){
    OutlinedTextField(value = value, onValueChange = onValueChanged, label = {Text(text = label, color = Color.Black)},
        modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor =  colorResource(id = R.color.black),
            unfocusedTextColor =  colorResource(id = R.color.black),
        )

        )
}

@Preview
@Composable
fun WishTextFieldPrev() {
    WishTextField(label = "text", value = "text", onValueChanged = {})
}