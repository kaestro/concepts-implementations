package main.java.com.dependencyinjection;

import main.java.com.dependencyinjection.Interfaces.Dependency;

import java.util.HashMap;
import java.util.Map;

public class MyClass<T> implements Dependency<T> {
    private Map<String, T> dependencies = new HashMap<>();

    @Override
    public void injectDependency(String key, T dependency) {
        dependencies.put(key, dependency);
    }

    @Override
    public boolean isDependencyInjected(String key) {
        return dependencies.containsKey(key);
    }

    @Override
    public T getDependency(String key) {
        return dependencies.get(key);
    }
}