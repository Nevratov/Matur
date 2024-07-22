package com.nevratov.matur.presentation.main.registration

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevratov.matur.R
import com.nevratov.matur.ui.theme.MaturTheme

@Composable
fun RequestEmailScreen(
    viewModel: RegistrationViewModel,
    onNextClicked: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isErrorEmail by remember { mutableStateOf(false) }

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
        Text(text = "Электронная почта", fontSize = 26.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Укажите email адрес почтового ящика", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(22.dp))
        EmailTextField(
            email = email,
            onEmailChange = {
                email = it
                isErrorEmail = false
            },
            isError = isErrorEmail
        )
        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = {
                    if (!email.contains('@') || !email.contains('.')) {
                        isErrorEmail = true
                        return@Button
                    }
                    viewModel.setEmail(email)
                    onNextClicked()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = stringResource(R.string.next_label))
            }
        }
    }
}

@Composable
private fun EmailTextField(email: String, onEmailChange: (String) -> Unit, isError: Boolean) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = email,
        placeholder = {
            Text(
                text = "example@gmail.com",
                color = Color.Gray,
            )
        },
        onValueChange = { onEmailChange(it) },
        isError = isError,
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
        singleLine = true
    )
}
