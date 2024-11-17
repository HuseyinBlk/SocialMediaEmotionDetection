
import android.widget.Toast
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.view.components.ActionButton
import com.hope.socialmediaemotiondetection.view.ui.theme.renk1
import com.hope.socialmediaemotiondetection.view.ui.theme.renk2
import com.hope.socialmediaemotiondetection.view.ui.theme.renk3
import com.hope.socialmediaemotiondetection.view.ui.theme.renk4
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
                brush = Brush.verticalGradient(
                    0f to renk1,
                    0.20f to renk2,
                    0.4f to renk3,
                    0.8f to renk4,
                )
            ).graphicsLayer(scaleAnim)
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(11))
                .padding(16.dp)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.4f) // Boyut artırıldı, çünkü yeni bir alan eklendi
        ) {
            Message(
                title = "Çok Az Kaldı!",
                subtitle = "Lütfen kullanıcı ad, biyografi ve ilgi alanlarınızı giriniz.",
                textColor = Color.Black,
                fontWeight = FontWeight.Normal
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Kullanıcı Adı") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Biyografi") },
                placeholder = { Text("Kendiniz hakkında birkaç cümle yazın") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                maxLines = 3
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                    .clickable { expanded = true }
                    .padding(16.dp)
            ) {
                Text(
                    text = if (selectedInterests.filterValues { it }.isEmpty())
                        "İlgili alanları seçiniz"
                    else
                        selectedInterests.filterValues { it }.keys.joinToString()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    interests.forEach { interest ->
                        DropdownMenuItem(
                            onClick = {
                                selectedInterests[interest] = !(selectedInterests[interest] ?: false)
                            },
                            text = {
                                Row {
                                    Checkbox(
                                        checked = selectedInterests[interest] ?: false,
                                        onCheckedChange = {
                                            selectedInterests[interest] = it
                                        }
                                    )
                                    Text(text = interest, modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        )
                    }
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
                containerColor = Color.White,
                contentColor = renk2
            ),
            shadowColor = renk2,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 80.dp, vertical = 16.dp) // Butonun hizalaması ve padding
                .fillMaxWidth()
        )
    }
}
