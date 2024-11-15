
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
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.view.ui.theme.DarkTextColor
import com.hope.socialmediaemotiondetection.view.ui.theme.renk2
import com.hope.socialmediaemotiondetection.view.ui.theme.renk4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(userList: List<User>) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

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
                            onValueChange = { searchText = it },
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
                                            // Arama butonu tıklama işlemi burada yapılabilir
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val filteredUsers = userList.filter {
                    it.username.contains(searchText.text, ignoreCase = true)
                }

                if (filteredUsers.isEmpty()) {
                    item {
                        Text(
                            text = "No users found.",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        )
                    }
                } else {
                    items(filteredUsers) { user ->
                        UserListItem(user = user)
                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    }
                }
            }
        }
    )
}