package com.chat.app.controller;

import com.chat.app.model.ChatMessage;
import com.chat.app.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private ChatMessageRepository repository;
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message){

        return repository.save(message);
    }

    @GetMapping("/chat/history")
    @ResponseBody
    public List<ChatMessage> getChatHistory() {
        return repository.findTop50ByOrderByTimestampAsc();
    }

    @GetMapping("chat")
    public String chat() {
        return "chat";
    }
}
