package edu.virginia.cs.countersapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.virginia.cs.countersapp.ui.theme.CountersAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val Context.dataStore by dataStore("app_settings.json", AppSettingsSerializer)


class MainActivity : ComponentActivity() {

    val database by lazy {
        CounterDatabase.getDatabase(applicationContext)
    }

    private val airplaneModeReceiver = AirplaneModeReceiver()
    private val testActionReceiver = TestActionReceiver()

    private lateinit var appSettings: AppSettings
    private lateinit var coroutineScope: CoroutineScope

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(
            airplaneModeReceiver,
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )

        registerReceiver(
            testActionReceiver,
            IntentFilter("TEST_ACTION"),
            RECEIVER_EXPORTED
        )

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            0
        )

        setContent {
            appSettings = dataStore.data.collectAsState(
                initial = AppSettings.getDefaultSettings()
            ).value

            coroutineScope = rememberCoroutineScope()

            val counterViewModel = counterViewModel()
            val isDarkTheme = appSettings.isDarkTheme ?: isSystemInDarkTheme()
            CountersAppTheme(
                darkTheme = isDarkTheme
            ) {
                // A surface container using the 'background' color from the theme
                CounterScreen(
                    counterViewModel = counterViewModel,
                    darkTheme = isDarkTheme
                )
            }
        }
    }

    @Composable
    private fun counterViewModel(): CounterViewModel {
        val counterViewModel = viewModel<CounterViewModel> (
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("Unchecked")
                    return CounterViewModel(
                        CounterRepository(database.counterDao)
                    ) as T
                }
            }
        )
        return counterViewModel
    }

    private suspend fun setDarkTheme(isDarkTheme: Boolean) {
        dataStore.updateData { settings ->
            settings.copy(
                isDarkTheme = isDarkTheme
            )
        }
    }

    private suspend fun updateLastCounterModified(lastCounterModified: Counter) {
        dataStore.updateData { settings ->
            settings.copy(
                lastCounterModified = lastCounterModified
            )
        }
    }

    @Composable
    fun CounterScreen(
        modifier: Modifier = Modifier,
        counterViewModel: CounterViewModel,
        darkTheme: Boolean
    ) {
        var isDarkTheme = remember{darkTheme}
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = modifier
            ) {
                Button(onClick = {
                    isDarkTheme = !isDarkTheme
                    coroutineScope.launch {
                        setDarkTheme(isDarkTheme)
                    }
                }) {
                    Text("Toggle Dark Mode")
                }
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
                    singleLine = true,
                    onValueChange = { newEntry -> textEntry = newEntry },
                    label = { Text("New counter") }
                )
                CounterButton(
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

        val counters = remember{ counterViewModel.countersList }
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
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.i("DEBUG: ", "$result")
            if (result.resultCode == RESULT_OK) {
                val newName = result.data!!.getStringExtra("newName")!!
                counterViewModel.rename(counter, newName)
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = if (appSettings.lastCounterModified == counter)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.background
                )
        ) {
            Text(
                text = counter.name,
                fontSize = 30.sp,
                modifier = modifier.weight(0.45F)
            )
            Text(
                text = "${counter.value}",
                fontSize = 30.sp,
                modifier = modifier.weight(0.40F)
            )
            CounterButton(
                onClick = {
                    val intent = Intent(context, EditCounterActivity::class.java)
                    intent.putExtra("counter", counter)
                    launcher.launch(intent) },
                icon = Icons.Default.Edit,
                contentDescription = "edit name",
                modifier = modifier.weight(0.15F))
        }
    }

    @Composable
    private fun CounterCardButtonRow(
        modifier: Modifier = Modifier,
        counter: Counter,
        counterViewModel: CounterViewModel
    ) {
        val isDecrementPossible = counter.value > 0
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CounterButton(
                modifier = modifier,
                onClick = {
                    coroutineScope.launch {
                        updateLastCounterModified(counter)
                    }
                    counterViewModel.increment(counter)
                },
                icon = Icons.Default.KeyboardArrowUp,
                contentDescription = "increment"
            )
            CounterButton(
                modifier = modifier,
                onClick = {
                    coroutineScope.launch {
                        updateLastCounterModified(counter)
                    }
                    counterViewModel.decrement(counter)
                },
                icon = Icons.Default.KeyboardArrowDown,
                contentDescription = "decrement",
                clickable = isDecrementPossible
            )
            CounterButton(
                modifier = modifier,
                onClick = {
                    coroutineScope.launch {
                        updateLastCounterModified(counter)
                    }
                    counterViewModel.reset(counter)
                },
                icon = Icons.Default.Refresh,
                contentDescription = "reset"
            )
            CounterButton(
                modifier = modifier,
                onClick = { counterViewModel.remove(counter) },
                icon = Icons.Default.Delete,
                contentDescription = "delete"
            )
        }
    }

    @Composable
    private fun CounterButton(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        icon: ImageVector,
        contentDescription: String,
        clickable: Boolean = true
    ) {
        FilledTonalButton(
            onClick = onClick,
            enabled = clickable,
            modifier = modifier
                .height(60.dp)
                .then(
                    if (clickable) Modifier else Modifier.alpha(0.5F)
                )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airplaneModeReceiver)
        unregisterReceiver(testActionReceiver)
    }
}