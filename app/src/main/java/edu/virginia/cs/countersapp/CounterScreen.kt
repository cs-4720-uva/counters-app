package edu.virginia.cs.countersapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.virginia.cs.countersapp.ui.theme.CountersAppTheme

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    counterViewModel: CounterViewModel = viewModel<CounterViewModel>()
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = modifier
        ) {
            AddCounterRow(modifier, counterViewModel)
            Divider(thickness = 1.dp)
            CountersColumn(modifier, counterViewModel)
        }
    }
}

@Composable
private fun AddCounterRow(
    modifier: Modifier,
    counterViewModel: CounterViewModel
) {
    Column {
        Text("Add new counter...",
            fontSize = 30.sp)
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var textEntry by remember { mutableStateOf("") }

            OutlinedTextField(
                value = textEntry,
                onValueChange = { newEntry -> textEntry = newEntry },
                label = { Text("New counter") }
            )
            IconButton(
                onClick = {  counterViewModel.addByName(textEntry) },
                icon = Icons.Default.Add,
                contentDescription = "Add new counter"
            )
        }
    }
}

@Composable
private fun CountersColumn(
    modifier: Modifier,
    counterViewModel: CounterViewModel
) {

    val counters = counterViewModel.countersList
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = counters) { counter->
            CounterCard(modifier, counter, counterViewModel)
            Divider(
                thickness = 2.dp,
                modifier = modifier.padding(5.dp)
            )
        }
    }
}

@Composable
private fun CounterCard(
    modifier: Modifier,
    counter: Counter,
    counterViewModel: CounterViewModel
) {

    Surface(modifier = modifier) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            CounterCardLabelRow(
                modifier,
                counter,
                counterViewModel)
            CounterCardButtonRow(
                modifier = modifier,
                counter = counter,
                counterViewModel = counterViewModel)
        }
    }
}

@Composable
private fun CounterCardLabelRow(
    modifier: Modifier = Modifier,
    counter: Counter,
    counterViewModel: CounterViewModel,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "${counter.name}",
            fontSize = 30.sp,
            modifier = modifier.weight(0.5F)
        )
        Text(
            text = "${counter.value}",
            fontSize = 30.sp,
            modifier = modifier.weight(0.5F)
        )
    }
}

@Composable
private fun CounterCardButtonRow(
    modifier: Modifier = Modifier,
    counter: Counter,
    counterViewModel: CounterViewModel
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            modifier = modifier,
            onClick = { counterViewModel.increment(counter) },
            icon = Icons.Default.KeyboardArrowUp,
            contentDescription = "increment"
        )
        IconButton(
            modifier = modifier,
            onClick = { counterViewModel.decrement(counter) },
            icon = Icons.Default.KeyboardArrowDown,
            contentDescription = "decrement"
        )
        IconButton(
            modifier = modifier,
            onClick = { counterViewModel.reset(counter) },
            icon = Icons.Default.Refresh,
            contentDescription = "reset"
        )
        IconButton(
            modifier = modifier,
            onClick = { counterViewModel.remove(counter) },
            icon = Icons.Default.Delete,
            contentDescription = "delete"
        )
    }
}

@Composable
private fun IconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.height(60.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CounterScreenPreview() {
    val counterViewModel = Counter( "Push-Ups")
    CountersAppTheme {
        CounterScreen(counterViewModel = viewModel())
    }
}