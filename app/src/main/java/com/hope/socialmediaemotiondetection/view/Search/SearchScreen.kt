
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.view.ui.theme.DarkTextColor
import com.hope.socialmediaemotiondetection.view.ui.theme.renk4
import com.hope.socialmediaemotiondetection.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val userSearchResult by viewModel.userSearchResult.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                viewModel.searchUsersByUsername(searchText.text)},
                            placeholder = {
                                Text(
                                    text = "Aramak İstediğiniz Kullanıcıyı Giriniz.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White
                                    )
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(1f),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .clickable {

                                        }
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                focusedTextColor = DarkTextColor,
                                unfocusedTextColor = DarkTextColor,
                                focusedPlaceholderColor = DarkTextColor,
                                unfocusedPlaceholderColor = DarkTextColor,
                                focusedLeadingIconColor = DarkTextColor,
                                unfocusedLeadingIconColor = DarkTextColor,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = renk4
                )
            )
        },
        bottomBar = {
            NavigationBar (
                modifier = Modifier.height(80.dp),
                containerColor = renk4
            ){
                NavigationBarItem(
                    onClick = {
                        // Tıklama olayını buraya ekleyin
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home Icon",
                            tint = Color.White
                        )
                    }
                )
                NavigationBarItem(
                    onClick = {
                        // Tıklama olayını buraya ekleyin
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.White
                        )
                    }
                )
                NavigationBarItem(
                    onClick = {
                        // Tıklama olayını buraya ekleyin
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Icon",
                            tint = Color.White
                        )
                    }
                )
            }
        },
        content = { paddingValues ->
            var textState by remember { mutableStateOf("") }
            when (val result = userSearchResult) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(result.data) { user ->
                            UserItem(user)
                        }
                    }
                }
                is Resource.Failure -> {
                    textState = result.message ?: "Bir hata oluştu"
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = textState,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        textState = ""
                        Text(
                            text = textState,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun UserItem(user: User) {
    // Kullanıcıyı listele
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Kullanıcıyı tıklama işlemi
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Icon",
                modifier = Modifier.size(40.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = user.username, style = MaterialTheme.typography.bodyLarge)
        }
    }
}