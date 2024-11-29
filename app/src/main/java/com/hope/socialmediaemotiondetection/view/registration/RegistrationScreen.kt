package com.hope.socialmediaemotiondetection.view.registration

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hope.socialmediaemotiondetection.R
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.view.components.ActionButton
import com.hope.socialmediaemotiondetection.view.components.Message
import com.hope.socialmediaemotiondetection.viewmodel.LoginAndRegisterViewModel

@Composable
fun RegistrationScreen(
    modifier: Modifier= Modifier,
    navController: NavController,
    viewModel: LoginAndRegisterViewModel = hiltViewModel()
    ){
    var animation by remember { mutableStateOf(true) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (animation) 0.5f else 1f,
        animationSpec = tween(durationMillis = 400, easing = EaseOutCirc), label = "LoginAnimate"
    )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            ).graphicsLayer(scaleAnim),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        var inputTextEmail by remember { mutableStateOf("") }
        var inputTextPassword by remember { mutableStateOf("") }
        val registerResult by viewModel.registerResult.collectAsState()
        val context = LocalContext.current
        var isLoggingIn by remember { mutableStateOf(true) }

        LaunchedEffect(registerResult) {
            when (registerResult) {
                is Resource.Idle -> {
                    animation = false
                    isLoggingIn = false
                }
                is Resource.Loading -> {
                    if (isLoggingIn) {
                        Toast.makeText(context, "Yükleniyor...", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Success -> {
                    if (isLoggingIn) {
                        navController.navigate("checkUserNameScreen"){
                            popUpTo("registerScreen") { inclusive = true }
                        }
                        isLoggingIn = false
                    }
                }
                is Resource.Failure -> {
                    if (isLoggingIn) {
                        Toast.makeText(
                            context,
                            "Error: ${(registerResult as Resource.Failure).message}",
                            Toast.LENGTH_LONG
                        ).show()
                        isLoggingIn = false
                    }
                }
            }
        }

        Image(painter = painterResource(R.drawable.registerscreenphoto),
            contentDescription =null,
            modifier = Modifier
                .padding(top = 10.dp)
                .size(300.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Message(
            title = "Welcome Aboard !",
            subtitle = "Don't let anything upset you.",
            textColor = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(40.dp))

        InputField(
            leadingIconRes = R.drawable.baseline_alternate_email_24,
            placeholderText = "email",
            modifier = Modifier.padding(horizontal = 30.dp),
            onInputTextChange = { inputTextEmail = it },
            inputValue = inputTextEmail
        )
        Spacer(modifier = Modifier.height(10.dp))

        InputField(
            leadingIconRes = R.drawable.ic_key,
            placeholderText = "password",
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(horizontal = 30.dp),
            onInputTextChange = { inputTextPassword = it },
            inputValue = inputTextPassword
        )
        Spacer(modifier.height(30.dp))
        ActionButton(
            text = "Register",
            isNavigationArrowVisible = false,
            onClicked = {
                isLoggingIn = true
                viewModel.register(inputTextEmail, inputTextPassword)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier.padding(horizontal = 50.dp)
        )

    }
}

@Composable
private fun InputField(
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    @DrawableRes leadingIconRes: Int,
    placeholderText: String,
    inputValue : String,
    onInputTextChange : (String) -> Unit,
){

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .padding(start = 15.dp, end = 15.dp),
        value = inputValue,
        onValueChange =onInputTextChange,
        visualTransformation = visualTransformation,
        singleLine = true,
        shape = RoundedCornerShape(10),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedTextColor = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.9f) else Color.Black.copy(alpha = 0.9f),
            unfocusedTextColor = Color.Black,
            focusedPlaceholderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.Black,
            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
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
