package com.chat.app.service.Impl;

import com.chat.app.model.ChatMessage;
import com.chat.app.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TelegramServiceImpl implements TelegramService {
    @Value("${telegram_bot_token}")
    private String botToken;
    @Value("${telegram_chat_id}")
    private String chatId;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Async
    public void notifyNewMessage(ChatMessage message) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        System.out.println(">>> Telegram firing. Token=" + botToken + " ChatId=" + chatId);

        String text = String.format(
                "💬 *New message in chat*\n👤 *From:* %s\n📝 *Message:* %s\n🔗 [Open Chat](https://chat.hrdevent.app/chat)",
                escapeMarkdown(message.getSender()),
                escapeMarkdown(message.getContent())
        );

        Map<String, String> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("text", text);
        body.put("parse_mode", "Markdown");

        try {
            String response = restTemplate.postForObject(url, body, String.class);
            System.out.println(">>> Telegram response: " + response); // 👈
        } catch (Exception e) {
            System.err.println(">>> Telegram FAILED: " + e.getMessage()); // 👈
        }
    }
    private String escapeMarkdown(String text) {
        return text.replace("_", "\\_").replace("*", "\\*").replace("`", "\\`");
    }
}
