package com.example.project_ten

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.Context.SENSOR_SERVICE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.project_ten.ui.theme.Project_tenTheme
import android.location.Geocoder
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Address
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController

class SensorClone : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project_tenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SensorCloneContent()
                }
            }
        }
    }
}

@Composable
fun SensorCloneContent() {
    var sensorData by remember { mutableStateOf("") }

    // Add ConfigurationListener to observe configuration changes
    ConfigurationListener {
        // Re-compose the content on configuration changes
        SensorCloneContent()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top fragment with a red ball using sensor (e.g., accelerometer)
        SensorTopFragment(
            onSensorDataChanged = { data ->
                sensorData = data
            },
            modifier = Modifier.weight(1f)
        )

        // Bottom fragment with sensor data log
        Spacer(modifier = Modifier.height(16.dp))
        BottomFragment(gestureLog = sensorData, modifier = Modifier.weight(1f))
    }
}

@Composable
fun SensorTopFragment(onSensorDataChanged: (String) -> Unit, modifier: Modifier) {
    var position by remember { mutableStateOf(IntOffset.Zero) }

    // Replace detectTransformGestures with sensor logic (e.g., accelerometer)
    // You would need to use SensorManager and SensorEventListener for accelerometer data

    val sensorManager = LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var accelerometerEventListener: SensorEventListener? = null

    DisposableEffect(Unit) {
        accelerometerEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle accuracy changes if needed
            }

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    // Process accelerometer data and update position
                    // Example: Update the X and Y position based on accelerometer values
                    val x = event.values[0]
                    val y = event.values[1]
                    position = IntOffset(x.roundToInt(), y.roundToInt())

                    // Notify the data change to the composable
                    onSensorDataChanged("Accelerometer: X=$x, Y=$y")
                }
            }
        }

        sensorManager.registerListener(
            accelerometerEventListener,
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager.unregisterListener(accelerometerEventListener)
        }
    }

    // Rest of the TopFragment remains the same...
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                // Reset position when the box is repositioned
                position = IntOffset.Zero
            }
            .then(modifier)
    ) {
        Ball(position = position)
    }
}