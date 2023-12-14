package com.example.project_ten

import android.graphics.PointF
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.example.project_ten.ui.theme.Project_tenTheme
import kotlin.math.roundToInt


class GestureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project_tenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GestureActivityContent()
                }
            }
        }
    }
}

@Composable
fun GestureActivityContent() {
    var gestureLog by remember { mutableStateOf("") }

    // Add ConfigurationListener to observe configuration changes
    ConfigurationListener {
        // Re-compose the content on configuration changes
        GestureActivityContent()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top fragment with a red ball
        TopFragment(
            onGesturePerformed = { gesture ->
                gestureLog += "$gesture\n"
            },
            modifier = Modifier.weight(1f)
        )

        // Bottom fragment with gesture log
        Spacer(modifier = Modifier.height(16.dp))
        BottomFragment(gestureLog = gestureLog, modifier = Modifier.weight(1f))
    }
}

@Composable
fun ConfigurationListener(onConfigurationChange: () -> Unit) {
    val configuration = LocalConfiguration.current

    DisposableEffect(configuration) {
        onDispose { /* clean up if needed */ }
    }

    LaunchedEffect(configuration) {
        onConfigurationChange()
    }
}

@Composable
fun Ball(position: IntOffset, modifier: Modifier = Modifier) {
    val size = 64.dp
    Box(
        modifier = modifier
            .size(size)
            .offset { position }
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun TopFragment(onGesturePerformed: (String) -> Unit, modifier: Modifier) {
    var position by remember { mutableStateOf(IntOffset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    position += pan.round()
                    onGesturePerformed("Pan: $pan")
                }
            }
            .onGloballyPositioned { coordinates ->
                // Reset position when the box is repositioned
                position = IntOffset.Zero
            }
            // Apply the provided modifier
            .then(modifier)
    ) {
        Ball(position = position)
    }
}


@Composable
fun BottomFragment(gestureLog: String, modifier: Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
            // Apply the provided modifier
            .then(modifier)
    ) {
        // Display gesture log
        Text(text = "Gesture Log:")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = gestureLog)
    }
}

@Preview(showBackground = true)
@Composable
fun GestureActivityPreview() {
    Project_tenTheme {
        GestureActivityContent()
    }
}