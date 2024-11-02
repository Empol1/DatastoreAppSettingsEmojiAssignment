package edu.farmingdale.datastoredemo.ui

import android.widget.Toast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import android.os.Handler
import android.os.Looper
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.farmingdale.datastoredemo.R

import edu.farmingdale.datastoredemo.data.local.LocalEmojiData

/*
 * Screen level composable
 */
@Composable
fun EmojiReleaseApp(
    emojiViewModel: EmojiScreenViewModel = viewModel(
        factory = EmojiScreenViewModel.Factory
    )
) {
    EmojiScreen(                // display EmojiScreen with UI state
        uiState = emojiViewModel.uiState.collectAsState().value,
        selectLayout = emojiViewModel::selectLayout,
        toggleTheme = emojiViewModel::toggleTheme
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable         // composable function for EmojiScreen UI
private fun EmojiScreen(
    uiState: EmojiReleaseUiState,
    selectLayout: (Boolean) -> Unit,
    toggleTheme: (Boolean) -> Unit
) {
    val isLinearLayout = uiState.isLinearLayout
    val isDarkTheme = uiState.isDarkTheme

    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val topBarColor = if (isDarkTheme) Color(0xFF6200EE) else Color(0xFFD1C4E9)    //colors
    val emojiBoxColor = if (isDarkTheme) Color(0xFFD1C4E9) else Color(0xFF6200EE)
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BCS371 Emoji", color = textColor) },
                actions = {
                    IconButton(onClick = { selectLayout(!isLinearLayout) }) {   //change icons layout
                        Icon(
                            painter = painterResource(uiState.toggleIcon),
                            contentDescription = stringResource(uiState.toggleContentDescription),
                            tint = textColor
                        )
                    }
                    Switch(
                        checked = isDarkTheme,   //switch for theme change
                        onCheckedChange = { toggleTheme(!isDarkTheme) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.Black
                        )
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = topBarColor
                )
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        val modifier = Modifier
            .padding(
                top = dimensionResource(R.dimen.padding_medium),
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium),
            )
        if (isLinearLayout) {
            EmojiReleaseLinearLayout(
                modifier = modifier.fillMaxWidth(),
                contentPadding = innerPadding,
                emojiBoxColor = emojiBoxColor
            )
        } else {
            EmojiReleaseGridLayout(
                modifier = modifier,
                contentPadding = innerPadding,
                emojiBoxColor = emojiBoxColor
            )
        }
    }
}
@Composable
fun EmojiReleaseLinearLayout(        // composable for displaying linear layout
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    emojiBoxColor: Color
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
    ) {
        items(
            items = LocalEmojiData.EmojiList,
            key = { e -> e }
        ) { e ->
            val emojiDescription = emojiDescriptions[e] ?: "Unknown Emoji" //if emoji doesn't have valid set name
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = emojiBoxColor
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = e, fontSize = 50.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .clickable {
                            val toast = Toast.makeText(context, "$emojiDescription", Toast.LENGTH_SHORT)
                            toast.show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                toast.cancel()
                            }, 1500)
                        },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable         // composable for displaying grid layout
fun EmojiReleaseGridLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    emojiBoxColor: Color
) {
    val context = LocalContext.current
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        items(
            items = LocalEmojiData.EmojiList,
            key = { e -> e }
        ) { e ->
            val emojiDescription = emojiDescriptions[e] ?: "Unknown Emoji"
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = emojiBoxColor
                ),
                modifier = Modifier.height(110.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = e, fontSize = 50.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)
                        .padding(dimensionResource(R.dimen.padding_small))
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            val toast = Toast.makeText(context, "$emojiDescription", Toast.LENGTH_SHORT) //emoji toast
                            toast.show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                toast.cancel()
                            }, 1500)
                        },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

