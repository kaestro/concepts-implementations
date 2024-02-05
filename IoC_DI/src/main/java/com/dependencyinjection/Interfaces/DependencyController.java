package com.dependencyinjection.Interfaces;

import java.util.Map;

// DependencyController 인터페이스 정의
public interface DependencyController<T> {
    // 주어진 키와 의존성을 주입합니다.
    void injectDependency(String key, T dependency);

    // 주어진 키에 해당하는 의존성을 제거하고 반환합니다.
    T removeDependency(String key);

    // 주어진 키에 해당하는 의존성이 주입되었는지 확인합니다.
    boolean isDependencyInjected(String key);

    // 주어진 키에 해당하는 의존성을 반환합니다.
    T getDependency(String key);

    // 모든 의존성을 제거합니다.
    void clearDependencies();

    // 현재 주입된 모든 의존성을 반환합니다.
    Map<String, T> getDependencies();

    // 현재 주입된 의존성의 수를 반환합니다.
    int getDependencyCount();
}
