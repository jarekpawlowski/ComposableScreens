package com.developerjp.composablescreens

import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.developerjp.composablescreens.navigation.MainScreen
import com.developerjp.composablescreens.navigation.Navigation
import com.developerjp.composablescreens.ui.theme.ComposableScreensTheme
import java.lang.Math.abs
import java.text.DecimalFormat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposableScreensTheme {


                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                    //Tutaj

                }
            }
        }
    }
}

@Composable
fun ImageButton(){
    Box(modifier = Modifier,
    ){
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.clickable {
            })
    }
}

@Composable
fun BottomAppBarWithFAB() {
    BottomAppBar(
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Check, contentDescription = "Localized description")
            }
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = {}){
                Icon(
                    Icons.Filled.Email,
                    contentDescription = "Email"
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        }
    )
}


@Composable
fun SwitchItem(name: String, modifier: Modifier = Modifier, bState: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(text = "$name",
            modifier = Modifier
                .weight(1f, true)
                .padding(8.dp)
                .align(Alignment.CenterVertically))

        Switch(checked = bState,
            onCheckedChange = null,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically),
            enabled = true,
            )

    }
}

private fun Float.toRad(): Float {
    return this * (Math.PI / 180f).toFloat()
}

@Composable
fun VolumeKnob(
    modifier: Modifier,
    numberOfCircles: Int = 40,
    minSize: Dp = 200.dp,
    onVolumeChanged: (Float) -> Unit = {}
) {

    var angle by rememberSaveable { mutableStateOf(0f) }
    var dragStartedAngle by rememberSaveable { mutableStateOf(0f) }
    var oldAngle by rememberSaveable { mutableStateOf(angle) }
    var volume by rememberSaveable { mutableStateOf(0f) }

    //represents the nob state ON or OFF
    var state by rememberSaveable { mutableStateOf(false) }
    val colourTransitionDurationMs = 1000

    val volumeFormatter = DecimalFormat("#.#")
    val ringColorOff = Color.LightGray
    val ringColorON = Color.Green
    val dotCircleNormalColor = Color(0xff00ff00)
    val dotCircleEmphasisedColor = Color(0xff9737bf)
    val offsetAngleDegree = 20f

    val ringColor by animateColorAsState(
        targetValue = if (state) ringColorON else ringColorOff,
        animationSpec = tween(
            durationMillis = colourTransitionDurationMs,
        )
    )

    BoxWithConstraints() {

        val width = if (minWidth < 1.dp)
            minSize else minWidth

        val height = if (minHeight < 1.dp)
            minSize else minHeight

        Canvas(
            modifier = modifier
                .size(width, height)
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStartedAngle = (atan2(
                                y = size.center.x - offset.x,
                                x = size.center.y - offset.y
                            ) * (180f / Math.PI.toFloat()) * -1).toFloat()
                        },
                        onDragEnd = {
                            oldAngle = angle
                        }
                    ) { change, _ ->

                        val touchAngle = atan2(
                            y = size.center.x - change.position.x,
                            x = size.center.y - change.position.y
                        ) * (180f / Math.PI.toFloat()) * -1

                        angle = oldAngle + (touchAngle - dragStartedAngle)

                        //we want to work with positive angles
                        if (angle > 360) {
                            angle -= 360
                        } else if (angle < 0) {
                            angle = 360 - abs(angle)
                        }

                        if (angle > 360f - (offsetAngleDegree * .8f))
                            angle = 0f
                        else if (angle > 0f && angle < offsetAngleDegree)
                            angle = offsetAngleDegree
                        //determinants the state of the nob. OFF or ON
                        state =
                            angle >= offsetAngleDegree && angle <= (360f - offsetAngleDegree / 2)

                        val newVolume = if (angle < offsetAngleDegree)
                            0f
                        else
                            (angle) / (360f - offsetAngleDegree)

                        volume = newVolume.coerceIn(
                            minimumValue = 0f,
                            maximumValue = 1f
                        )

                        onVolumeChanged(newVolume)
                    }
                }
        ) {
            //just calculating the radius so that the circle will fill the parent
            //also adding 25% padding of the radius as a padding to the parent..
            val radius = (size.width * .5f) - (.25f * (size.width * .5f))

            drawCircle(
                color = ringColor,
                style = Stroke(
                    width = (radius * .1f)
                ),
                radius = radius,
                center = size.center
            )

            // Represents the angle difference between each dot.
            // We are taking into consideration the offsetAngleDegree as wall.
            val lineDegree = (360f - offsetAngleDegree * 2) / numberOfCircles

            for (circleNumber in 0..numberOfCircles) {


                val angleInDegrees = lineDegree * circleNumber - 90f + offsetAngleDegree
                val angleRad = Math.toRadians(angleInDegrees.toDouble()).toFloat()

                val isDotEmphasised =
                    angle >= angleInDegrees + 90f && angle < (360f - offsetAngleDegree / 2)

                val normalDotRad = radius * .015f
                val emphasisedDotRad = normalDotRad + (circleNumber * (radius * .001f))

                val dotRadius = if (isDotEmphasised)
                    emphasisedDotRad
                else normalDotRad

                val dotColor = if (isDotEmphasised)
                    dotCircleEmphasisedColor
                else dotCircleNormalColor

                //basically we are shifting the dot in both X and Y directions by this amount
                val dotDistanceFromMainCircle = radius * .15f

                drawCircle(
                    center = Offset(
                        x = (radius + dotDistanceFromMainCircle) * cos(angleRad) + size.center.x,
                        y = ((radius + dotDistanceFromMainCircle) * sin(angleRad) + size.center.y).toFloat()
                    ),
                    color = dotColor,
                    radius = dotRadius
                )
            }

            val spacing = radius * .225f // ~22% of the radius

            val knobCenter = Offset(
                x = (radius - spacing) * cos((angle - 90f).toRad()) + size.center.x,
                y = (radius - spacing) * sin((angle - 90f).toRad()) + size.center.y
            )

            //the nob itself
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.Transparent, Color.Gray),
                    center = knobCenter,
                    radius = radius * (1f / 9f)
                ),
                radius = radius * (1f / 9f),
                center = knobCenter
            )

            //state text.
            drawContext.canvas.nativeCanvas.apply {
                val text = if (state) "ON" else "OFF"
                val paint = Paint()
                paint.textSize = radius * .15f
                val textRect = Rect()
                paint.getTextBounds(text, 0, text.length, textRect)

                val positionX = size.center.x - (textRect.width() / 2)
                val positionY = size.center.y - radius - (.15f * radius) + (textRect.height() / 2)

                drawText(
                    text,
                    positionX,
                    positionY,
                    paint
                )

                if (state) {
                    val volumeText = if (state) {
                        volumeFormatter.format((volume * 100))
                    } else "OFF"

                    val volumePaint = Paint()
                    volumePaint.color = android.graphics.Color.parseColor("#9737bf")
                    volumePaint.textSize = (radius / 2)
                    val volumeTextRect = Rect()
                    volumePaint.getTextBounds(volumeText, 0, volumeText.length, volumeTextRect)

                    val volumeTextPositionX = size.center.x - (volumeTextRect.width() / 2)
                    val volumeTextPositionY = size.center.y + (volumeTextRect.height() / 2)

                    drawText(
                        volumeText,
                        volumeTextPositionX,
                        volumeTextPositionY,
                        volumePaint
                    )
                }


            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposableScreensTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), Orientation.Vertical)){
        }
    }
}
