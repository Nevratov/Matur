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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevratov.matur.R

@Composable
fun RequestNameScreen(
    onNextOnClickListener: (name: String) -> Unit,
    paddingValues: PaddingValues
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var isErrorFirstName by rememberSaveable { mutableStateOf(false) }
    var isErrorLastName by rememberSaveable { mutableStateOf(false) }

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
        Text(text = "Создать аккаунт", fontSize = 26.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Введите своё имя", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(22.dp))
        FirstNameTextField(
            firstName = firstName,
            onFirstNameChange = {
                firstName = it
                isErrorFirstName = false
            },
            isError = isErrorFirstName
        )
        Spacer(modifier = Modifier.height(8.dp))
        LastNameTextField(
            lastName = lastName,
            onLastNameChange = {
                lastName = it
                isErrorLastName = false
            },
            isError = isErrorLastName
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = {
                if (firstName.isEmpty()) isErrorFirstName = true
                if (lastName.isEmpty()) isErrorLastName = true
                if (isErrorFirstName || isErrorLastName) return@Button
                onNextOnClickListener(firstName)
            }
            ) {
                Text(text = stringResource(R.string.next_label))
            }
        }
    }
}

@Composable
private fun FirstNameTextField(firstName: String, onFirstNameChange: (String) -> Unit, isError: Boolean) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = firstName,
        onValueChange = { onFirstNameChange(it) },
        label = { Text(text = stringResource(R.string.name_label)) },
        isError = isError
    )
}

@Composable
private fun LastNameTextField(lastName: String, onLastNameChange: (String) -> Unit, isError: Boolean) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = lastName,
        onValueChange = { onLastNameChange(it) },
        label = { Text(text = stringResource(R.string.family_label)) },
        isError = isError
    )
}








