package com.example.gemini2

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gemini2.ui.theme.ChatViewModel
import com.example.gemini2.ui.theme.MessageModel
import com.example.gemini2.ui.theme.colormodelmessage
import com.example.gemini2.ui.theme.colorusermessage

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E1E1E), Color(0xFF2A2A2A))
                )
            )
    ) {
        AppHeader()
        MessageList(
            modifier = Modifier.weight(1f),
            messageList = viewModel.messageList
        )
        MessageInput { message ->
            viewModel.sendMessage(message)
        }
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messageList: List<MessageModel>
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 12.dp),
        reverseLayout = true
    ) {
        items(
            count = messageList.size,
            itemContent = { index ->
                val message = messageList[messageList.size - 1 - index]
                MessageRow(messageModel = message)
            }
        )
    }
}

@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.role == "model"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(
                    start = if (isModel) 12.dp else 50.dp,
                    end = if (isModel) 50.dp else 12.dp
                )
                .clip(RoundedCornerShape(16.dp))
                .background(if (isModel) colormodelmessage else colorusermessage)
                .padding(14.dp)
        ) {
            Text(
                text = messageModel.message,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun MessageInput(onMessageSend: (String) -> Unit) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            placeholder = { Text("Type a message...") },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                if (message.isNotEmpty()) {
                    onMessageSend(message)
                    message = ""
                }
            },
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .border(1.dp, Color.White, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.White
            )
        }
    }
}


@Composable
fun AppHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "GemBot",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
