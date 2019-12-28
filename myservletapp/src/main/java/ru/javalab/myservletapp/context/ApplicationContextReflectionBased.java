package ru.javalab.myservletapp.context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationContextReflectionBased implements ApplicationContext {
    private Map<String, Object> cmap;

    public ApplicationContextReflectionBased() {
        cmap = new HashMap<String, Object>();
    }


    public <T> T getComponent(Class<T> componentType, String name) {
        if (!cmap.containsKey(name)) {
            createComponent(componentType);
        }
        return (T) cmap.get(name);
    }

    public <T> void createComponent(Class<T> componentType) {
        try {
            if (cmap.get(componentType.getName()) == null) {
                Field[] fields = componentType.getDeclaredFields();
                for (Field f : fields) {
                    Class[] inter = f.getType().getInterfaces();
                    for (Class i : inter) {
                        if (i.getName().equals(Component.class.getName())) {
                            createComponent(f.getType());
                        }
                    }
                }
            }
            Object o = componentType.getDeclaredConstructor().newInstance();
            cmap.put(((Component) o).getName(), o);
            for (Field f : o.getClass().getDeclaredFields()) {
                Class[] inter = f.getType().getInterfaces();
                for (Class i : inter) {
                    if (i.getName().equals(Component.class.getName())) {
                        f.setAccessible(true);
                        Component co = (Component) cmap.entrySet().stream().filter(c -> c.getValue().getClass().getName().equals(f.getType().toString().substring(6))).limit(1).collect(Collectors.toList()).get(0).getValue();
                        f.set(o, co);
                    }
                }
            }
//            dealConnections(componentType);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

//    private <T> void dealConnections(Class<T> componentType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        Object o = componentType.getDeclaredConstructor().newInstance();
//        cmap.put(((Component) o).getName(), o);
//        for (Field f : o.getClass().getDeclaredFields()) {
//            Class[] inter = f.getType().getInterfaces();
//            for (Class i : inter) {
//                if (i.getName().equals(Component.class.getName())) {
//                    f.setAccessible(true);
//                    Component co = cmap.entrySet().stream().filter(c -> c.getValue().getClass().getName().equals(f.getType().toString().substring(6))).limit(1).collect(Collectors.toList()).get(0).getValue;
//                    f.set(o, co);
//                }
//            }
//        }
//    }
}

