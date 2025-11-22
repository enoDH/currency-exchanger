package ooh.app.currencyexchanger.presentation.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ooh.app.currencyexchanger.presentation.viewModal.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Button(onClick = { viewModel.changeBaseExpand() }) {
                Text(uiState.baseCurrency)
            }
            DropdownMenu(
                expanded = uiState.baseCurrencyExpand,
                onDismissRequest = { viewModel.changeBaseExpand() }
            ) {
                uiState.baseCodes.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.changeBaseExpand()
                            viewModel.changeBaseCurrency(option)
                        }
                    )
                }
            }

            TextField(
                value = uiState.baseAmount.toString(),
                onValueChange = { amount: String ->
                    viewModel.changeBaseAmount(amount = amount.toDouble())
                },
                modifier = Modifier
                    .weight(0.1f)
                    .padding(horizontal = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                ),
                singleLine = true
            )

            TextField(
                value = uiState.targetAmount.toString(),
                onValueChange = { amount: String ->
                    viewModel.changeTargetAmount(amount = amount.toDouble())
                },
                modifier = Modifier
                    .weight(0.1f)
                    .padding(horizontal = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                ),
                singleLine = true
            )

            Button(onClick = { viewModel.changeTargetExpand() }) {
                Text(uiState.targetCurrency)
            }
            DropdownMenu(
                expanded = uiState.targetCurrencyExpand,
                onDismissRequest = { viewModel.changeTargetExpand() }
            ) {
                uiState.baseCodes.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.changeTargetExpand()
                            viewModel.changeTargetCurrency(option)
                        }
                    )
                }
            }
        }
    }
}