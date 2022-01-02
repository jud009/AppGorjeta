package com.estud.tipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estud.tipapp.components.InputField
import com.estud.tipapp.ui.theme.TipAppTheme
import com.estud.tipapp.widgets.RoundIconButton

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartApp {
                MainContent()
            }
        }
    }
}

@Composable
fun StartApp(content: @Composable () -> Unit) {
    //container function
    TipAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun MainContent() {
    Column(
        modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val totalPerPerson = remember {
            mutableStateOf(0.0)
        }

        val totalValue = remember {
            mutableStateOf(0.0)
        }

        CardHeader(totalPerPerson = totalPerPerson.value)

        Spacer(modifier = Modifier.height(8.dp))

        TipCalculator(onValueChanged = {
            try {
                totalPerPerson.value = it.toDouble()
                totalValue.value = it.toDouble()
            } catch (e: NumberFormatException) {
                Log.i("TAG_value", "number format error")
            }
        }, totalPersonChanged = { total ->
            val newValue = totalValue.value / total
            totalPerPerson.value = newValue
        }, percentageChanged = { tipPercentage ->
            val newValue = totalValue.value + (totalValue.value * tipPercentage)
            Log.i("TAG_value", "new value: $newValue")
            totalPerPerson.value = newValue
        })
    }

}

@ExperimentalComposeUiApi
@Composable
fun TipCalculator(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
    totalPersonChanged: (Int) -> Unit,
    percentageChanged: (Double) -> Unit
) {
    val billState = remember {
        mutableStateOf("")
    }

    val validState = remember(billState.value) {
        billState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        border = BorderStroke(width = 2.dp, color = Color.Gray),
        shape = RoundedCornerShape(corner = CornerSize(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputField(
                valueState = billState,
                hint = "Digite um valor",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions

                    keyboardController?.hide() //when I click in the keyboard action it goes away
                })

            if (validState) {
                onValueChanged(billState.value.trim())

                val sliderPercentage = remember {
                    mutableStateOf(0f)
                }

                DisplaySplitAmount {
                    totalPersonChanged(it)
                }

                DisplayTipAmount()

                DisplaySliderTipAndTextPercent(sliderPercentage.value) { newValue ->
                    sliderPercentage.value = newValue
                    percentageChanged(newValue.toDouble())
                }

            } else {
                Box {}
            }
        }
    }
}

@Composable
fun DisplayTipAmount() {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Gorjeta"
        )

        Spacer(modifier = Modifier.width(165.dp))

        Text(
            text = "R$45.00"
        )
    }
}

@Composable
fun DisplaySliderTipAndTextPercent(percentage: Float, sliderChanged: (Float) -> Unit) {

    val formatPercentage = "%.1f".format(percentage * 100)

    Text(modifier = Modifier.padding(16.dp), text = "$formatPercentage%")

    Slider(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth(), value = percentage, onValueChange = { newValue ->
            sliderChanged(newValue)
        }, steps = 5
    )
}

@Composable
fun DisplaySplitAmount(currentValue: (Int) -> Unit) {

    val splitValue = remember {
        mutableStateOf(1)
    }

    Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.Start) {
        Text(
            text = "Dividir",
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(100.dp))

        Row(
            modifier = Modifier.padding(horizontal = 3.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            RoundIconButton(imageVector = Icons.Default.Remove) {
                if (splitValue.value > 1) {
                    splitValue.value -= 1
                    currentValue(splitValue.value)
                }
            }

            Text(
                modifier = Modifier.padding(8.dp),
                text = splitValue.value.toString()
            )

            RoundIconButton(imageVector = Icons.Default.Add) {
                splitValue.value += 1
                currentValue(splitValue.value)
            }
        }
    }
}

@Composable
fun CardHeader(totalPerPerson: Double = 0.0) {
    //surface is like a layout (linear, constraint and etc)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(16.dp))),
        //.clip(shape = RoundedCornerShape(corner = CornerSize(16.dp))),
        color = Color(0xFFBAABDA)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Total por pessoa",
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(8.dp))

            val total = "%.2f".format(totalPerPerson)

            Text(
                text = "R$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StartApp {
        CardHeader(130.0)
    }
}