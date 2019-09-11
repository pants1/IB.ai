/* Copyright 2017-2019 Jarred Vardy
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

package com.ibdiscord.startup.tasks;

import com.ibdiscord.IBai;
import com.ibdiscord.data.LocalConfig;
import com.ibdiscord.listeners.FilterListener;
import com.ibdiscord.listeners.GuildListener;
import com.ibdiscord.listeners.MessageListener;
import com.ibdiscord.listeners.MonitorListener;
import com.ibdiscord.listeners.ReactionListener;
import com.ibdiscord.listeners.ReadyListener;
import com.ibdiscord.startup.AbstractStartupTask;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public final class StartBot extends AbstractStartupTask {

    @Getter private static JDA jda;

    /**
     * Creates the task.
     */
    public StartBot() {
        super("Start-Bot");
    }

    /**
     * Attempts to start the bot.
     * @throws Exception Any exception.
     */
    @Override
    public void doTask() throws Exception {
        LocalConfig localConfig = IBai.INSTANCE.getConfig();

        jda = new JDABuilder()
                .setToken(localConfig.getBotToken())
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.playing(String.format("v%s | %shelp",
                        localConfig.getBotVersion(),
                        localConfig.getStaticPrefix()))
                )
                .addEventListeners(new FilterListener(),
                        new GuildListener(),
                        new MessageListener(),
                        new MonitorListener(),
                        new ReactionListener(),
                        new ReadyListener()
                )
                .build();
        jda.setAutoReconnect(true);
        jda.awaitReady();
    }

}
