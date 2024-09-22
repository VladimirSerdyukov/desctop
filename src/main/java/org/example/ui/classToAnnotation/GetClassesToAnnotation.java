package org.example.ui.classToAnnotation;

import org.example.ui.menu.TupeMenu;
import org.reflections.Reflections;
import java.util.ArrayList;
import java.util.Set;

public class GetClassesToAnnotation {
    public static ArrayList<Class<?>> reflectionGetClass() {

        Reflections reflections = new Reflections("org.example.ui.menu");
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(TupeMenu.class);

        ArrayList<Class<?>> list = new ArrayList<Class<?>>(set);
        return list;
    }
}
