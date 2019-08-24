/**
 * Copyright 2017-2019 Arraying, Jarred Vardy <jarred.vardy@gmail.com>
 *
 * This file is part of IB.ai.
 *
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord.listeners;

import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.react.EmoteData;
import com.ibdiscord.data.db.entries.react.ReactionData;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ReactionListener extends ListenerAdapter {

    /**
     * Triggered when a reaction is added from a message in a private chat,
     * private group DM or within a guild.
     * Uses a case to filter the event down to a single group type.
     * @param event The event.
     */
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        react(event.getMember(), event.getMessageIdLong(), getEmoji(event.getReactionEmote()), true);
    }

    /**
     * Triggered when a reaction is removed from a message in a private chat,
     * private group DM or within a guild.
     * Uses a case to filter the event down to a single group type.
     * @param event The event.
     */
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        react(event.getMember(), event.getMessageIdLong(), getEmoji(event.getReactionEmote()), false);
    }

    /**
     * Handles the reaction and adding/removing roles.
     * @param member The member.
     * @param message The message ID.
     * @param emote The emoji ID.
     * @param add True to add, false to remove.
     */
    private void react(Member member, long message, String emote, boolean add) {
        Guild guild = member.getGuild();
        ReactionData reactionData = DContainer.INSTANCE.getGravity().load(new ReactionData(guild.getId(), message));
        EmoteData emoteData = DContainer.INSTANCE.getGravity().load(new EmoteData(reactionData.get(emote).asString()));
        Collection<Role> roles = emoteData.contents().stream()
                .map(prop -> member.getGuild().getRoleById(prop.defaulting(0L).asLong()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if(add) {
            guild.getController().addRolesToMember(member, roles).queue(null, Throwable::printStackTrace);
        } else {
            guild.getController().removeRolesFromMember(member, roles).queue(null, Throwable::printStackTrace);
        }
    }

    /**
     * Gets the emoji of the reaction.
     * @param emote The emote object.
     * @return An emoji.
     */
    private String getEmoji(MessageReaction.ReactionEmote emote) {
        return emote.isEmote() ?
                emote.getEmote().getAsMention() :
                emote.getName();
    }

}
