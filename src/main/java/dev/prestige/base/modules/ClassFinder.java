package dev.prestige.base.modules;

import com.google.common.reflect.ClassPath;
import dev.prestige.base.PrestigeBase;
import net.minecraft.launchwrapper.Launch;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public final class ClassFinder {

    public static List<Class<?>> from(String packageName) {
        try {
            final List<Class<?>> classes = new ArrayList<>();
            for (ClassPath.ClassInfo info : ClassPath.from(Launch.classLoader).getAllClasses()) {
                if (info.getName().startsWith(packageName)) {
                    classes.add(info.load());
                }
            }
            return classes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
