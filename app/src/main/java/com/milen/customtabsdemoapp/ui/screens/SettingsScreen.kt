package com.milen.customtabsdemoapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.milen.customtabsdemoapp.R
import com.milen.customtabsdemoapp.ui.viewmodel.AppViewModel
import com.milen.customtabsdemoapp.ui.viewmodel.UrlValidationError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val loginUrl by viewModel.loginUrl.collectAsState()
    val editingUrl by viewModel.editingUrl.collectAsState()
    val validationError by viewModel.urlValidationError.collectAsState()

    LaunchedEffect(loginUrl) {
        viewModel.initEditingUrl(loginUrl)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_desc_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = editingUrl,
                onValueChange = { viewModel.setEditingUrl(it) },
                label = { Text(stringResource(R.string.login_url_label)) },
                isError = validationError != null,
                supportingText = {
                    validationError?.let { error ->
                        Text(
                            text = when (error) {
                                UrlValidationError.NOT_HTTPS -> stringResource(R.string.error_url_must_be_https)
                                UrlValidationError.INVALID_FORMAT -> stringResource(R.string.error_url_invalid)
                                UrlValidationError.NO_HOST -> stringResource(R.string.error_url_no_host)
                            },
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.saveUrl()
                        onNavigateBack()
                    },
                    enabled = viewModel.isUrlValid(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.btn_save))
                }

                OutlinedButton(
                    onClick = {
                        viewModel.resetToDefault()
                        viewModel.initEditingUrl(com.milen.customtabsdemoapp.data.UrlStore.DEFAULT_URL)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.btn_reset_demo_url))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_back))
            }
        }
    }
}
