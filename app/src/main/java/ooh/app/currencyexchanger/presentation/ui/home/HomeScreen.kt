package ooh.app.currencyexchanger.presentation.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import ooh.app.currencyexchanger.presentation.viewModal.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.weight(0.5f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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

            if(uiState.showButtonSave){
                Button(onClick = {
                    viewModel.savePair()
                    Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Save Pair")
                }
            }
        }

        Column(modifier = Modifier.weight(0.5f)) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .background(if(!uiState.displayFavoritePairs) Color.Cyan else Color.Transparent)
                        .padding(8.dp)
                        .clickable(
                            onClick = viewModel::displayPairs
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Currency Rates")

                    Icon(
                        Icons.Outlined.StackedBarChart,
                        contentDescription = "Rate"
                    )
                }
                if(!uiState.pairs.isEmpty()){
                    Row(
                        modifier = Modifier
                            .background(if(uiState.displayFavoritePairs) Color.Cyan else Color.Transparent)
                            .padding(8.dp)
                            .clickable(
                                onClick = viewModel::displayFavoritePairs
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Favorite Rates")

                        Icon(
                            Icons.Outlined.Star,
                            contentDescription = "Save"
                        )
                    }
                }

            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                if (uiState.displayFavoritePairs) {
                    items(items = uiState.pairs)
                    { item ->
                        Column {
                            Text(
                                "${item.targetCurrency}/${item.baseCurrency}",
                                modifier = Modifier.background(
                                    color = Color.Green, shape = RoundedCornerShape(8.dp)
                                )
                            )
                            Text("${item.targetRate}")
                        }

                    }
                } else {
                    items(
                        items = uiState.currencyRates.rates.entries.toList(),
                        key = { it.key })
                    { entry ->
                        if (uiState.baseCurrency != entry.key) {
                            Column {
                                Text(
                                    "${entry.key}/${uiState.baseCurrency}",
                                    modifier = Modifier.background(
                                        color = Color.Cyan, shape = RoundedCornerShape(8.dp)
                                    )
                                )
                                Text("${entry.value}")
                            }
                        } else {
                            Column {
                                Text(
                                    "Buy/Sell",
                                    modifier = Modifier.background(
                                        color = Color.Green, shape = RoundedCornerShape(8.dp)
                                    )
                                )
                                Text("Price")
                            }
                        }
                    }
                }
            }
        }
    }
}