package com.chat.app.controller;

import com.chat.app.model.ChatMessage;
import com.chat.app.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {
    @Autowired
    private ChatMessageRepository repository;
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message){

        return repository.save(message);
    }

    @MessageMapping("/editMessage")
    @SendTo("/topic/messages")
    public ChatMessage editMessage(ChatMessage message) {
        ChatMessage existing = repository.findById(message.getId()).orElse(null);
        if (existing != null && existing.getSender().equals(message.getSender())) {
            existing.setContent(message.getContent());

            existing.setEditedAt(Instant.now());

            return repository.save(existing);
        }
        return null;
    }

    @MessageMapping("/deleteMessage")
    @SendTo("/topic/messages")
    public Map<String, String> deleteMessage(ChatMessage message) {
        ChatMessage existing = repository.findById(message.getId()).orElse(null);
        if (existing != null && existing.getSender().equals(message.getSender())) {
            repository.deleteById(message.getId());

            // Return a JSON object instead of a raw string
            Map<String, String> response = new HashMap<>();
            response.put("deleteId", message.getId());
            return response;
        }
        return null;
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
