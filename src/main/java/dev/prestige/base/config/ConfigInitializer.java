package dev.prestige.base.config;

import dev.prestige.base.PrestigeBase;
import dev.prestige.base.modules.Module;
import dev.prestige.base.settings.Setting;
import dev.prestige.base.settings.impl.EnumSetting;
import dev.prestige.base.settings.impl.StringSetting;

import java.io.*;
import java.util.ArrayList;

public class ConfigInitializer {
    ArrayList<Module> modules = new ArrayList<>();
    File path;

    public void init() {
        path = new File(PrestigeBase.mc.gameDir + File.separator + "prestigebase");
        if (!path.exists())
            path.mkdir();
        modules.addAll(PrestigeBase.moduleInitializer.getModuleList());
        load();
    }

    public void save() {
        saveModuleFile();
    }

    public void load() {
        setModuleEnabled();
    }

    public void saveModuleFile() {
        try {
            for (Module module : modules) {
                File categoryPath = new File(path.getAbsolutePath() + File.separator + module.getCategory().toString());
                if (!categoryPath.exists())
                    categoryPath.mkdir();
                File file = new File(categoryPath.getAbsolutePath(), module.getName() + ".txt");
                if (!file.exists())
                    file.createNewFile();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                if (!file.exists())
                    file.mkdir();
                bufferedWriter.write("State:" + (module.isEnabled() ? "Enabled" : "Disabled"));
                bufferedWriter.write("\r\n");
                for (Setting setting : module.getSettings()) {
                    if (setting.getName().equals("Keybind"))
                        continue;
                    if(setting instanceof EnumSetting){
                        bufferedWriter.write(setting.getName() + ":" + ((EnumSetting) setting).getValueEnum());
                        bufferedWriter.write("\r\n");
                        continue;
                    }
                    if(setting instanceof StringSetting){
                        String str = (String) setting.getValue();
                        String properString = str.replace(" ", "_");
                        bufferedWriter.write(setting.getName() + ":" + properString);
                        bufferedWriter.write("\r\n");
                        continue;
                    }
                    bufferedWriter.write(setting.getName() + ":" + setting.getValue());
                    bufferedWriter.write("\r\n");
                }
                bufferedWriter.write("Keybind:" + module.getKeyBind());
                bufferedWriter.close();
            }
        } catch (Exception ignored) {
        }
    }

    public void setModuleEnabled() {
        for (Module module : modules) {
            try {
                File categoryPath = new File(path.getAbsolutePath() + File.separator + module.getCategory().toString());
                if (!categoryPath.exists())
                    continue;
                File file = new File(categoryPath.getAbsolutePath(), module.getName() + ".txt");
                if (!file.exists())
                    continue;
                FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(dataInputStream));
                bufferReader.lines().forEach(line -> {
                    String clarification = line.split(":")[0];
                    String state = line.split(":")[1];
                    if(clarification.equals("State"))
                    if (state.equals("Enabled"))
                        module.enableModule();
                });
            } catch (Exception ignored) {
            }
        }
    }

    public void setModuleBind(){

    }
}