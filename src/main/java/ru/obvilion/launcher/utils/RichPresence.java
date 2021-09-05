package ru.obvilion.launcher.utils;

import club.minnced.discord.rpc.*;

public class RichPresence {
    private final String applicationId = "657878741703327754";
    private final DiscordRPC lib = DiscordRPC.INSTANCE;

    public DiscordEventHandlers handlers;
    public DiscordRichPresence presence;

    public RichPresence() {
        presence = new DiscordRichPresence();

        presence.details = Lang.get("inAuth");
        presence.largeImageKey = "logo";

        handlers = new DiscordEventHandlers();

        lib.Discord_Initialize(applicationId, handlers, true, null);
        render();

        System.out.println("Initialized Discord rich presence.");
    }

    public void updateTimestamp() {
        presence.startTimestamp = System.currentTimeMillis();
        render();
    }

    public void updateDescription(String description) {
        presence.details = description;
        render();
    }

    public void updateState(String state) {
        presence.state = state;
        render();
    }

    public void render() {
        lib.Discord_UpdatePresence(presence);
    }

    public void dispose() {
        lib.Discord_Shutdown();
    }
}
