package com.ds.profiledirectory.ui.profile

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.ds.profiledirectory.R
import com.ds.profiledirectory.data.model.User
import com.ds.profiledirectory.data.model.UserList
import com.ds.profiledirectory.ui.ShowError
import com.ds.profiledirectory.ui.ShowLoading
import com.ds.profiledirectory.ui.profile.ui.theme.ProfileDirectoryTheme
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetail : ComponentActivity() {
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            ProfileDirectoryTheme {
//                profileViewModel.fetchUserList(1)

//                DetailsScreen(profileViewModel )
                  val user = Gson().fromJson(intent.getStringExtra("user"),User::class.java)
                  ProfileDetailScreen(user = user)
            }


        }

    }
}

/*@Composable
fun DetailsScreen(profileModel: ProfileViewModel){
    val uiState by profileModel.uiState.collectAsStateWithLifecycle()
    ShowUserList(uiState = uiState)
}

@Composable
fun ShowUserList(uiState: UiState<UserList>) {
    when (uiState) {

        is UiState.Success -> ProfileDetailScreen(uiState.data.results[0])
        is UiState.Loading -> ShowLoading()
        is UiState.Error -> ShowError(uiState.message)

    }
}*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    user: User?/*,
    navController: NavHostController*/
) {
    var isBackPressed by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            IconButton(onClick = { isBackPressed = true}) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_navigate_up),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        ProfileContent(user = user, modifier = Modifier.padding(innerPadding))
    }

    if (isBackPressed){
        val context = LocalContext.current
        context.getActivity()?.finish()

    }

}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

@ExperimentalMaterial3Api
@Composable
private fun ProfileContent(
    user: User?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            modifier = Modifier
                .size(280.dp, 120.dp)
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(25.dp)),
            contentScale = ContentScale.FillHeight,
            model = user?.picture?.large,
            contentDescription = "User Profile Image"
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = user?.name?.first + " " + user?.name?.last,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        user?.phone?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Divider(thickness = 1.dp)

        Card( modifier = Modifier.padding(16.dp)
            ) {
            Text(
                text = "Gender : ${user?.gender}",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "DOB : ${user?.dob}",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Address : ${user?.location?.street?.number}" +
                        "${user?.location?.street?.name}" + "${user?.location?.city}" +
                        "${user?.location?.state}" + "${user?.location?.state}",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Email : ${user?.email}",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ProfileDirectoryTheme {
//        ProfileDetailScreen()
    }
}