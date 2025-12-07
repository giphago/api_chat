package com.example.chatbot_tra

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot_tra.adapter.MessageAdapter
import com.example.chatbot_tra.model.Message
import com.example.chatbot_tra.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val TAG = "DEEPL_CHATBOT"

    private lateinit var editTextInput: EditText
    private lateinit var buttonTranslate: Button
    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT

        setContentView(R.layout.activity_main)

        editTextInput = findViewById(R.id.editTextInput)
        buttonTranslate = findViewById(R.id.buttonTranslate)
        recyclerViewChat = findViewById(R.id.recyclerViewChat)

        messageAdapter = MessageAdapter(messageList)
        recyclerViewChat.layoutManager = LinearLayoutManager(this)
        recyclerViewChat.adapter = messageAdapter

        buttonTranslate.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val textToTranslate = editTextInput.text.toString().trim()
        if (textToTranslate.isEmpty()) {
            Toast.makeText(this, "번역할 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val userMessage = Message(textToTranslate, isUser = true)
        messageAdapter.addMessage(userMessage)
        recyclerViewChat.scrollToPosition(messageAdapter.itemCount - 1)

        editTextInput.text.clear()

        val loadingMessage = Message("...번역 중...", isUser = false)
        messageAdapter.addMessage(loadingMessage)
        recyclerViewChat.scrollToPosition(messageAdapter.itemCount - 1)

        translateText(textToTranslate, loadingMessage)
    }

    private fun translateText(text: String, loadingMessage: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.deepLService.translateText(
                    authHeader = "DeepL-Auth-Key ${RetrofitClient.API_KEY}",
                    targetLanguage = "EN",
                    text = text
                )

                withContext(Dispatchers.Main) {
                    // 로딩 메시지를 제거하고 실제 응답으로 대체
                    val loadingIndex = messageList.indexOf(loadingMessage)
                    if (loadingIndex != -1) {
                        messageList.removeAt(loadingIndex)
                        messageAdapter.notifyItemRemoved(loadingIndex)
                    }

                    if (response.isSuccessful) {
                        val translatedText = response.body()?.translations?.firstOrNull()?.text ?: "번역 결과를 찾을 수 없습니다."

                        // ✅ 성공: 챗봇 메시지 추가
                        val botMessage = Message(translatedText, isUser = false)
                        messageAdapter.addMessage(botMessage)

                    } else {
                        // ❌ 실패: 오류 메시지 추가
                        val code = response.code()
                        val errorText = "❌ 오류 (Code: $code): 번역에 실패했습니다."
                        val errorBotMessage = Message(errorText, isUser = false)
                        messageAdapter.addMessage(errorBotMessage)
                    }
                    recyclerViewChat.scrollToPosition(messageAdapter.itemCount - 1)
                }

            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    val loadingIndex = messageList.indexOf(loadingMessage)
                    if (loadingIndex != -1) {
                        messageList.removeAt(loadingIndex)
                        messageAdapter.notifyItemRemoved(loadingIndex)
                    }

                    val errorText = "❌ 네트워크 오류: 연결 상태를 확인하세요."
                    val errorBotMessage = Message(errorText, isUser = false)
                    messageAdapter.addMessage(errorBotMessage)
                    recyclerViewChat.scrollToPosition(messageAdapter.itemCount - 1)

                    Toast.makeText(this@MainActivity, errorText, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val loadingIndex = messageList.indexOf(loadingMessage)
                    if (loadingIndex != -1) {
                        messageList.removeAt(loadingIndex)
                        messageAdapter.notifyItemRemoved(loadingIndex)
                    }
                    val errorText = "❌ 알 수 없는 오류 발생"
                    val errorBotMessage = Message(errorText, isUser = false)
                    messageAdapter.addMessage(errorBotMessage)
                    recyclerViewChat.scrollToPosition(messageAdapter.itemCount - 1)
                }
            }
        }
    }
}