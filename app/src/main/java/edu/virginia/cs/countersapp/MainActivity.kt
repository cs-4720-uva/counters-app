package edu.virginia.cs.countersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.virginia.cs.countersapp.ui.theme.CountersAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val counters: List<CounterViewModel> = listOf(
            CounterViewModel(0),
            CounterViewModel(0),
            CounterViewModel(0)
        )

        setContent {
            CountersAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn {
                        items(counters) {counter ->
                            CounterCard(counterViewModel = counter)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CounterCard(counterViewModel: CounterViewModel, modifier: Modifier = Modifier) {
    var counter by remember{ mutableStateOf(counterViewModel) }
    Surface(modifier = modifier) {
        Column {
            Text(text = "${counter.currentValue}")
            Row {
                FilledTonalButton(onClick = { counter = counter.increment() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "increment",
                    )
                }
                FilledTonalButton(onClick = { counter = counter.decrement() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "decrement"
                    )
                }
                FilledTonalButton(onClick = { counter = counter.reset() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "refresh"
                    )
                }
                FilledTonalButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val counterViewModel = CounterViewModel()
    CountersAppTheme {
        CounterCard(counterViewModel = counterViewModel)
    }
}