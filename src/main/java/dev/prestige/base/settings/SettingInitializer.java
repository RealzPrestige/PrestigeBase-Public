package dev.prestige.base.settings;

import dev.prestige.base.modules.Module;

import java.util.ArrayList;
import java.util.List;

public class SettingInitializer {

    List<Setting> settingRewriteList;

    public SettingInitializer() {
        settingRewriteList = new ArrayList<>();
    }

    public void addSetting(Setting setting) {
        settingRewriteList.add(setting);
    }

    public List<Setting> getSettingsInModule(Module module) {
        List<Setting> settings = new ArrayList<>();
        for (Setting setting : settingRewriteList) {
            if (!setting.getModule().equals(module))
                continue;

            settings.add(setting);
        }
        return settings;
    }
}
