package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>[] initialConfigClasses) {
        processConfigClasses(initialConfigClasses);
    }

    public AppComponentsContainerImpl(String configPackageName) {
        Reflections reflections = new Reflections(configPackageName, new TypeAnnotationsScanner());
        Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class, true);
        processConfigClasses(allClasses.toArray(new Class[0]));
    }

    private void processConfigClasses(Class<?>[] configClasses){
        Arrays.stream(configClasses).sorted((o1, o2) -> {
            checkConfigClass(o1);
            checkConfigClass(o2);
            AppComponentsContainerConfig a1 = o1.getAnnotation(AppComponentsContainerConfig.class);
            AppComponentsContainerConfig a2 = o2.getAnnotation(AppComponentsContainerConfig.class);
            return Integer.compare(a1.order(), a2.order());
        }).forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        try {
            Object configInstance = configClass.getDeclaredConstructor().newInstance();
            List<Method> appComponentsFactoryMethods = getAppComponentsFactoryMethods(configClass);

            for (Method factoryMethod : appComponentsFactoryMethods) {
                processFactoryMethod(configInstance, factoryMethod);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processFactoryMethod(Object configInstance, Method factoryMethod) throws InvocationTargetException, IllegalAccessException {
        AppComponent appComponentDesc = factoryMethod.getAnnotation(AppComponent.class);
        Object[] methodParams = getFactoryMethodParams(factoryMethod, appComponentDesc);
        Object appComponent = factoryMethod.invoke(configInstance, methodParams);
        appComponents.add(appComponent);
        appComponentsByName.put(appComponentDesc.name(), appComponent);
    }

    private Object[] getFactoryMethodParams(Method factoryMethod, AppComponent appComponentDesc) {
        Class<?>[] parameterTypes = factoryMethod.getParameterTypes();
        Object[] methodParams = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            methodParams[i] = getAppComponent(parameterTypes[i]);
            if (methodParams[i] == null) {
                throw new RuntimeException(String.format("No required component with type %s found to create %s",
                        parameterTypes[i].getName(), appComponentDesc.name()));
            }
        }
        return methodParams;
    }

    private List<Method> getAppComponentsFactoryMethods(Class<?> configClass) {
        Method[] methods = configClass.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted((o1, o2) -> {
                    AppComponent a1 = o1.getAnnotation(AppComponent.class);
                    AppComponent a2 = o2.getAnnotation(AppComponent.class);
                    int res = Integer.compare(a1.order(), a2.order());
                    return res == 0 ? a1.name().compareTo(a2.name()) : res;
                }).collect(Collectors.toList());
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(c -> componentClass.isAssignableFrom(c.getClass())).findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
