package com.nevratov.matur.presentation.main

import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
        Spacer(modifier = Modifier.width(12.dp))
        GenderTextField()
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

@Composable
fun RowScope.MonthTextField() {
    var month by rememberSaveable { mutableStateOf("") }
    var isOpenDialog by remember { mutableStateOf(false) }

    TextFieldWithRadioButtonOnDialog(
        modifier = Modifier
            .weight(1.4f)
            .clickable { isOpenDialog = true },
        label = stringResource(id = R.string.month_label),
        options = stringArrayResource(id = R.array.monthsOfYear),
        text = month,
        isOpenDialog = isOpenDialog,
        changeStateDialog = { isOpenDialog = it },
        changeStateText = { month = it }
    )
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

@Composable
fun GenderTextField() {
    var gender by rememberSaveable { mutableStateOf("") }
    val isOpenDialog = remember { mutableStateOf(false) }


    TextFieldWithRadioButtonOnDialog(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isOpenDialog.value = true },
        label = stringResource(id = R.string.gender_label),
        options = stringArrayResource(id = R.array.genders),
        text = gender,
        isOpenDialog = isOpenDialog.value,
        changeStateDialog = { isOpenDialog.value = it },
        changeStateText = { gender = it }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithRadioButtonOnDialog(
    modifier: Modifier,
    label: String,
    options: Array<String>,
    text: String,
    isOpenDialog: Boolean,
    changeStateDialog: (Boolean) -> Unit,
    changeStateText: (String) -> Unit
) {

    OutlinedTextField(
        modifier = modifier,
        value = text,
        enabled = false,
        singleLine = true,
        onValueChange = { changeStateText(it) },
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledTextColor = MaterialTheme.colorScheme.primary,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.outline,

            ),
        label = { Text(
            modifier = Modifier.clickable { changeStateDialog(true) },
            text = label
        ) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { changeStateDialog(true) }
            )
        }
    )

    if (isOpenDialog) {
        AlertDialog(
            onDismissRequest = { changeStateDialog(false) },
            modifier = Modifier.background(Color.White)

        ) {
            Column(modifier = Modifier.selectableGroup()) {
                options.forEach { option ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .selectable(
                            selected = option == text,
                            onClick = {
                                changeStateText(option)
                                changeStateDialog(false)
                            },
                            role = Role.Button
                        )
                        .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option == text,
                            onClick = {
                                changeStateText(option)
                                changeStateDialog(false)
                            }
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Prev() {
    MaturTheme(darkTheme = false) {
        RegisterScreenDate()
    }
}


