package com.ds.profiledirectory.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.ds.profiledirectory.data.model.User
import com.ds.profiledirectory.data.model.UserList
import com.ds.profiledirectory.ui.ShowError
import com.ds.profiledirectory.ui.ShowLoading
import com.ds.profiledirectory.ui.theme.ProfileDirectoryTheme
import com.ds.profiledirectory.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val profileViewModel by viewModels<ProfileViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileDirectoryTheme {
//                NavigationApp(profileViewModel = profileViewModel)
                HomeScreen(profileViewModel = profileViewModel)
            }
        }
    }
}

/*@Composable
fun NavigationApp(profileViewModel: ProfileViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") { HomeScreen(profileViewModel,navController) }
        composable("profile_detail_screen") { ProfileDetailScreen(null,navController) }
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(profileViewModel: ProfileViewModel/*, navController: NavHostController*/) {

    var showDialog by remember { mutableStateOf(false) }
    var isAPICalled by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White
            ), title = { Text(text = AppConstants.APP_NAME) })
        },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FabAddButton(onClick = { showDialog = true
            })
        },
    ) { innerPadding ->

        if (showDialog) {
            InputDialog(
                title = "User List No",
                message = "",
                onDismiss = { showDialog = false }) {
                showDialog = false
                //Call API
                profileViewModel.fetchUserList(it.toInt())
                isAPICalled = true

            }
        }

        if (isAPICalled) {
            val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
            ShowUserList(uiState, modifier = Modifier.padding(innerPadding))
//            isAPICalled = false
        }

    }


}


@Composable
fun ShowUserList(uiState: UiState<UserList>, modifier: Modifier) {
    when (uiState) {

        is UiState.Success -> ItemList(uiState.data, modifier)
        is UiState.Loading -> ShowLoading()
        is UiState.Error -> ShowError(uiState.message)

    }
}

@Composable
fun ItemList(userList: UserList, modifier: Modifier) {
    var isSelected by remember { mutableStateOf(false) }
    var i by remember { mutableStateOf(0) }
    LazyColumn {
        itemsIndexed(userList.results) {index, it->
            UserItem(it,index, modifier) { a->
                i = a
                isSelected = true
            }
        }
    }
    if (isSelected){
//        DetailsScreen(userList.results[i] )
        isSelected = false
    }
}



@Composable
fun InputDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = {
            Column {
                Text(text = message)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter Number") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(text)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text("Dismiss")
            }
        }
    )
}


@Composable
fun FabAddButton(onClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier,
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(Icons.Filled.Add, "Small floating action button.")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun UserItem(user: User, index: Int, modifier: Modifier, onProfileSelect:(index: Int) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth(1f)
            .height(180.dp)
            .padding(8.dp)
            .clickable(onClick = { onProfileSelect(index) })
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth(0.4f)
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(25.dp)),
                contentScale = ContentScale.FillHeight,
                model = user.picture.large,
                contentDescription = "User Profile Image"
            )
            Column {
                Text(
                    text = user.name.first + " " + user.name.last,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp
                )
                Text(
                    text = user.location.street.number.toString() + " " + user.location.street.name,
                    style = MaterialTheme.typography.titleLarge
                )


            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    ProfileDirectoryTheme {

    }
}