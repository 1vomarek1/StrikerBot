package com.vomarek.StrikerBot.ReactionRoles;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

public class ReactionRole {
    private String message;
    private String emote;
    private String role;
    private String requiredRole;

    public ReactionRole(@NotNull String message, @NotNull String emote, @NotNull String role) {
        this.message = message;
        this.emote = emote;
        this.role = role;
        this.requiredRole = null;
    }

    public ReactionRole(@NotNull String message, @NotNull String emote, @NotNull String role, @NotNull String requiredRole) {
        this.message = message;
        this.emote = emote;
        this.role = role;
        this.requiredRole = requiredRole;
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    @NotNull
    public String getEmote() {
        return emote;
    }

    @NotNull
    public String getRole() {
        return role;
    }

    @NotNull
    public Boolean canBeAdded(@NotNull Member member) {
        if (requiredRole == null) return true;

        for (Role r : member.getRoles()) {
            if (requiredRole.equals(r.getId())) return true;
        }

        return false;
    }
}
