package com.hope.socialmediaemotiondetection.view.registration

import android.widget.Toast
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.view.components.ActionButton
import com.hope.socialmediaemotiondetection.view.components.Message
import com.hope.socialmediaemotiondetection.viewmodel.UserDetailsViewModel

@Composable
fun GetUsernameScreen(
    navController: NavController,
    viewModel : UserDetailsViewModel = hiltViewModel()
) {
    var animation by remember { mutableStateOf(true) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (animation) 0.3f else 1f,
        animationSpec = tween(durationMillis = 500, easing = EaseOutCirc), label = "detailsAnimate"
    )
    val interests = listOf("Teknoloji", "Sanat", "Spor", "Bilim", "Edebiyat")
    var expanded by remember { mutableStateOf(false) }
    val selectedInterests = remember { mutableStateMapOf<String, Boolean>() }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    val userDetailResult by viewModel.registerUserResult.collectAsState()
    val context = LocalContext.current
    var isLoginInProcess by remember { mutableStateOf(false) }

    LaunchedEffect(userDetailResult) {
        when (userDetailResult) {
            is Resource.Idle -> {
                animation = false
                isLoginInProcess = false
            }
            is Resource.Loading -> {
                if (isLoginInProcess) {
                    Toast.makeText(context, "Yükleniyor...", Toast.LENGTH_SHORT).show()
                }
            }
            is Resource.Success -> {
                if (isLoginInProcess) {
                    navController.navigate("mainScreen") {
                        popUpTo("userDetailsScreen") { inclusive = true }
                    }
                    isLoginInProcess = false
                }
            }
            is Resource.Failure -> {
                if (isLoginInProcess) {
                    Toast.makeText(
                        context,
                        "Error: ${(userDetailResult as Resource.Failure).message}",
                        Toast.LENGTH_LONG
                    ).show()
                    isLoginInProcess = false
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background
            ).graphicsLayer(scaleAnim)
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(11))
                .padding(16.dp)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.5f)
        ) {
            Message(
                title = "Çok Az Kaldı!",
                subtitle = "Lütfen kullanıcı ad, biyografi ve ilgi alanlarınızı giriniz.",
                textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Normal
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Kullanıcı Adı") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Biyografi") },
                minLines = 2,
                placeholder = { Text("Kendiniz hakkında birkaç cümle yazın") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp, bottom = 8.dp),
                maxLines = 2
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(1.dp, Color.Gray)
                    .clickable { expanded = true }
                    .padding(30.dp)
            ) {
                Text(
                    text = if (selectedInterests.filterValues { it }.isEmpty())
                        "İlgili alanları seçiniz"
                    else
                        selectedInterests.filterValues { it }.keys.joinToString(),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    focusable = true,
                    clippingEnabled = false
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                interests.forEach { interest ->
                    DropdownMenuItem(
                        onClick = {
                            selectedInterests[interest] = !(selectedInterests[interest] ?: false)
                        },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = selectedInterests[interest] ?: false,
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.secondary,
                                        uncheckedColor = Color.Transparent,
                                        checkmarkColor = MaterialTheme.colorScheme.onSecondary
                                    )
                                )
                                Text(
                                    text = interest,
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    )
                }
            }
        }

        ActionButton(
            text = "Devam Et",
            isNavigationArrowVisible = true,
            onClicked = {
                val selectedInterestsList = selectedInterests.filter { it.value }.keys.toList()
                isLoginInProcess = true
                viewModel.registerUserDetails(username,"",bio,selectedInterestsList)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 80.dp, vertical = 16.dp) // Butonun hizalaması ve padding
                .fillMaxWidth()
        )
    }
}
