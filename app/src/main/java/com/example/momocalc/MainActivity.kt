package com.example.momocalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.momocalc.ui.theme.MomoCalcTheme

/**
 * This is my main starting point of my app.
 * Think of it like my front door that opens when you tap my app icon on my phone.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This part tells my app to start drawing our user interface
        setContent {
            // We apply our custom 'look and feel' (colors and fonts) here
            MomoCalcTheme {
                // Surface is like a clean sheet of paper that fills my whole screen
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // called my main calculator screen here
                    MoMoCalcScreen()
                }
            }
        }
    }
}

/**
 * This is my main design of our calculator screen.
 * It puts together all my buttons, text boxes, and math logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoMoCalcScreen() {
    // --- STEP 1: INITIALIZING MY VARIABLES ---

    // We use "amountInput" to remember exactly what my user is typing in my box.
    var amountInput by remember { mutableStateOf("") }

    // We try to turn my text into a real number we can do math with.
    val numericAmount = amountInput.toDoubleOrNull()

    // If my user typed something that isn't a number, we'll show an error.
    val isError = amountInput.isNotEmpty() && numericAmount == null

    // --- STEP 2: DOING MY MATH ---

    // We calculate a 3% fee. If there's no number, we just use 0.
    val fee = (numericAmount ?: 0.0) * 0.03

    // We add my fee to my original amount to get my total.
    val total = (numericAmount ?: 0.0) + fee

    // We turn those numbers into pretty text with currency signs (like UGX 1,000).
    val formattedFee = "UGX %,.0f".format(fee)
    val formattedTotal = "UGX %,.0f".format(total)

    // --- STEP 3: BUILDING MY LOOK ---

    // Scaffold is a basic layout structure. It's like my skeleton of my screen.
    Scaffold(
        // This is my header bar at my very top of my screen.
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                // We give my top bar a nice background color from our theme.
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        // Column stacks things vertically (one on top of my other).
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_screen)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- MY INPUT SECTION ---

            // We put my text box inside a Card to make it look like it's popping off my screen.
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = dimensionResource(id = R.dimen.card_elevation)
                ),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius))
            ) {
                Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))) {
                    // This is my actual box where you type my numbers.
                    OutlinedTextField(
                        value = amountInput,
                        // This updates our memory whenever my user types or deletes a character.
                        onValueChange = {
                            if (it.all { char -> char.isDigit() || char == '.' }) amountInput = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.enter_amount)) },
                        placeholder = { Text("e.g. 5000") },
                        // A small icon to make it look nicer.
                        leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
                        isError = isError,
                        // This makes sure my number keypad pops up on my phone.
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.text_field_corner_radius))
                    )

                    // If there's a mistake, we show a red warning message.
                    if (isError) {
                        Text(
                            text = stringResource(R.string.error_numbers_only),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(
                                start = dimensionResource(id = R.dimen.padding_medium),
                                top = dimensionResource(id = R.dimen.padding_small)
                            )
                        )
                    }
                }
            }

            // A bit of empty space between my box and my results.
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium)))

            // --- MY RESULTS SECTION ---

            // We only show my results card if my user has typed a valid number.
            if (numericAmount != null && numericAmount > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius))
                ) {
                    Column(
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_screen)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Calculation Result",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_small)))

                        // Show my fee.
                        ResultRow(label = "Transfer Fee (3%)", value = formattedFee)

                        // A simple line to separate my parts.
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                vertical = dimensionResource(id = R.dimen.padding_vertical_divider)
                            ),
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f)
                        )

                        // Show my final total in a bigger, bolder font.
                        ResultRow(
                            label = "Total to Deduct",
                            value = formattedTotal,
                            isTotal = true
                        )
                    }
                }
            } else {
                // --- MY EMPTY STATE ---

                // If nothing is entered, we show a friendly hint.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = R.dimen.padding_large)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(dimensionResource(id = R.dimen.icon_size_large)),
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_extra_small)))
                        Text(
                            text = "Enter an amount above to see my fee breakdown",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

/**
 * A small helper part that shows a label (like "Fee") and its price side-by-side.
 */
@Composable
fun ResultRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isTotal)
                MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal)
                FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (isTotal)
                MaterialTheme.typography.headlineSmall else
                MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isTotal)
                MaterialTheme.colorScheme.primary else
                MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

/**
 * This is for my developer!
 * it lets us see how my app looks right here in my editor without a real phone.
 */
@Preview(showBackground = true)
@Composable
fun MoMoCalcPreview() {
    MomoCalcTheme {
        MoMoCalcScreen()
    }
}
