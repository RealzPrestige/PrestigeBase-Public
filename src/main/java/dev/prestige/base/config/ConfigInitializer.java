package dev.prestige.base.config;

import dev.prestige.base.PrestigeBase;
import dev.prestige.base.modules.Module;

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
        saveModuleState();
    }

    public void load(){
        loadModuleState();
    }

    public void saveModuleState() {
        try {
            File file = new File(path.getAbsolutePath(), "EnabledModuleList.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (Module module : modules) {
                if(module.isEnabled()){
                bufferedWriter.write(module.getName());
                bufferedWriter.write("\r\n");
                }
            }
            bufferedWriter.close();
        } catch (Exception ignored) {
        }
    }

    public void loadModuleState(){
        try {
            File file = new File(path.getAbsolutePath(), "EnabledModuleList.txt");
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(dataInputStream));
            bufferReader.lines().forEach(line -> {
                for (Module module : modules) {
                    if (module.getName().equals(line)) {
                        module.enableModule();
                    }
                }
            });
            bufferReader.close();
        } catch (Exception ignored) {
        }
    }
}