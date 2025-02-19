package com.example.gemini2.ui.theme

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel: GenerativeModel = createGenerativeModel()

    private fun createGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-pro",
            apiKey = Constants.apiKey
        )
    }

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                // Create history from current messageList
                val history = messageList.map { message ->
                    content(message.role) {
                        text(message.message)
                    }
                }

                // Add user's question to the message list
                messageList.add(MessageModel(question, "user"))

                // Update UI to show "typing" state
                messageList.add(MessageModel("typing", role = "model"))

                // Perform AI response
                val chatResponse = generativeModel.startChat(history)
                val responseText = chatResponse.sendMessage(question).text.toString()

                // Update UI with AI response
                messageList.removeLast() // Remove "typing" state
                messageList.add(MessageModel(responseText, "model"))

                // Log response
                Log.e("response from ai", responseText)
            } catch (e: Exception) {
                // Handle error
                messageList.add(MessageModel("Oops! Something went wrong.", "user"))
                Log.e("sendMessage error", "Error: ${e.message}", e)
            }
        }
    }
}
