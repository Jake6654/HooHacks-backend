package com.example.travellog.service;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TravelChatBotService {

    private final ChatClient chatClient;


    private final List<String> conversationList = new ArrayList<>();


    public String askAi(String userMessage) {
        conversationList.add("User: " + userMessage);

        String prompt = userMessage + "\n\nPlease answer in 2 to 3 concise sentences. Do not exceed 3 lines.";


        //  AI call
        ChatResponse response = chatClient.prompt()
                .user(prompt)
                .call() // Spring AI M6 기준
                .chatResponse();  // chatResponse()로 변환

        //  Abstract content from AI
        String aiReply = response.getResult()
                .getOutput()
                .getText(); // or getContent()

        // 4) store
        conversationList.add("AI: " + aiReply);

        return aiReply;
    }

    /**
     * 현재까지 누적된 대화 목록 반환 (화면 표시용)
     */
    public List<String> getConversationList() {
        return conversationList;
    }
}
