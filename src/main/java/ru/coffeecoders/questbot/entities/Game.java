/**
 * Author: ezuykow
 */

package ru.coffeecoders.questbot.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @Column(name = "game_name")
    private String gameName;

    @Column(name = "global_chat_id")
    private long globalChatId;

    @Column(name = "groups")
    private Object groups;
}
