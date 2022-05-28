package com.eliasasskali.tfg.android.ui.features.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.R
import com.eliasasskali.tfg.android.ui.features.clubs.Loading
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.model.Chat
import com.eliasasskali.tfg.model.ChatDto
import com.eliasasskali.tfg.model.Message
import org.koin.androidx.compose.get

@Composable
fun ChatsScreen(
    viewModel: ChatsViewModel,
    paddingValues: PaddingValues,
    onChatClicked: (Chat) -> Unit,
    onFindClubsClicked: () -> Unit = {}
) {
    when (viewModel.state.value.step) {
        is ChatsSteps.Error -> {}
        is ChatsSteps.IsLoading -> Loading()
        is ChatsSteps.ShowChats -> ChatsView(
            viewModel,
            paddingValues,
            onChatClicked,
            onFindClubsClicked
        )
    }
}

@Composable
fun ChatsView(
    viewModel: ChatsViewModel,
    paddingValues: PaddingValues,
    onChatClicked: (Chat) -> Unit,
    onFindClubsClicked: () -> Unit
) {
    Surface(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.chats),
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
            )
            Divider()
            Spacer(Modifier.height(8.dp))

            when (val chatsMutable = viewModel.getChatsMutable()
                .collectAsState(
                    initial = null
                ).value
            ) {
                is OnErrorChats -> {
                    // TODO
                    println("ERROR OBTAINING CHATS")
                }
                is OnSuccessChats -> {
                    val chatSnapshot = chatsMutable.querySnapshot
                    val chats = chatSnapshot?.map { document ->
                        document.toObject(ChatDto::class.java).toModel(document.id)
                    }
                    chats?.let { chats ->
                        if (chats.isEmpty()) {
                            NoActiveChatsView(viewModel, onFindClubsClicked)
                        } else {
                            LazyColumn(
                                Modifier
                                    .fillMaxSize()
                            ) {
                                items(chats.size) { index ->
                                    ChatCard(
                                        chat = chats[index],
                                        onChatClicked = onChatClicked,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }
                    }
                }
                null -> {
                    NoActiveChatsView(viewModel, onFindClubsClicked)
                }
            }
        }
    }
}

@Composable
fun ChatCard(
    chat: Chat,
    onChatClicked: (Chat) -> Unit,
    viewModel: ChatsViewModel
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onChatClicked(chat) }
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .align(CenterVertically)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = viewModel.getOtherUserName(chat),
                        style = MaterialTheme.typography.h1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.weight(1f))

                    Text(
                        text =
                        if (chat.messages.isNotEmpty()) viewModel.getLastMessageDateTime(chat)
                        else "",
                        style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Light),
                        modifier = Modifier
                            .align(CenterVertically),
                        maxLines = 1,
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    val youOrOtherUser = if (chat.messages.isNotEmpty()) {
                            if (chat.messages.last().senderId == viewModel.getUserId()) {
                                stringResource(id = R.string.you) + ":"
                            } else {
                                viewModel.getOtherUserName(chat) + ":"
                            }
                    } else {
                        stringResource(id = R.string.no_messages_yet)
                    }
                    Text(
                        text = youOrOtherUser,
                        style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier
                            .align(CenterVertically)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (chat.messages.isNotEmpty()) chat.messages.last().message else "",
                        style = MaterialTheme.typography.h2,
                        maxLines = 1,
                        modifier = Modifier.align(CenterVertically),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun NoActiveChatsView(
    viewModel: ChatsViewModel,
    onFindClubsClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = 12.dp,
                vertical = 16.dp
            )
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.you_have_no_active_chats),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))

        if (!viewModel.isClub()) {
            Text(
                text = stringResource(R.string.find_clubs_to_start_a_chat),
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(CenterHorizontally)
            )
            Spacer(Modifier.height(8.dp))

            Button(
                modifier = Modifier
                    .align(CenterHorizontally),
                onClick = {
                    onFindClubsClicked()
                }) {
                Text(
                    text = stringResource(R.string.find_clubs),
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ChatCardPreview() {
    val chat = Chat(
        "chatID",
        "senderID",
        "receiverID",
        "senderName",
        "receiverName",
        messages = listOf(
            Message(
                "Hello, how are you?",
                "senderID",
                1939832930202
            )
        )
    )
    AppTheme {
        Surface {
            ChatCard(chat = chat, onChatClicked = {}, viewModel = get())
        }
    }
}