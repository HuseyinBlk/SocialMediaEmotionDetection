package com.hope.socialmediaemotiondetection.view.Search

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.viewmodel.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {

    val textFieldValueSaver =
        androidx.compose.runtime.saveable.Saver<TextFieldValue, Pair<String, TextRange>>(
            save = { value -> Pair(value.text, value.selection) },
            restore = { (text, selection) -> TextFieldValue(text, selection) }
        )

    var searchText by rememberSaveable(stateSaver = textFieldValueSaver) { mutableStateOf(TextFieldValue("")) }
    LaunchedEffect(searchText.text) {
        viewModel.searchUsersByUsername(searchText.text)
    }
    val userSearchResult by viewModel.userSearchResult.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                placeholder = {
                    Text(
                        text = "Aramak İstediğiniz Kullanıcıyı Giriniz.",
                        style = MaterialTheme.typography.bodyMedium.copy
                            (color = MaterialTheme.colorScheme.onBackground)
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(1f),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .clickable {}
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Divider(modifier = Modifier.fillMaxWidth()
                .padding(top = 75.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f) // İçeriğin geri kalan alanı kaplaması için
        ) {
            var textState by remember { mutableStateOf("") }
            when (val result = userSearchResult) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(result.data) { user ->
                            UserItem(user, viewModel)
                        }
                    }
                }
                is Resource.Failure -> {
                    textState = result.message ?: "Bir hata oluştu"
                    Box(
                        modifier = Modifier.fillMaxSize(),
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
                        modifier = Modifier.fillMaxSize(),
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
    }
}
@Composable
fun UserItem(
    user: User,
    viewModel: SearchViewModel
) {
    val openDialog = remember { mutableStateOf(false) }
    val followCheckResult by viewModel.followCheckResult.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isFollowing by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

    if (openDialog.value) {
        LaunchedEffect(openDialog.value) {
            isLoading = true
            viewModel.checkIfUserIsFollowing(user.userId)
            isLoading = false
        }
    }
    // Takip durumu güncellemesi
    LaunchedEffect(followCheckResult) {
        when (followCheckResult) {
            is Resource.Success -> {
                isFollowing = (followCheckResult as Resource.Success<Boolean>).data
            }
            is Resource.Failure -> {
                isFollowing = false
            }
            else -> Unit
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                CoroutineScope(Dispatchers.Main).launch {
                    inputMethodManager.hideSoftInputFromWindow((context as? android.app.Activity)?.currentFocus?.windowToken, 0)
                    delay(50) // Klavyenin kapanması için kısa bir gecikme
                    openDialog.value = true
                }
            },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Tüm içeriği yatayda ortalar
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Icon",
                modifier = Modifier.size(60.dp), // İkon boyutunu büyüttük
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = user.username,
                style = MaterialTheme.typography.titleMedium, // Daha büyük bir yazı stili
                modifier = Modifier.align(Alignment.CenterVertically)
            )

        }
    }

    if (openDialog.value) {
        UserProfileDialog(
            openDialog = openDialog,
            user = user,
            isLoading = isLoading,
            isFollowing = isFollowing,
            onFollowClick = {
                viewModel.followUser(user.userId)
                viewModel.checkIfUserIsFollowing(user.userId)
            },
            onUnfollowClick = {
                viewModel.unfollowUser(user.userId)
                viewModel.checkIfUserIsFollowing(user.userId)
            }
        )
    }
}


@Composable
fun UserProfileDialog(
    openDialog: MutableState<Boolean>,
    user: User,
    isLoading: Boolean,
    isFollowing: Boolean,
    onFollowClick: () -> Unit,
    onUnfollowClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = {
            Text(
                text = "Profil",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Lütfen bekleyin...",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(100.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = user.username,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = user.bio ?:" ",
                        style = MaterialTheme.typography.titleMedium, // Daha büyük bir yazı stili
                        textAlign = TextAlign.Start
                    )

                    Button(
                        onClick = {
                            if (isFollowing) {
                                onUnfollowClick()
                            } else {
                                onFollowClick()
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFollowing) Color.Red else Color.Blue
                        )
                    ) {
                        Text(
                            text = if (isFollowing) "Takipten Çık" else "Takip Et",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        },
        confirmButton = {}
    )
}