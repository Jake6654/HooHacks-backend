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


        ChatResponse response = chatClient.prompt()
                .user(prompt)
                .call()
                .chatResponse();


        String aiReply = response.getResult()
                .getOutput()
                .getText(); // or getContent()


        conversationList.add("AI: " + aiReply);

        return aiReply;
    }


    public List<String> getConversationList() {
        return conversationList;
    }
}