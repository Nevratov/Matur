package com.nevratov.matur.presentation.main.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nevratov.matur.R
import com.nevratov.matur.ui.theme.MaturTheme

@Composable
fun AuthScreen(
    paddingValues: PaddingValues,
//    onLoginClicked: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel()

    val email = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_m),
            contentDescription = stringResource(R.string.logo_matur_description)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(R.string.login_label), fontSize = 26.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Введите данные для входа в приложение", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(22.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            EmailRequest(
                emailState = email,
                onEmailChanged = {
                    email.value = it
                }
            )
            PasswordRequest(
                passwordState = password,
                onPasswordChanged = {
                    password.value = it
                }
            )
        }
        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = {
//                    onLoginClicked()
                    viewModel.login(email.value, password = password.value)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = stringResource(R.string.login_button))
            }
        }
    }
}

@Composable
private fun EmailRequest(
    emailState: MutableState<String>,
    onEmailChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = emailState.value,
        onValueChange = { onEmailChanged(it) },
        label = { Text(text = "Email") }
    )
}

@Composable
private fun PasswordRequest(
    passwordState: MutableState<String>,
    onPasswordChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = passwordState.value,
        onValueChange = { onPasswordChanged(it) },
        label = { Text(text = "Пароль") }
    )
}


@Preview
@Composable
fun PreviewAuthScreen() {
    MaturTheme(darkTheme = false) {
        AuthScreen(PaddingValues())
    }
}