package dev.prestige.base.settings;

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


}
