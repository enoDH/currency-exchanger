package ooh.app.currencyexchanger.presentation.ui.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StackedBarChart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ooh.app.currencyexchanger.presentation.ui.components.Currencies
import ooh.app.currencyexchanger.presentation.ui.components.CustomDialog
import ooh.app.currencyexchanger.presentation.viewModal.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        onClick = { viewModel.changeBaseExpand() },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(uiState.baseCurrency)
                    }

                    if (uiState.baseCurrencyExpand) {
                        CustomDialog(onDismissRequest = { !uiState.baseCurrencyExpand }) {
                            Currencies(
                                currencies = uiState.baseCodes,
                                onClickCurrency = { item ->
                                    viewModel.changeBaseCurrency(item)
                                    viewModel.changeBaseExpand()
                                },
                                onBack = viewModel::changeBaseExpand
                            )
                        }
                    }

                    TextField(
                        value = uiState.baseAmount.toString(),
                        onValueChange = { amount ->
                            viewModel.changeBaseAmount(amount.toDouble())
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    TextField(
                        value = uiState.targetAmount.toString(),
                        onValueChange = { amount ->
                            viewModel.changeTargetAmount(amount.toDouble())
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = { viewModel.changeTargetExpand() },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(uiState.targetCurrency)
                    }

                    if (uiState.targetCurrencyExpand) {
                        CustomDialog(onDismissRequest = { !uiState.targetCurrencyExpand }) {
                            Currencies(
                                currencies = uiState.baseCodes,
                                onClickCurrency = { item ->
                                    viewModel.changeTargetCurrency(item)
                                    viewModel.changeTargetExpand()
                                },
                                onBack = viewModel::changeTargetExpand
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.showButtonSave) {
                    Button(
                        onClick = {
                            viewModel.savePair()
                            Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save Pair")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Column(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                val selectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                val unselectedColor = Color.Transparent

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (!uiState.displayFavoritePairs) selectedColor else unselectedColor,
                    modifier = Modifier.clickable(onClick = viewModel::displayPairs)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Currency Rates")
                        Icon(Icons.Outlined.StackedBarChart, contentDescription = null)
                    }
                }

                if (uiState.pairs.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (uiState.displayFavoritePairs) selectedColor else unselectedColor,
                        modifier = Modifier.clickable(onClick = viewModel::displayFavoritePairs)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Favorite Rates")
                            Icon(Icons.Outlined.Star, contentDescription = null)
                        }
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (uiState.displayFavoritePairs) {

                    items(uiState.pairs) { item ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "${item.targetCurrency}/${item.baseCurrency}",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text("${item.targetRate}")
                            }
                        }
                    }

                } else {

                    items(
                        items = uiState.currencyRates.rates.entries.toList(),
                        key = { it.key }
                    ) { entry ->

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (uiState.baseCurrency != entry.key) {
                                    Text(
                                        "${entry.key}/${uiState.baseCurrency}",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text("${entry.value}")
                                } else {
                                    Text("Buy/Sell", style = MaterialTheme.typography.titleSmall)
                                    Text("Price")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
