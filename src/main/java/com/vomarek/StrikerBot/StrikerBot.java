package com.vomarek.StrikerBot;

import com.vomarek.StrikerBot.ReactionRoles.ReactionRole;
import com.vomarek.StrikerBot.ReactionRoles.ReactionRoles;
import com.vomarek.StrikerBot.YoutubeAnnouncements.YoutubeAnnouncements;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.yaml.snakeyaml.Yaml;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Map;
import java.util.SimpleTimeZone;

public class StrikerBot {

    private static JDA jda;
    private static ReactionRoles reactionRoles;

    private static boolean disableAnnouncements = false;
    private static Map configValues;

    public static void main(String[] args) {

        for (String s : args) {
            if (s.equals("-DisableAnnouncements")) {
                disableAnnouncements = true;
            }
        }

        if (!loadConfigData()) {
            System.out.println("Could not load config.yml");
            return;
        }

        if (configValues == null) return;

        if (!configValues.containsKey("token")) {
            System.out.println("There is no token in the config");
            return;
        }

        final JDABuilder builder = new JDABuilder((String) configValues.get("token"));

        try {
            jda = builder.build();
        } catch (LoginException ignored) {

        }

        if (jda == null) return;

        reactionRoles = new ReactionRoles(jda);
        loadReactionRoles();

        if (!disableAnnouncements) {
            new YoutubeAnnouncements(jda, configValues);
        }
    }

    private static Boolean loadConfigData() {

        final File file = new File("./config.yml");

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();

                if (StrikerBot.class.getResourceAsStream("/config.yml") == null) System.out.println("t");

                final InputStreamReader inStream = new InputStreamReader(StrikerBot.class.getResourceAsStream("/config.yml"));
                final BufferedReader in = new BufferedReader(inStream);

                PrintWriter writer = new PrintWriter(file);

                String line = "";
                while ((line = in.readLine()) != null) {
                    writer.write(line);
                    writer.write("\n");
                    writer.flush();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Yaml yaml = new Yaml();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            configValues = yaml.load(reader);

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static void loadReactionRoles() {
        //
        // #roles
        //

        // Youtube Uploads
        reactionRoles.addReactionRole(new ReactionRole("615210565836472320", "625762168931090461", "615199809883865101"));
        // Anti-Xray plugin updates
        reactionRoles.addReactionRole(new ReactionRole("615210565836472320", "625762203869642772", "594555729583669282"));
        // Video helper
        reactionRoles.addReactionRole(new ReactionRole("615210565836472320", "\uD83C\uDFC5", "701094637410189414"));
        // Video helper
        reactionRoles.addReactionRole(new ReactionRole("615210565836472320", "\uD83C\uDF81", "703346298983546910"));

        //
        // #verify
        //
        reactionRoles.addReactionRole(new ReactionRole("683317823052972032", "\uD83D\uDD16", "680478295921393686"));

        //
        // #subroles
        //

        // Web dev
        reactionRoles.addReactionRole(new ReactionRole("672159388999352320", "672151555155296296", "672145834271309844", "590583011330752533"));
        // Plugin dev
        reactionRoles.addReactionRole(new ReactionRole("672159388999352320", "638420262039060480", "672144965169840129", "590583011330752533"));
        // Bot dev
        reactionRoles.addReactionRole(new ReactionRole("672159388999352320", "672152440711413790", "672145620215136259", "590583011330752533"));

        // Configurator
        reactionRoles.addReactionRole(new ReactionRole("672159388999352320", "\uD83D\uDCDC", "661663271165755412", "590584082027773963"));

        // Builder
        reactionRoles.addReactionRole(new ReactionRole("672159388999352320", "673560945053794324", "661662959822569502", "590581420351553557"));

        // Artist
        reactionRoles.addReactionRole(new ReactionRole("672159388999352320", "\uD83C\uDFA8", "672154492560474163", "590581190122274826"));

        // Youtuber
        reactionRoles.addReactionRole(new ReactionRole("672159388999352320", "674632812317638717", "661663115364270091", "589982966516547587"));

    }


}
