package com.example.project_ten

import android.content.Context.SENSOR_SERVICE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalDensity
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset


class SensorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SensorActivityContent()
        }
    }
}

@Composable
fun SensorActivityContent() {
    var temperature by remember { mutableStateOf("") }
    var airPressure by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val sensorManager = LocalContext.current.getSystemService(SENSOR_SERVICE) as SensorManager

    // Choose the sensor you want to use
    val temperatureSensorType = Sensor.TYPE_AMBIENT_TEMPERATURE
    val temperatureSensor = sensorManager.getDefaultSensor(temperatureSensorType)

    val pressureSensorType = Sensor.TYPE_PRESSURE
    val pressureSensor = sensorManager.getDefaultSensor(pressureSensorType)

    var temperatureSensorEventListener: SensorEventListener? = null
    var pressureSensorEventListener: SensorEventListener? = null

    DisposableEffect(Unit) {
        // Temperature Sensor
        temperatureSensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handling accuracy changes if needed

            }

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    temperature = it.values[0].toString()
                }
            }
        }

        // Pressure Sensor
        pressureSensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle accuracy changes if needed
            }

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    airPressure = it.values[0].toString()
                }
            }
        }

        // Register listeners
        sensorManager.registerListener(
            temperatureSensorEventListener,
            temperatureSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            pressureSensorEventListener,
            pressureSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            // Unregister listeners
            temperatureSensorEventListener?.let {
                sensorManager.unregisterListener(it)
            }

            pressureSensorEventListener?.let {
                sensorManager.unregisterListener(it)
            }
        }
    }

    LaunchedEffect(Unit) {
        // Get location using Geocoder
        val geocoder = Geocoder(LocalContext.current)
        val addresses: List<Address>? = withContext(Dispatchers.IO) {
            geocoder.getFromLocation(39.1651903,-86.5257804017032,1)
        }

        addresses?.let {
            if (it.isNotEmpty()) {
                val address = it[0]
                location = "${address.locality}, ${address.adminArea}"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Your Name: Your Name Here")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Your Location: $location")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Current Temperature: $temperature")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Air Pressure: $airPressure")
        Spacer(modifier = Modifier.height(16.dp))

        // Add a button to navigate to Gesture Activity on fling
        var offsetX by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectTransformGestures { _, panGesture ->
                        offsetX += panGesture.offset.x

                        if (panGesture.velocity.x > 8000) {
                            nav controller . navigate ("gesture")

                        }
                    }
                }
        ) {
            Button(onClick = { /* Handling button click */ }) {
                Text("Gesture Playground")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SensorActivityPreview() {
    Project_tenTheme {
        SensorActivityContent()
    }
}