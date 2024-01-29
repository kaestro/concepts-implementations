package main.java.com.dependencyinjection.Interfaces;

// Dependency 인터페이스 정의
public interface Dependency<T> {
    void injectDependency(String key, T dependency);
    boolean isDependencyInjected(String key);
    T getDependency(String key);
}
