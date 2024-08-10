package com.nevratov.matur.presentation.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import com.nevratov.matur.R

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    val permissionGranted = remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted.value = isGranted
        if (!isGranted) showAlert = true
    }

    LaunchedEffect(key1 = Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            when (isGranted) {
                true -> {
                    permissionGranted.value = true
                }

                false -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            permissionGranted.value = true
        }
    }

    if (!permissionGranted.value) {
        if (showAlert) {
            ShowAlertDialogRequestNotificationPermission(
                context = context,
                onDismissRequest = { showAlert = false }
            )
        }
    }
}

@Composable
private fun ShowAlertDialogRequestNotificationPermission(
    context: Context,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.notification_permission_title)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.notification_permission_description)
            )
        },
        onDismissRequest = {
            onDismissRequest()
            Toast.makeText(
                context,
                context.getString(R.string.notification_permission_not_allowed_toast),
                Toast.LENGTH_SHORT
            ).show()
        },
        confirmButton = {
            Text(
                modifier = Modifier.clickable {
                    onDismissRequest()
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    context.startActivity(intent)
                },
                text = stringResource(R.string.notification_permission_confirm_button)
            )
        }
    )
}