package me.daxanius.npe.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            NoPryingEyesConfig config = NoPryingEyesConfig.getInstance();
            NoPryingEyesConfig configDefault = new NoPryingEyesConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("npe.title"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory cat = builder.getOrCreateCategory(Text.translatable("npe.config.category.main"));

            // Signing mode
            cat.addEntry(entryBuilder.startEnumSelector(Text.translatable("npe.config.signing_mode"), NoPryingEyesConfig.SigningMode.class, config.signing_mode)
                    .setTooltip(Text.translatable("npe.config.signing_mode.tooltip"))
                    .setDefaultValue(configDefault.signing_mode)
                    .setSaveConsumer(value -> config.signing_mode = value)
                    .build());

            // Telemetry
            cat.addEntry(entryBuilder.startBooleanToggle(Text.translatable("npe.config.disable_telemetry"), config.disable_telemetry)
                    .setTooltip(Text.translatable("npe.config.disable_telemetry.tooltip"))
                    .setDefaultValue(configDefault.disable_telemetry)
                    .setSaveConsumer(value -> config.disable_telemetry = value)
                    .build());

            // Global bans
            cat.addEntry(entryBuilder.startBooleanToggle(Text.translatable("npe.config.disable_global_bans"), config.disable_global_bans)
                    .setTooltip(Text.translatable("npe.config.disable_global_bans.tooltip"))
                    .setDefaultValue(configDefault.disable_global_bans)
                    .setSaveConsumer(value -> config.disable_global_bans = value)
                    .build());

            // Profanity filter
            cat.addEntry(entryBuilder.startBooleanToggle(Text.translatable("npe.config.disable_profanity_filter"), config.disable_profanity_filter)
                    .setTooltip(Text.translatable("npe.config.disable_profanity_filter.tooltip"))
                    .setDefaultValue(configDefault.disable_profanity_filter)
                    .setSaveConsumer(value -> config.disable_profanity_filter = value)
                    .build());

            // Server toasts
            cat.addEntry(entryBuilder.startBooleanToggle(Text.translatable("npe.config.server_toasts"), config.server_toasts)
                    .setTooltip(Text.translatable("npe.config.server_toasts.tooltip"))
                    .setDefaultValue(configDefault.server_toasts)
                    .setSaveConsumer(value -> config.server_toasts = value)
                    .build());

            // Fake ban
            cat.addEntry(entryBuilder.startBooleanToggle(Text.translatable("npe.config.fake_ban"), config.fake_ban)
                    .setTooltip(Text.translatable("npe.config.fake_ban.tooltip"))
                    .setDefaultValue(configDefault.fake_ban)
                    .setSaveConsumer(value -> config.fake_ban = value)
                    .build());

            // Verbose
            cat.addEntry(entryBuilder.startBooleanToggle(Text.translatable("npe.config.verbose"), config.verbose)
                    .setTooltip(Text.translatable("npe.config.verbose.tooltip"))
                    .setDefaultValue(configDefault.verbose)
                    .setSaveConsumer(value -> config.verbose = value)
                    .build());

            // Chat indicator
            SubCategoryBuilder chatIndicatorSubCat = entryBuilder.startSubCategory(Text.translatable("npe.config.chat_indicator"));

            chatIndicatorSubCat.add(entryBuilder.startBooleanToggle(Text.translatable("npe.config.chat_indicator.hide_red"), config.chat_indicator.hide_red)
                    .setTooltip(Text.translatable("npe.config.chat_indicator.hide_red.tooltip"))
                    .setDefaultValue(configDefault.chat_indicator.hide_red)
                    .setSaveConsumer(value -> config.chat_indicator.hide_red = value)
                    .build());

            chatIndicatorSubCat.add(entryBuilder.startBooleanToggle(Text.translatable("npe.config.chat_indicator.hide_yellow"), config.chat_indicator.hide_yellow)
                    .setTooltip(Text.translatable("npe.config.chat_indicator.hide_yellow.tooltip"))
                    .setDefaultValue(configDefault.chat_indicator.hide_yellow)
                    .setSaveConsumer(value -> config.chat_indicator.hide_yellow = value)
                    .build());

            cat.addEntry(chatIndicatorSubCat.build());

            NoPryingEyesConfig.setInstance(config);
            builder.setSavingRunnable(NoPryingEyesConfig::save);
            return builder.build();
        };
    }
}