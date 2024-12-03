package ua.kuk.book_playback.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.kuk.book_playback.R
import ua.kuk.book_playback.formatSpeed
import ua.kuk.ui_kit.ui.theme.BookPlayBackTheme

private const val MIN_SPEED = 0.5f
private const val MAX_SPEED = 2f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayBackSpeedDialog(
    speed: Float,
    changeSpeed: (Float) -> Unit
) {
    ModalBottomSheet(
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        onDismissRequest = { changeSpeed(speed) },
        dragHandle = null,
    ) {
        PlayBackSpeedDialogContent(speed, changeSpeed)
    }
}

@Composable
private fun PlayBackSpeedDialogContent(
    speed: Float,
    changeSpeed: (Float) -> Unit
) {
    var speedState by rememberSaveable { mutableFloatStateOf(speed) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.label_playback_speed),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        SpeedControlRow(speedState) { speedState = it }

        Slider(
            value = speedState,
            valueRange = MIN_SPEED..MAX_SPEED,
            onValueChange = { speedState = it.formatSpeed(0f) },
            steps = 16,
            colors = SliderDefaults.colors().copy(
                activeTrackColor = MaterialTheme.colorScheme.surfaceContainer,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainer,
                activeTickColor = MaterialTheme.colorScheme.onSurface,
                disabledInactiveTickColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            SpeedConst.entries.forEach { btn ->
                val title = stringResource(id = btn.titleResId)
                ConstSpeedBtn(title) {
                    speedState = btn.speed
                }

            }
        }

        Button(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(50.dp),
            onClick = { changeSpeed(speedState) },
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.label_continue),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun SpeedControlRow(speedState: Float, changeSpeed: (Float) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Box(modifier =Modifier.minimumInteractiveComponentSize()) {
            if (speedState != MIN_SPEED) FilledIconButton(
                onClick = {
                    val speedValue = speedState.formatSpeed(-0.1f)
                    changeSpeed(speedValue)
                },
                colors = IconButtonDefaults.filledIconButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_minus),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Text(
            modifier = Modifier.width(80.dp),
            text = stringResource(R.string.placeholder_speed_x, speedState),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Box(modifier = Modifier.minimumInteractiveComponentSize()) {
            if (speedState != MAX_SPEED) FilledIconButton(
                onClick = {
                    val speedValue = speedState.formatSpeed(0.1f)
                    changeSpeed(speedValue)
                },
                modifier = Modifier.minimumInteractiveComponentSize(),
                colors = IconButtonDefaults.filledIconButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_plus),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun RowScope.ConstSpeedBtn(title: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.weight(1f),
        onClick = { onClick() },
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors()
            .copy(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ModalPhotoPickerPreview() {
    BookPlayBackTheme {
        PlayBackSpeedDialogContent(1f) {}
    }
}

