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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    onChatClicked: (Chat) -> Unit
) {
    when (viewModel.state.value.step) {
        is ChatsSteps.Error -> {}
        is ChatsSteps.IsLoading -> Loading()
        is ChatsSteps.ShowChats -> ChatsView(
            viewModel,
            paddingValues,
            onChatClicked
        )
    }
}

@Composable
fun ChatsView(
    viewModel: ChatsViewModel,
    paddingValues: PaddingValues,
    onChatClicked: (Chat) -> Unit
) {
    when (val chatsMutable = viewModel.getChatsMutable()
        .collectAsState(
            initial = null
        ).value
    ) {
        is OnErrorChats -> {
            // TODO
        }
        is OnSuccessChats -> {
            val chatSnapshot = chatsMutable.querySnapshot
            val chats = chatSnapshot?.map { document ->
                document.toObject(ChatDto::class.java).toModel(document.id)
            }
            chats?.let { chats ->
                Surface(
                    Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
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
                    .align(Alignment.CenterVertically)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = viewModel.getOtherUserName(chat),
                        style = MaterialTheme.typography.h1
                    )
                    Spacer(Modifier.weight(1f))

                    Text(
                        text =
                        if (chat.messages.isNotEmpty()) viewModel.getLastMessageDateTime(chat)
                        else "",
                        style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Light),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = if (chat.messages.isNotEmpty()) chat.messages.last().message else "",
                    style = MaterialTheme.typography.h2,
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