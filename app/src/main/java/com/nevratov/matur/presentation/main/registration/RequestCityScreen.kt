package com.nevratov.matur.presentation.main.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevratov.matur.R
import com.nevratov.matur.domain.entity.City

@Composable
fun RequestCityScreen(
    viewModel: RegistrationViewModel,
    onNextClicked: () -> Unit
) {
    val state = viewModel.regState.collectAsState()

    var city by remember { mutableStateOf("") }
    var isErrorCity by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_m),
            contentDescription = stringResource(R.string.logo_matur_description)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Город", fontSize = 26.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Начните вводить название вашего города", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(22.dp))
        CityTextField(
            email = city,
            onCityChange = { nameCity ->
                city = nameCity
                isErrorCity = false
                if (nameCity.length > 2) {
                    viewModel.getCitiesByName(nameCity)
                }
            },
            isError = isErrorCity
        )
        Spacer(modifier = Modifier.height(40.dp))

        val currentState = state.value
        val cit = if (currentState is RegistrationState.RequestCity) currentState.cities else listOf()
        ShowCities(
            cities = cit,
            onCitySelected = {
                viewModel.setCity(it)
                onNextClicked()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowCities(
    cities: List<City> = listOf(),
    onCitySelected: (City) -> Unit
) {

    LazyColumn() {
        items(items = cities, key = {it.id}) { city->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onCitySelected(city) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),

            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = city.name,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Divider(color = Color.Gray.copy(alpha = 0.3f))
        }
    }
}

@Composable
private fun CityTextField(email: String, onCityChange: (String) -> Unit, isError: Boolean) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = email,
        placeholder = {
            Text(
                text = "Казань",
                color = Color.Gray,
            )
        },
        onValueChange = { onCityChange(it) },
        isError = isError,
        label = { Text("Город") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        singleLine = true
    )
}
