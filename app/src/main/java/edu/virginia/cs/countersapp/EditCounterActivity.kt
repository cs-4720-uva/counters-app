package edu.virginia.cs.countersapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.virginia.cs.countersapp.ui.theme.CountersAppTheme

class EditCounterActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val counter = intent.getSerializableExtra(
            "counter", Counter::class.java)!!

        setContent {
            CountersAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EditScreen(
                        counter = counter,
                        onSave = { newName ->
                            val resultIntent = Intent()
                                .putExtra("newName", newName)
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        },
                        onFindTime = {
                            val calendarIntent = Intent(Intent.ACTION_MAIN)
                            calendarIntent.setPackage("com.google.android.calendar")
                            startActivity(calendarIntent)
                        },
                        onCountingHelp = {
                            val youtubeAppIntent = Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse("vnd.youtube:HH5o4avA8aY"))
                            val webIntent= Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse("https://www.youtube.com/watch?v=HH5o4avA8aY"))
                            try {
                                startActivity(youtubeAppIntent)
                            } catch (e: ActivityNotFoundException) {
                                startActivity(webIntent)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EditScreen(
    counter: Counter,
    onSave: (String) -> Unit,
    onFindTime: () -> Unit,
    onCountingHelp: () -> Unit
) {
    Column() {
        val originalName = counter.name
        var textValue by remember { mutableStateOf(counter.name) }
        var valueChanged by remember { mutableStateOf(false) }
        Text(
            text = "Rename Counter",
            fontSize = 40.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Divider(thickness = 1.dp)
        Text(
            text = "Original Name:\n $originalName",
            fontSize = 30.sp
        )
        OutlinedTextField(
            value = textValue,
            singleLine = true,
            onValueChange = {
                textValue = it
                valueChanged = true
            },
            label = { Text("Counter name") }
        )
        FilledTonalButton(
            onClick = {
                onSave(textValue)
            }
        ) {
            Text(text = "Save")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = onFindTime)
        {
            Text("Find Time To Count")
        }
        Spacer(modifier = Modifier.height(1.dp))
        Button(onClick = onCountingHelp)
        {
            Text("Counting Help")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CountersAppTheme {
        EditScreen(
            Counter("Preview Count"),
            { },
            { },
            { }
        )
    }
}