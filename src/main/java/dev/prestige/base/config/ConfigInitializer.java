package dev.prestige.base.config;

import com.google.gson.JsonElement;
import dev.prestige.base.PrestigeBase;
import dev.prestige.base.modules.Module;
import dev.prestige.base.settings.Setting;
import dev.prestige.base.settings.impl.*;
import net.minecraft.block.BlockPlanks;

import java.awt.*;
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
        setModuleBind();
        setModuleSettingValues();
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
                    if (setting.getName().equals("Keybind") || setting.getName().equals("Enabled"))
                        continue;
                    if (setting instanceof StringSetting) {
                        String str = (String) setting.getValue();
                        String properString = str.replace(" ", "_");
                        bufferedWriter.write(setting.getName() + ":" + properString);
                        bufferedWriter.write("\r\n");
                        continue;
                    }
                    if(setting instanceof ColorSetting){
                        bufferedWriter.write(setting.getName() + ":" + ((ColorSetting) setting).getColor().getRGB());
                        bufferedWriter.write("\r\n");
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
                    if (clarification.equals("State"))
                        if (state.equals("Enabled"))
                            module.enableModule();
                });
            } catch (Exception ignored) {
            }
        }
    }

    public void setModuleBind() {
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
                    if (clarification.equals("Keybind")) {
                        if (state.equals("0"))
                            return;
                        module.setKeyBind(Integer.parseInt(state));
                    }
                });
            } catch (Exception ignored) {
            }
        }
    }

    public void setModuleSettingValues() {
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
                    for (Setting setting : module.getSettings()) {
                        if (setting.getName().equals(clarification)) {
                            if(setting instanceof StringSetting){
                                setting.setValue(state);
                                continue;
                            }
                            if (setting instanceof IntegerSetting) {
                                setting.setValue(Integer.parseInt(state));
                                continue;
                            }
                            if(setting instanceof FloatSetting){
                                setting.setValue(Float.parseFloat(state));
                                continue;
                            }
                            if(setting instanceof DoubleSetting){
                                setting.setValue(Double.parseDouble(state));
                                continue;
                            }
                            if(setting instanceof BooleanSetting){
                                setting.setValue(Boolean.parseBoolean(state));
                                continue;
                            }
                            if(setting instanceof KeySetting){
                                setting.setValue(Integer.parseInt(state));
                                continue;
                            }
                            if(setting instanceof ColorSetting){
                                ((ColorSetting) setting).setColor(new Color(Integer.parseInt(state), true));
                                continue;
                            }
                            if(setting instanceof EnumSetting){
                                setting.setValue(state);
                            }
                        }
                    }
                });
            } catch (Exception ignored) {
            }
        }
    }
}