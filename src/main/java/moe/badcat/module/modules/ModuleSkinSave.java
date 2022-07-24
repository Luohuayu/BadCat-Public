package moe.badcat.module.modules;

import moe.badcat.hook.SkinCompletionHook;
import moe.badcat.module.BaseEnableModule;
import moe.badcat.module.ITickable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleSkinSave extends BaseEnableModule implements ITickable {
    protected Class<?> class_ModelBakery;
    protected Class<?> class_SkinIOUtils;
    protected Class<?> class_Skin;
    protected Method method_saveSkinToStream;
    protected Method method_skinString1;
    protected Method method_skinString2;

    protected Map<Object, SkinInfo> loadSkins = new ConcurrentHashMap<Object, SkinInfo>();
    protected File saveDir = null;
    protected boolean useCache = false;

    public ModuleSkinSave(String displayName) {
        super("MOD类", displayName);
    }

    public ModuleSkinSave() {
        this("时装保存");
    }

    @Override
    public boolean initModule() {
        /*
        sig:
        ModelBakery: java.util.concurrent.CompletionService
        SkinIOUtils: java.lang.String ".armour"
        */
        return tryInit("moe.plushie.armourers_workshop.client.model.bake.ModelBakery", "moe.plushie.armourers_workshop.utils.SkinIOUtils");
    }

    public boolean tryInit(String classModelBakery, String classSkinIOUtils) {
        try {
            class_ModelBakery = Class.forName(classModelBakery);
            class_SkinIOUtils = Class.forName(classSkinIOUtils);

            Object instance = null;
            for (Field field : class_ModelBakery.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getType() == class_ModelBakery) {
                    instance = field.get(null);
                    break;
                }
            }
            if (instance == null) return false;

            Field field_CompletionService = null;
            for (Field field : class_ModelBakery.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getType() == CompletionService.class) {
                    field_CompletionService = field;
                    break;
                }
            }
            if (field_CompletionService == null) return false;

            for (Method method : class_SkinIOUtils.getMethods()) {
                if (method.getParameterCount() == 2 && method.getParameterTypes()[0] == OutputStream.class && method.getReturnType() == boolean.class) {
                    class_Skin = method.getParameterTypes()[1];
                    method_saveSkinToStream = method;
                    break;
                }
            }
            if (method_saveSkinToStream == null || class_Skin == null) return false;

            for (Method method : class_Skin.getMethods()) {
                if (method.getParameterCount() == 0 && method.getReturnType() == String.class && !"toString".equals(method.getName())) {
                    if (method_skinString1 == null) {
                        method_skinString1 = method;
                    } else if (method_skinString2 == null) {
                        method_skinString2 = method;
                    } else {
                        // unknown version, not get skin name
                        method_skinString1 = null;
                        method_skinString2 = null;
                        break;
                    }
                }
            }

            CompletionService<?> completionService = (CompletionService<?>)field_CompletionService.get(instance);
            completionService = new SkinCompletionHook(this, completionService);
            field_CompletionService.set(instance, completionService);

            return true;
        } catch (Exception ignored) {}
        return false;
    }

    @Override
    public void onUse() {
        super.onUse();

        if (isEnable()) {
            saveDir = new File("serverSkin");
            if (!saveDir.exists()) saveDir.mkdir();
        }
    }

    @Override
    public void tick() {
        if (isEnable()) {
            for (Map.Entry<Object, SkinInfo> entry : loadSkins.entrySet()) {
                Object skin = entry.getKey();
                SkinInfo skinInfo = entry.getValue();
                if (!skinInfo.saved) {
                    // debug output
                    try {
                        if (ModuleDebug.enable) System.out.println(skin);
                    } catch (Exception e) {
                        if (ModuleDebug.enable) e.printStackTrace();
                    }

                    // get name
                    String uuid = UUID.nameUUIDFromBytes((skin.toString() + ":" + skin.hashCode()).getBytes(StandardCharsets.UTF_8)).toString().replace("-", "");
                    String name;
                    try {
                        name = (method_skinString1 != null && method_skinString2 != null) ? method_skinString1.invoke(skin) + " - " + method_skinString2.invoke(skin) + " - " + uuid : uuid;
                    } catch (Exception e) {
                        name = uuid;
                    }

                    // save to file
                    if (skinInfo.cachedBytes == null) {
                        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(saveDir, name + ".armour"))) {
                            if (method_saveSkinToStream.getParameterTypes()[1] == class_Skin) {
                                method_saveSkinToStream.invoke(null, fileOutputStream, skin);
                            } else {
                                method_saveSkinToStream.invoke(null, skin, fileOutputStream);
                            }
                            fileOutputStream.flush();
                        } catch (Exception e) {
                            if (ModuleDebug.enable) e.printStackTrace();
                        }
                    } else {
                        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(saveDir, name + ".armour"))) {
                            fileOutputStream.write(skinInfo.cachedBytes);
                            fileOutputStream.flush();
                        } catch (Exception e) {
                            if (ModuleDebug.enable) e.printStackTrace();
                        }
                    }

                    // set saved flag
                    skinInfo.saved = true;
                }
            }
        }
    }

    public void hookSubmit(Object task) {
        try {
            for (Field field : task.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getType() == class_Skin) {
                    Object skin = field.get(task);
                    if (!loadSkins.containsKey(skin)) {
                        SkinInfo skinInfo = new SkinInfo();
                        if (useCache) {
                            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                                try (DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {
                                    if (method_saveSkinToStream.getParameterTypes()[1] == class_Skin) {
                                        method_saveSkinToStream.invoke(null, dataOutputStream, skin);
                                    } else {
                                        method_saveSkinToStream.invoke(null, skin, dataOutputStream);
                                    }
                                    dataOutputStream.flush();
                                }
                                byteArrayOutputStream.flush();
                                skinInfo.cachedBytes = byteArrayOutputStream.toByteArray();
                            } catch (Exception e) {
                                if (ModuleDebug.enable) e.printStackTrace();
                            }
                        }
                        loadSkins.put(skin, skinInfo);
                        onSkinSubmit(skin, skinInfo);
                        break;
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    protected void onSkinSubmit(Object skin, SkinInfo skinInfo) {}

    static class SkinInfo {
        public boolean saved;
        public byte[] cachedBytes;
    }
}
