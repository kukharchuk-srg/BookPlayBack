package ua.kuk.book_playback.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.kuk.book_playback.Error
import ua.kuk.book_playback.PlaybackIntent
import ua.kuk.book_playback.PlaybackIntent.ChangeSpeed
import ua.kuk.book_playback.PlaybackIntent.Next
import ua.kuk.book_playback.PlaybackIntent.Pause
import ua.kuk.book_playback.PlaybackIntent.Play
import ua.kuk.book_playback.PlaybackIntent.Previous
import ua.kuk.book_playback.PlaybackIntent.SeekBackward
import ua.kuk.book_playback.PlaybackIntent.SeekForward
import ua.kuk.book_playback.PlaybackIntent.SeekTrack
import ua.kuk.book_playback.PlaybackScreenStatePreviewProvider
import ua.kuk.book_playback.PlaybackViewModel
import ua.kuk.book_playback.R
import ua.kuk.book_playback.ScreenState
import ua.kuk.book_playback.formatSpeed
import ua.kuk.book_playback.formatToTimeContent
import ua.kuk.data.model.BookAudioInfo
import ua.kuk.ui_kit.ui.theme.BookPlayBackTheme

@Composable
fun PlaybackScreen(
    modifier: Modifier = Modifier,
    book: BookAudioInfo,
    selectedKeyPointPosition: Int
) {
    PlaybackScreen(
        modifier = modifier,
        viewModel = koinViewModel(parameters = { parametersOf(book) }),
        selectedKeyPointPosition
    )
}

@Composable
internal fun PlaybackScreen(
    modifier: Modifier,
    viewModel: PlaybackViewModel,
    keyPointPosition: Int
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        viewModel.init(keyPointPosition)
        onDispose { viewModel.cancelPlaying() }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                Error -> Toast.makeText(
                    context, context.getString(R.string.message_error), Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    PlaybackContent(
        modifier = modifier,
        state = state,
        sendIntent = viewModel::sendIntent
    )
}

@Composable
fun PlaybackContent(modifier: Modifier, state: ScreenState, sendIntent: (PlaybackIntent) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        val bookLogo by rememberSaveable(state.bookLogo) { mutableStateOf(state.bookLogo) }
        val keyPointNumber by rememberSaveable(state.keyPointNumber) { mutableIntStateOf(state.keyPointNumber) }
        val totalKeyPoint by rememberSaveable(state.totalKeyPoint) { mutableIntStateOf(state.totalKeyPoint) }
        val keyPointTitle by rememberSaveable(state.keyPointTitle) { mutableStateOf(state.keyPointTitle) }
        val playbackPosition by rememberSaveable(state.playbackPosition) { mutableLongStateOf(state.playbackPosition) }
        val duration by rememberSaveable(state.duration) { mutableLongStateOf(state.duration) }
        val speed by rememberSaveable(state.speed) { mutableFloatStateOf(state.speed) }
        val isPlaying by rememberSaveable(state.isPlaying) { mutableStateOf(state.isPlaying) }

        var showSpeedDialog by rememberSaveable { mutableStateOf(false) }

        if (showSpeedDialog) {
            PlayBackSpeedDialog(speed) { speedValue ->
                sendIntent(ChangeSpeed(speedValue))
                showSpeedDialog = false
            }
        }

        BookLogo(bookLogo)

        val keyPointContent = stringResource(
            R.string.placeholder_keypoint, keyPointNumber, totalKeyPoint
        )
        Text(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = keyPointContent.uppercase(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = keyPointTitle,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        PlaybackPosition(playbackPosition, duration, sendIntent)

        Button(
            onClick = { showSpeedDialog = true },
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            val speedContent = stringResource(
                R.string.placeholder_title_speed_x, formatSpeed(speed)
            )
            Text(
                text = speedContent,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
        }

        PlaybackControl(keyPointNumber, sendIntent, isPlaying, totalKeyPoint)
        Spacer(modifier = Modifier.height(92.dp))
    }
}

@Composable
private fun PlaybackControl(
    keyPointNumber: Int,
    sendIntent: (PlaybackIntent) -> Unit,
    isPlaying: Boolean,
    totalKeyPoint: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        PreviousTrackIcon(keyPointNumber, sendIntent)
        Icon(
            modifier = Modifier
                .size(48.dp)
                .padding(8.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { sendIntent(SeekBackward) },
            painter = painterResource(id = R.drawable.ic_seek_backward),
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .padding(8.dp)
                .size(48.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    val intent = if (isPlaying) Pause else Play
                    sendIntent(intent)
                },
            painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .size(48.dp)
                .padding(8.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { sendIntent(SeekForward) },
            painter = painterResource(id = R.drawable.ic_seek_forward),
            contentDescription = null
        )

        NextTrackIcon(keyPointNumber != totalKeyPoint, sendIntent)
    }
}

@Composable
private fun PlaybackPosition(
    playbackPosition: Long,
    duration: Long,
    sendIntent: (PlaybackIntent) -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = playbackPosition.formatToTimeContent(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        PositionSlider(playbackPosition, duration, sendIntent)

        Text(
            text = duration.formatToTimeContent(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun BookLogo(bookLogo: Uri) {
    Box(
        modifier = Modifier
            .padding(horizontal = 80.dp)
            .aspectRatio(1.0f / 1.5f),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface) // Apply background to Box
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.surfaceContainer,
                    RoundedCornerShape(12.dp)
                ),
            contentScale = ContentScale.Fit,
            model = bookLogo,
            contentDescription = null,
        )
    }
}

@Composable
private fun RowScope.PositionSlider(
    playbackPosition: Long,
    duration: Long,
    sendIntent: (PlaybackIntent) -> Unit
) {
    val maxRange = if (duration > 0) duration.toFloat() else 0f
    Slider(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .weight(1f),
        value = playbackPosition.toFloat(),
        valueRange = 0.0f..maxRange,
        onValueChange = { sendIntent(SeekTrack(it.toLong())) },
        colors = SliderDefaults.colors().copy(
            inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainer,
            activeTrackColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun NextTrackIcon(enable: Boolean, sendIntent: (PlaybackIntent) -> Unit) {
    val tint = if (enable) MaterialTheme.colorScheme.onBackground
    else MaterialTheme.colorScheme.surfaceContainer
    Icon(
        modifier = Modifier
            .size(48.dp)
            .padding(8.dp)
            .clickable(
                enabled = enable,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { sendIntent(Next) },
        painter = painterResource(id = R.drawable.ic_next),
        contentDescription = null,
        tint = tint
    )
}

@Composable
private fun PreviousTrackIcon(
    keyPointNumber: Int,
    sendIntent: (PlaybackIntent) -> Unit
) {
    val tint = if (keyPointNumber == 1) MaterialTheme.colorScheme.surfaceContainer
    else MaterialTheme.colorScheme.onBackground

    Icon(
        modifier = Modifier
            .size(48.dp)
            .padding(8.dp)
            .clickable(
                enabled = keyPointNumber != 1,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { sendIntent(Previous) },
        painter = painterResource(id = R.drawable.ic_previous),
        tint = tint,
        contentDescription = null
    )
}

@Preview
@Composable
fun PlaybackContentPreview(
    @PreviewParameter(PlaybackScreenStatePreviewProvider::class, limit = 1) state: ScreenState
) {
    BookPlayBackTheme(false) {
        PlaybackContent(
            modifier = Modifier, state = state
        ) {}
    }
}

