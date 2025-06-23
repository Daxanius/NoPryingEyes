package me.daxanius.npe.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.daxanius.npe.NoPryingEyes;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NoPryingEyesConfig {
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("npe.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static NoPryingEyesConfig INSTANCE;
    //https://stackoverflow.com/a/5889590/22705536
    //the transient keyword should exclude tempSign from being serialized
    private transient boolean tempSign = false;
    private transient boolean toastHasBeenSent = false;

    public boolean disable_telemetry = true;

    public boolean disable_global_bans = true;

    public boolean disable_profanity_filter = true;

    public boolean server_toasts = true;

    public boolean fake_ban = false;

    public boolean verbose = false;

    public SigningMode signing_mode = SigningMode.NO_KEY;

    public OnDemandWarning onDemandWarning = OnDemandWarning.IF_TOAST_NOT_SENT;

    public ChatIndicatorOptions chat_indicator = new ChatIndicatorOptions();

    // For ease of use
    public boolean noSign() {
        return (signing_mode != SigningMode.SIGN && !tempSign()) || noKey();
    }

    public boolean noKey() {
        return signing_mode == SigningMode.NO_KEY;
    }

    public boolean onDemand() {
        return signing_mode == SigningMode.ON_DEMAND;
    }

    public boolean tempSign() {
        return tempSign;
    }

    public void setTempSign(boolean value) {
        this.tempSign = value;
    }

    public boolean toastHasBeenSent() {
        return toastHasBeenSent;
    }

    public void setToastHasBeenSent(boolean value) {
        this.toastHasBeenSent = value;
    }

    public static class ChatIndicatorOptions {
        public boolean hide_red = true;
        public boolean hide_yellow = false;
    }

    // There are 3 signing modes which the user can select
    public enum SigningMode {
        // Sign messages normally
        SIGN,

        // Don't sign messages, but still send the public key
        NO_SIGN,

        // Don't send the public key at all
        NO_KEY,

        // Sign messages only if demanded by the server
        ON_DEMAND
    }

    public enum OnDemandWarning {
        //Always
        ALWAYS,

        //Never
        NEVER,

        //Only if the toast has not been sent
        IF_TOAST_NOT_SENT
    }


    public static NoPryingEyesConfig getInstance() {
        if (INSTANCE == null) {
            load();
        }

        return INSTANCE;
    }

    // Sets the current instance with another one
    public static void setInstance(NoPryingEyesConfig instance) {
        INSTANCE = instance;
    }

    public static void load() {
        NoPryingEyes.LOGGER.info("Loading config");
        INSTANCE = readFile();

        if (INSTANCE == null) {
            INSTANCE = new NoPryingEyesConfig();
            save();
        }
    }

    public static void save() {
        NoPryingEyes.LOGGER.info("Saving config");
        if (INSTANCE == null) {
            INSTANCE = new NoPryingEyesConfig();
        }

        writeFile(INSTANCE);
    }

    @Nullable
    private static NoPryingEyesConfig readFile() {
        if (!Files.isRegularFile(CONFIG_FILE))
            return null;

        try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE)) {
            return GSON.fromJson(reader, NoPryingEyesConfig.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void writeFile(NoPryingEyesConfig instance) {
        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}