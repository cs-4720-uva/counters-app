package edu.virginia.cs.countersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.virginia.cs.countersapp.ui.theme.CountersAppTheme


class MainActivity : ComponentActivity() {

    val database by lazy {
        CounterDatabase.getDatabase(applicationContext)
    }

    @Composable
    private fun counterViewModel(): CounterViewModel {
        val counterViewModel = viewModel<CounterViewModel>(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val counterViewModel = counterViewModel()
            CountersAppTheme {
                // A surface container using the 'background' color from the theme
                CounterScreen(counterViewModel = counterViewModel)
            }
        }
    }
}