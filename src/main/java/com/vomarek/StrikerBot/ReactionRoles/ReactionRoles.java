package com.vomarek.StrikerBot.ReactionRoles;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.handle.GuildSetupController;
import org.jetbrains.annotations.NotNull;
import sun.plugin.javascript.navig.Array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReactionRoles extends ListenerAdapter {
    private JDA jda;
    private HashMap<Integer, ReactionRole> reactionRoles;

    public ReactionRoles(JDA jda) {
        this.jda = jda;
        reactionRoles = new HashMap<Integer, ReactionRole>();
        jda.addEventListener(this);
    }

    public void addReactionRole(ReactionRole role) {
        reactionRoles.put(reactionRoles.size(),role);
    }

    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {

        for (ReactionRole role : reactionRoles.values()) {

            if (!role.getMessage().equals(event.getMessageId())) continue;

            if (event.getReaction().getReactionEmote().isEmoji()) {
                if (!role.getEmote().equals(event.getReactionEmote().getEmoji())) continue;
            } else {
                if (!role.getEmote().equals(event.getReactionEmote().getId())) continue;
            }


            final Role discordrole = event.getGuild().getRoleById(role.getRole());

            if (discordrole == null) continue;

            if (!role.canBeAdded(event.getMember())) continue;

            final List<Role> roles = new ArrayList<Role>(event.getMember().getRoles());

            roles.add(discordrole);

            event.getGuild().modifyMemberRoles(event.getMember(), roles).queue();
        }

    }

    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {

        for (ReactionRole role : reactionRoles.values()) {

            if (!role.getMessage().equals(event.getMessageId())) continue;

            if (event.getReaction().getReactionEmote().isEmoji()) {
                if (!role.getEmote().equals(event.getReactionEmote().getEmoji())) continue;
            } else {
                if (!role.getEmote().equals(event.getReactionEmote().getId())) continue;
            }

            final Role discordrole = event.getGuild().getRoleById(role.getRole());

            if (event.getMember() == null) continue;

            if (discordrole == null) continue;

            final List<Role> roles = new ArrayList<Role>(event.getMember().getRoles());

            roles.remove(discordrole);

            event.getGuild().modifyMemberRoles(event.getMember(), roles).queue();
        }

    }
}
