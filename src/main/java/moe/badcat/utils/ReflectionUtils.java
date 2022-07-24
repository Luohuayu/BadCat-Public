package moe.badcat.utils;

import moe.badcat.BadCat;

import moe.badcat.module.modules.ModuleDebug;
import java.lang.reflect.Field;

public class ReflectionUtils {
    public static void setPrivateField(Class<?> clazz, Object instance, String deobfName, String obfName, Object newObject) {
        setPrivateField(clazz, instance, BadCat.getInstance().isDeobfEnv() ? deobfName : obfName, newObject);
    }

    public static void setPrivateField(Class<?> clazz, Object instance, String name, Object newObject) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);

            field.set(instance, newObject);
        } catch (Exception e) {
            if (ModuleDebug.enable) e.printStackTrace();
        }
    }

    public static Object getPrivateField(Class<?> clazz, Object instance, String deobfName, String obfName) {
        return getPrivateField(clazz, instance, BadCat.getInstance().isDeobfEnv() ? deobfName : obfName);
    }

    public static Object getPrivateField(Class<?> clazz, Object instance, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);

            return field.get(instance);
        } catch (Exception e) {
            if (ModuleDebug.enable) e.printStackTrace();
        }
        return null;
    }
}
