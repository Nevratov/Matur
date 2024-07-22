package com.nevratov.matur.presentation.main.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevratov.matur.R
import com.nevratov.matur.ui.theme.MaturColorLight
import com.nevratov.matur.ui.theme.MaturColorPrimary

@Composable
fun RequestDateScreen(
    viewModel: RegistrationViewModel,
    onNextClicked: () -> Unit
) {
    var day by rememberSaveable { mutableStateOf("") }
    var month by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }

    var isErrorDay by rememberSaveable { mutableStateOf(false) }
    var isErrorMonth by rememberSaveable { mutableStateOf(false) }
    var isErrorYear by rememberSaveable { mutableStateOf(false) }
    var isErrorGender by rememberSaveable { mutableStateOf(false) }

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
        Text(text = "Общие сведения", fontSize = 26.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Укажите свою дату рождения и пол", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(22.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            DayTextField(
                day = day,
                onDayChange = {
                    day = it
                    isErrorDay = false
                },
                isError = isErrorDay
            )
            Spacer(modifier = Modifier.width(12.dp))
            MonthTextField(
                month = month,
                onMonthChange = {
                    month = it
                    isErrorMonth = false
                },
                isError = isErrorMonth
            )
            Spacer(modifier = Modifier.width(12.dp))
            YearTextField(
                year = year,
                onYearChange = {
                    year = it
                    isErrorYear = false
                },
                isError = isErrorYear
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        GenderTextField(
            gender = gender,
            onGenderChange = {
                gender = it
                isErrorGender = false
            },
            isError = isErrorGender
        )
        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = {
                    if (day.isEmpty() || day.toInt() !in 1..31) isErrorDay = true
                    if (month.isEmpty()) isErrorMonth = true
                    if (year.isEmpty() || year.toInt() !in 1900..2024) isErrorYear = true
                    if (gender.isEmpty()) isErrorGender = true
                    if (isErrorDay || isErrorYear || isErrorMonth || isErrorGender) return@Button
                    viewModel.setBirthdayAndGender(day, month, year, gender)
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
private fun RowScope.DayTextField(day: String, onDayChange: (String) -> Unit, isError: Boolean) {
    OutlinedTextField(
        modifier = Modifier.weight(1f),
        value = day,
        onValueChange = { if (it.length <= DAY_MAX_CHAR) onDayChange(it) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        singleLine = true,
        label = { Text(text = stringResource(R.string.day_label)) },
        isError = isError
    )
}

@Composable
private fun RowScope.MonthTextField(month: String, onMonthChange: (String) -> Unit, isError: Boolean) {
    var isOpenDialog by remember { mutableStateOf(false) }
    val months = Months.entries.map { it.monthName }

    TextFieldWithRadioButtonOnDialog(
        modifier = Modifier
            .weight(1.4f)
            .clickable { isOpenDialog = true },
        label = stringResource(id = R.string.month_label),
        options = months,
        text = month,
        isError = isError,
        isOpenDialog = isOpenDialog,
        changeStateDialog = { isOpenDialog = it }
    ) { onMonthChange(it) }
}

@Composable
private fun RowScope.YearTextField(year: String, onYearChange: (String) -> Unit, isError: Boolean) {
    OutlinedTextField(
        modifier = Modifier.weight(1f),
        value = year,
        onValueChange = { if (it.length <= YEAR_MAX_CHAR) onYearChange(it) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        singleLine = true,
        label = { Text(text = stringResource(R.string.year_label)) },
        isError = isError
    )
}

@Composable
private fun GenderTextField(gender: String, onGenderChange: (String) -> Unit, isError: Boolean) {
    val isOpenDialog = remember { mutableStateOf(false) }
    val genders = Genders.entries.map { it.genderName }

    TextFieldWithRadioButtonOnDialog(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isOpenDialog.value = true },
        label = stringResource(id = R.string.gender_label),
        options = genders,
        text = gender,
        isError = isError,
        isOpenDialog = isOpenDialog.value,
        changeStateDialog = { isOpenDialog.value = it }
    ) { onGenderChange(it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFieldWithRadioButtonOnDialog(
    modifier: Modifier,
    label: String,
    options: List<String>,
    text: String,
    isError: Boolean,
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
            disabledBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledLabelColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.outline,
        ),
        label = {
            Text(
                modifier = Modifier.clickable { changeStateDialog(true) },
                text = label
            )
        },
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
            modifier = Modifier.background(Color.DarkGray),
            onDismissRequest = { changeStateDialog(false) },
        ) {
            Column(modifier = Modifier.selectableGroup()) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
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
                            },
                            colors = RadioButtonDefaults.colors(
                                unselectedColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

private const val YEAR_MAX_CHAR = 4
private const val DAY_MAX_CHAR = 2



