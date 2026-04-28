package com.chat.app.service;

import com.chat.app.model.ChatMessage;

public interface TelegramService {
    void notifyNewMessage(ChatMessage saved);
}