val emojiDescriptions = mapOf(   //name of all emojis in the toasts
    "😀" to "Smiling Face",
    "😃" to "Smiling Face with Big Eyes",
    "😄" to "Smiling Face with Closed Eyes",
    "😁" to "Smiling Face with Teeth",
    "😆" to "Excited Face",
    "😅" to "Grinning Face with Sweat",
    "😂" to "Face with Tears of Joy",
    "🤣" to "Rolling on the Floor Laughing",
    "😊" to "Smiling Face with Closed Eyes",
    "😇" to "Smiling Face with Halo",
    "🙂" to "Slightly Smiling Face",
    "🙃" to "Upside-Down Face",
    "😉" to "Winking Face",
    "😌" to "Relieved Face",
    "😍" to "Heart Eyes Face",
    "🥰" to "Smiling Face with Hearts",
    "😘" to "Face Blowing a Kiss",
    "😗" to "Kissing Face",
    "😙" to "Kissing Face with Smiling Eyes",
    "😚" to "Kissing Face with Closed Eyes",
    "😋" to "Face Savoring Food",
    "😛" to "Face with Tongue",
    "😝" to "Squinting Face with Tongue",
    "😜" to "Winking Face with Tongue",
    "🤪" to "Zany Face",
    "🤨" to "Face with Raised Eyebrow",
    "🧐" to "Face with Monocle",
    "🤓" to "Nerd Face",
    "😎" to "Smiling Face with Sunglasses",
    "🤩" to "Star-Struck Face",
    "🥳" to "Partying Face",
    "😏" to "Smirking Face",
    "😒" to "Unamused Face",
    "😞" to "Disappointed Face",
    "😔" to "Pensive Face",
    "😟" to "Worried Face",
    "😕" to "Confused Face",
    "🙁" to "Slightly Frowning Face",
    "☹️" to "Frowning Face",
    "😣" to "Persevering Face",
    "😖" to "Confounded Face",
    "😫" to "Tired Face",
    "😩" to "Weary Face",
    "🥺" to "Pleading Face",
    "😢" to "Crying Face",
    "😭" to "Loudly Crying Face",
    "😤" to "Face with Steam from Nose",
    "😠" to "Angry Face",
    "😡" to "Pouting Face",
    "🤬" to "Face with Symbols on Mouth",
    "😈" to "Smiling Face with Horns",
    "👿" to "Angry Face with Horns",
    "💀" to "Skull",
    "☠️" to "Skull and Crossbones",
    "💩" to "Pile of Poo",
    "🤡" to "Clown Face",
    "👹" to "Ogre",
    "👺" to "Goblin",
    "👻" to "Ghost",
    "👽" to "Alien",
    "👾" to "Alien Monster",
    "🤖" to "Robot Face",
    "😺" to "Grinning Cat Face",
    "😸" to "Grinning Cat Face with Smiling Eyes",
    "😹" to "Cat Face with Tears of Joy",
    "😻" to "Smiling Cat Face with Heart Eyes",
    "😼" to "Cat Face with Wry Smile",
    "😽" to "Kissing Cat Face",
    "🙀" to "Weary Cat Face",
    "😿" to "Crying Cat Face",
    "😾" to "Pouting Cat Face",
    "🙈" to "See-No-Evil Monkey",
    "🙉" to "Hear-No-Evil Monkey",
    "🙊" to "Speak-No-Evil Monkey",
    "💋" to "Kiss Mark",
    "💌" to "Love Letter",
    "💘" to "Heart with Arrow",
    "💝" to "Heart with Ribbon",
    "💖" to "Sparkling Heart",
    "💗" to "Growing Heart",
    "💓" to "Beating Heart",
    "💞" to "Revolving Hearts",
    "💕" to "Two Hearts",
    "💟" to "Heart Decoration",
    "❣️" to "Heart Exclamation",
    "👍" to "Thumbs Up",
    "👎" to "Thumbs Down",
    "✊" to "Raised Fist",
    "👊" to "Oncoming Fist",
    "✌️" to "Victory Hand"
)

