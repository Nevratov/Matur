package com.nevratov.matur.presentation.main.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevratov.matur.R

@Composable
fun LoginScreen (
    viewModel: LoginViewModel,
    createAccountClicked:  () -> Unit
) {

    val state = viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_m),
            contentDescription = stringResource(R.string.logo_matur_description)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.login_label), fontSize = 26.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Введите данные для входа в приложение", fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(22.dp))
        Fields(state = state, viewModel = viewModel)
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.clickable { createAccountClicked() },
                text = "Создать аккаунт",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
            )
            ButtonLogin(state = state, viewModel = viewModel)
        }
    }
}

@Composable
private fun Fields(
    state: State<LoginScreenState>,
    viewModel: LoginViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        when (val currentState = state.value) {
            is LoginScreenState.Content -> {
                EmailRequest(
                    email = currentState.email,
                    onEmailChanged = { viewModel.changeEmail(it) }
                )
                PasswordRequest(
                    password = currentState.password,
                    onPasswordChanged = { viewModel.changePassword(it) }
                )
            }
            LoginScreenState.Loading -> { ShowProgressBar() }
            LoginScreenState.Initial -> { }
        }
    }
}

@Composable
private fun ColumnScope.ShowProgressBar() {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ButtonLogin(
    state: State<LoginScreenState>,
    viewModel: LoginViewModel
) {
    Button(
        enabled = state.value !is LoginScreenState.Loading,
        onClick = { viewModel.login() },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(text = stringResource(R.string.login_button))
    }
}

@Composable
private fun EmailRequest(
    email: String,
    onEmailChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = { onEmailChanged(it) },
        label = { Text(text = "Email") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        )
    )
}

@Composable
private fun PasswordRequest(
    password: String,
    onPasswordChanged: (String) -> Unit
) {
    var isVisiblePassword by remember {
        mutableStateOf(false)
    }
    val icon = if (isVisiblePassword) {
        painterResource(id = R.drawable.password_visible_ico)
    } else {
        painterResource(id = R.drawable.password_unvisible)
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        visualTransformation = if (isVisiblePassword) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = { onPasswordChanged(it) },
        label = { Text(text = "Пароль") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            IconButton(
                onClick = { isVisiblePassword = !isVisiblePassword },
            ) {
                Icon(
                    painter = icon,
                    contentDescription = "Видимость пароля"
                )
            }
        }
    )
}
