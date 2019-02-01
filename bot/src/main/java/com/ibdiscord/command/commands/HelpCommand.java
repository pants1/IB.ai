package com.ibdiscord.command.commands;

import com.ibdiscord.IBai;
import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.utils.UDatabase;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.util.HashSet;

/**
 * Copyright 2019 Arraying, Ray Clark
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class HelpCommand extends Command {

    /**
     * Creates the command.
     */
    public HelpCommand() {
        super("help",
                new HashSet<>(),
                CommandPermission.discord(),
                new HashSet<>()
        );
    }

    /**
     * Shows a list of available commands.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        // TODO go over this and ensure it is accurate.
        String botPrefix = UDatabase.getPrefix(context.getGuild());

        EmbedBuilder ebHelpMenu = new EmbedBuilder();
        ebHelpMenu.setColor(Color.white);
        ebHelpMenu.setAuthor("IB.ai", "https://discord.me/pbh", null);
        ebHelpMenu.setDescription("Hey! Welcome to the IBO Discord Server. I'm IB.ai version: `" + IBai.INSTANCE.getConfig().getBotVersion() + "`." +
                "All command arguments in <> are required, [] are optional. Here's some of the things I can do:");
        ebHelpMenu.addField("Getting Started:", "You can join subjects by typing `" + botPrefix + "join <subject name>`, which will grant you access " +
                "to its subject-specific channels.\n\nWhich subjects are available? See that by typing: `" + botPrefix + "subjectlist`.\n\n" +
                "If you want to leave a role you can type: `" + botPrefix + "leave <subject name>`.\n\n" +
                "To get your final-exam-year, type `" + botPrefix + "join <month> <year>`. This'll also give you a fancy coloured name.", false);
        ebHelpMenu.addField("IB Resources:", "You can go to the IB Resources website to get up-to-date download links for textbooks, past papers, exam guides and more! Type:\n\n" +
                "`link the resources`", false);
        ebHelpMenu.addField("Other Commands:", "- Server details, including member count: `" + botPrefix + "serverinfo`\n" +
                "- Details of a user: `" + botPrefix + "userinfo [user]`\n" +
                "- The bot's ping: `" + botPrefix + "ping`\n" +
                "- See this help menu: `" + botPrefix + "help`", false);
        ebHelpMenu.addField("Information:", "Developed with <3 by the [development team on GitHub](" + IBai.INSTANCE.getConfig().getGithubLink() +")", false);
        context.reply(ebHelpMenu.build());
    }

}
