package me.dueris.genesismc.util.console;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OriginConsoleSender extends OriginServerCommandSender implements ConsoleCommandSender {
    protected final ConversationTracker conversationTracker = new ConversationTracker();

    public OriginConsoleSender() {
        super();
    }

    @Override
    public void sendMessage(String message) {
    }

    public void sendRawMessage(String message) {
    }

    public void sendRawMessage(UUID sender, String message) {
    }

    @Override
    public void sendMessage(String... messages) {
    }

    @Override
    public @NotNull String getName() {
        return "ORIGINS";
    }

    @Override
    public net.kyori.adventure.text.Component name() {
        return net.kyori.adventure.text.Component.text(this.getName());
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return this.conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
//        this.conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
//        this.conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(String input) {
        this.conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean isConversing() {
        return this.conversationTracker.isConversing();
    }

    @Override
    public boolean hasPermission(String name) {
        return true;
    }

    @Override
    public boolean hasPermission(org.bukkit.permissions.Permission perm) {
        return true;
    }
}
