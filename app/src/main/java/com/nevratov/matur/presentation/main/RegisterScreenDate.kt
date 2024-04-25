package com.nevratov.matur.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevratov.matur.R
import com.nevratov.matur.ui.theme.MaturTheme


@Composable
fun RegisterScreenDate() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_m),
            contentDescription = stringResource(R.string.logo_matur_description)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Общие сведения", fontSize = 26.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Укажите свою дату рождения и пол", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(22.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            DayTextField()
            Spacer(modifier = Modifier.width(12.dp))
            MonthTextField()
            Spacer(modifier = Modifier.width(12.dp))
            YearTextField()
        }
        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = stringResource(R.string.next_label))
            }
        }


    }
}

@Composable
fun RowScope.DayTextField() {
    var text by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.weight(1f),
        value = text,
        onValueChange = { text = it },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary
        ),
        label = { Text(text = stringResource(R.string.day_label)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.MonthTextField() {
    var text by rememberSaveable { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }


        OutlinedTextField(
            modifier = Modifier
                .weight(1.4f)
                .padding(0.dp)
                .clickable { openDialog.value = true }
            ,
            value = text,
            enabled = false,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
            singleLine = true,
            onValueChange = { text = it },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledTextColor = MaterialTheme.colorScheme.primary,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.outline,

            ),
            label = { Text(
                modifier = Modifier.clickable { openDialog.value = true },
                text = stringResource(R.string.month_label)
            ) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { openDialog.value = true }
                )
            }
        )



    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            modifier = Modifier.background(Color.White)

        ) {
            val months = arrayOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сетнябрь", "Октябрь", "Ноябрь", "Декабрь")
            var selectedOption by rememberSaveable { mutableStateOf(months[0]) }

            Column(modifier = Modifier.selectableGroup()) {
                months.forEach { month ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .selectable(
                            selected = text == selectedOption,
                            onClick = {
                                selectedOption = month
                                text = month
                                openDialog.value = false
                            },
                            role = Role.Button
                        )
                        .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = text == selectedOption,
                            onClick = {
                                selectedOption = month
                                text = month
                                openDialog.value = false
                            }
                        )
                        Text(
                            text = month,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.YearTextField() {
    var text by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.weight(1f),
        value = text,
        onValueChange = { text = it },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.secondary
        ),
        singleLine = true,
        label = { Text(text = stringResource(R.string.year_label)) }
    )
}



@Preview
@Composable
fun Prev() {
    MaturTheme(darkTheme = false) {
        RegisterScreenDate()
    }
}


