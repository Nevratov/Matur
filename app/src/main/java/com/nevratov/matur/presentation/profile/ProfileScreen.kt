package com.nevratov.matur.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nevratov.matur.R
import com.nevratov.matur.navigation.NavigationState
import com.nevratov.matur.presentation.BottomNavigationBar
import com.nevratov.matur.ui.theme.MaturAlternativeColor

@Composable
fun ProfileScreen(
    navigationState: NavigationState
) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navigationState = navigationState) }
    )  { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(text = "Разработчик трудится над контентом...")
                CircularProgressIndicator()
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.profile_navigation_item),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaturAlternativeColor,
            titleContentColor = MaterialTheme.colorScheme.background
        )
    )
}