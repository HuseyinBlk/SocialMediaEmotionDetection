package com.hope.socialmediaemotiondetection.view.Login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hope.socialmediaemotiondetection.R
import com.hope.socialmediaemotiondetection.view.components.ActionButton
import com.hope.socialmediaemotiondetection.view.components.Message
import com.hope.socialmediaemotiondetection.view.ui.theme.DarkTextColor
import com.hope.socialmediaemotiondetection.view.ui.theme.renk1
import com.hope.socialmediaemotiondetection.view.ui.theme.renk2
import com.hope.socialmediaemotiondetection.view.ui.theme.renk3
import com.hope.socialmediaemotiondetection.view.ui.theme.renk4
import com.hope.socialmediaemotiondetection.view.ui.theme.renk5

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(
                brush = Brush.verticalGradient(
                    0f to renk1,
                    0.20f to renk2,
                    0.4f to renk3,
                    0.8f to renk4,

                    )
            ),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Image(painter = painterResource(R.drawable.avatar),
            contentDescription =null,
            modifier = Modifier
                .padding(top = 100.dp)
                .size(100.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Message(
            title = "Welcome Back !",
            subtitle = "Please Login.",
            textColor = Color.White,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(70.dp))

        InputField(
            leadingIconRes = R.drawable.baseline_alternate_email_24,
            placeholderText = "email",
            modifier = Modifier.padding(horizontal = 30.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        InputField(
            leadingIconRes = R.drawable.ic_key,
            placeholderText = "password",
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(horizontal = 30.dp)

        )
        Spacer(modifier.height(30.dp))
        ActionButton(
            text = "Login",
            isNavigationArrowVisible = false,
            onClicked = { navController.navigate("mainScreen") },
            colors = ButtonDefaults.buttonColors(
                containerColor = renk2,
                contentColor = Color.White
            ),
            shadowColor = renk5,
            modifier = Modifier.padding(horizontal = 50.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        ClickableText(
            text = AnnotatedString("Hesabın yoksa tıklayarak Kayıt Ol!"),
            onClick = { offset ->
                // "Kayıt Ol" tıklama olayını işleme
                // TODO: Kayıt ol ekranına yönlendirme işlemi eklenebilir
            },
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,

            ),
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(text = "Diğer giriş seçenekleri",
            style = TextStyle(
                color = Color.White,

            )
        )
        Row{

            Image(painter = painterResource(R.drawable.google),
                contentDescription = null,
                Modifier.size(70.dp))
        }
    }
}



@Composable
private fun InputField(
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    @DrawableRes leadingIconRes: Int,
    placeholderText: String
){
    var inputValue by remember { mutableStateOf("") }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .padding(start = 15.dp, end = 15.dp),
        value = inputValue,
        onValueChange ={inputValue = it},
        visualTransformation = visualTransformation,
        singleLine = true,
        shape = RoundedCornerShape(10),
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
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Medium
        ),
        leadingIcon = {
            Icon(painter = painterResource(leadingIconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
                )
        },
        placeholder = { 
            Text(text = placeholderText)
        }
    )
}
