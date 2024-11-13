
import android.widget.Toast
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
import androidx.compose.ui.text.font.FontWeight
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.view.components.ActionButton
import com.hope.socialmediaemotiondetection.view.ui.theme.renk1
import com.hope.socialmediaemotiondetection.view.ui.theme.renk2
import com.hope.socialmediaemotiondetection.view.ui.theme.renk3
import com.hope.socialmediaemotiondetection.view.ui.theme.renk4
import com.hope.socialmediaemotiondetection.view.components.Message
import com.hope.socialmediaemotiondetection.view.ui.theme.renk5


@Composable
fun GetUsernameScreen() {
    val interests = listOf("Teknoloji", "Sanat", "Spor", "Bilim", "Edebiyat")
    var expanded by remember { mutableStateOf(false) }
    //seçilen ilgi alanları için liste
    val selectedInterests = remember { mutableStateMapOf<String, Boolean>() }
    Box(modifier = Modifier
        .background(
            brush = Brush.verticalGradient(
                0f to renk1,
                0.20f to renk2,
                0.4f to renk3,
                0.8f to renk4,

                )
        )
        .fillMaxSize()
        .padding(16.dp)
        ,
        contentAlignment = Alignment.Center
    )
    {

        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(11))
                .padding(16.dp)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.3f)
        )
        {
            Message(
                title = "Çok Az Kaldı!",
                subtitle = "Lütfen kullanıcı ad ve ilgi alanlarınızı giriniz.",
                textColor = Color.Black,
                fontWeight = FontWeight.Normal
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Kullanıcı Adı") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                    .clickable { expanded = true }
                    .padding(16.dp)
            )
            {
                Text(text = if (selectedInterests.filterValues { it }
                        .isEmpty()) "İlgili alanları seçiniz" else selectedInterests.filterValues { it }.keys.joinToString())

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    interests.forEach { interest ->
                        DropdownMenuItem(
                            onClick = {
                                selectedInterests[interest] =
                                    !(selectedInterests[interest] ?: false)
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
        // ActionButton, Column dışında ve beyaz kutunun altına yerleştirildi
        ActionButton(
            text = "Devam Et",
            isNavigationArrowVisible = true,
            onClicked = {
                // Tıklama işlemi burada işlenir
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = renk2
            ),
            shadowColor = renk2,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 80.dp, vertical = 16.dp) // Butonun hizalaması ve padding
        )
    }
}

