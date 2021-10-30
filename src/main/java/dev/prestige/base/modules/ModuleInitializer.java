package dev.prestige.base.modules;

import dev.prestige.base.PrestigeBase;
import dev.prestige.base.clickgui.ClickGui;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleInitializer {

    List<Module> moduleList;

    public ModuleInitializer(){
        moduleList = new ArrayList<>();
        init();
    }

    public void init(){
        addModules("combat");
        addModules("core");
        addModules("misc");
        addModules("movement");
        addModules("player");
        addModules("visual");
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public List<Module> getCategoryModules(Module.Category category) {
        final List<Module> list = new ArrayList<>();
        for (Module module : moduleList) {
            if (module.getCategory() == category)
                list.add(module);
        }

        return list;
    }

    public void addModules(String folder) {
        try {
            final List<Class<?>> classes = ClassFinder.from("dev.prestige.base.modules." + folder);
            if (classes == null)
                return;
            for (Class<?> clazz : classes) {
                if (!Modifier.isAbstract(clazz.getModifiers()) && Module.class.isAssignableFrom(clazz)) {
                    for (Constructor<?> constructor : clazz.getConstructors()) {
                        final Module instance = (Module) constructor.newInstance();
                        for (Field field : instance.getClass().getDeclaredFields())
                            if (!field.isAccessible())
                                field.setAccessible(true);

                        moduleList.add(instance);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTick(){
        for(Module module : moduleList){
            if(module.isEnabled())
                module.onTick();
        }
    }

    public void onKey(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || PrestigeBase.mc.currentScreen instanceof ClickGui)
            return;

        moduleList.forEach(module -> {
            if (module.getKeyBind() == eventKey) {
                if (module.isEnabled())
                    module.disableModule();
                else module.enableModule();
            }
        });
    }
}
