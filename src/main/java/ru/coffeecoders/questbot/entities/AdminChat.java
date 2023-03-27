package ru.coffeecoders.questbot.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin_chats")
public class AdminChat {

    @Id
    @Column(name = "admin_chat_id")
    private long adminChatId;
}
