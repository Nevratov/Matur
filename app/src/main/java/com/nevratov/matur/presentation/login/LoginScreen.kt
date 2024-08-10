package com.nevratov.matur.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onCreateAccountClicked:  () -> Unit
) {
    val screenState = viewModel.screenState.collectAsState()

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
            text = stringResource(R.string.input_data_login_label), fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(22.dp))
        LoginScreenContent(
            screenState = screenState,
            viewModel = viewModel,
            onCreateAccountClicked = onCreateAccountClicked
        )
    }
}

@Composable
private fun LoginScreenContent(
    screenState: State<LoginScreenState>,
    viewModel: LoginViewModel,
    onCreateAccountClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        when (val currentState = screenState.value) {
            is LoginScreenState.Content -> {
                LoginForm(
                    screenState = currentState,
                    viewModel = viewModel,
                    onCreateAccountClicked = onCreateAccountClicked
                )
            }
            LoginScreenState.Loading -> { ShowProgressBar() }
        }
    }
}

@Composable
private fun LoginForm(
    screenState: LoginScreenState.Content,
    viewModel: LoginViewModel,
    onCreateAccountClicked: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf(screenState.email) }
    var password by rememberSaveable { mutableStateOf(screenState.password) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = { email = it },
        label = { Text(text = "Email") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        )
    )

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
        onValueChange = { password = it },
        label = { Text(text = stringResource(R.string.password_label)) },
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
                    contentDescription = stringResource(R.string.password_visibility_description)
                )
            }
        }
    )
    Spacer(modifier = Modifier.height(40.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { viewModel.login(email = email, password = password) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.login_button))
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
