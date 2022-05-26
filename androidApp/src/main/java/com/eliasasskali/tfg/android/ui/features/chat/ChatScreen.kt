package com.eliasasskali.tfg.android.ui.features.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.features.clubs.Loading
import com.eliasasskali.tfg.model.ChatDto

/**
 * The home view which will contain all the code related to the view for HOME.
 *
 * Here we will show the list of chat messages sent by user.
 * And also give an option to send a message and logout.
 */

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    paddingValues: PaddingValues
) {
    when (viewModel.state.value.step) {
        is ChatSteps.Error -> {}
        is ChatSteps.IsLoading -> Loading()
        is ChatSteps.ShowChat -> ChatView(viewModel, paddingValues)
    }
}

@Composable
fun ChatView(
    viewModel: ChatViewModel,
    paddingValues: PaddingValues
) {
    val newMessage = viewModel.state.value.newMessage
    val focusManager = LocalFocusManager.current

    when (val chatMutable = viewModel.getChatMutable()
        .collectAsState(
            initial = null
        ).value) {

        is OnErrorChat -> {
            // TODO
        }
        is OnSuccessChat -> {
            val chatSnapshot = chatMutable.querySnapshot
            val chat = chatSnapshot?.toObject(ChatDto::class.java)?.toModel(chatSnapshot.id)
            chat?.messages?.let { messages ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1f, fill = true),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        reverseLayout = true
                    ) {
                        items(messages.reversed()) { message ->
                            val isCurrentUser = viewModel.isSender(message)

                            SingleMessage(
                                message = message.message,
                                sentOnString = viewModel.getDateTime(message.sentOn),
                                isCurrentUser = isCurrentUser,
                                modifier =
                                if (isCurrentUser) Modifier
                                    .align(Alignment.Start)
                                else Modifier
                                    .align(Alignment.End)
                            )
                        }
                    }
                    OutlinedTextField(
                        value = newMessage,
                        onValueChange = {
                            viewModel.setNewMessage(it)
                        },
                        label = {
                            Text(
                                stringResource(id = R.string.type_message)
                            )
                        },
                        maxLines = 3,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 12.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                        ),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    focusManager.clearFocus()
                                    viewModel.sendMessage(newMessage.trimEnd())
                                },
                                enabled = viewModel.state.value.newMessage.isNotBlank()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = stringResource(id = R.string.send_button)
                                )
                            }
                        }
                    )
                }
            }
        }
        null -> {
            // TODO
        }
    }
}

@Composable
fun SingleMessage(
    message: String,
    sentOnString: String,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {
    val paddingStart = if (isCurrentUser) 0.dp else 24.dp
    val paddingEnd = if (isCurrentUser) 24.dp else 0.dp
    Card(
        shape = RoundedCornerShape(4.dp),
        backgroundColor = if (isCurrentUser) MaterialTheme.colors.primary
        else MaterialTheme.colors.secondary,
        modifier = modifier
            .padding(start = paddingStart, end = paddingEnd)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = message,
                textAlign =
                if (isCurrentUser)
                    TextAlign.End
                else
                    TextAlign.Start,
                modifier = Modifier
                    .padding(8.dp),
                color =
                if (isCurrentUser) MaterialTheme.colors.onPrimary
                else MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.body1.copy(fontSize = 14.sp)
            )
            Text(
                text = sentOnString,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.caption.copy(fontSize = 10.sp)
            )
        }
    }
}