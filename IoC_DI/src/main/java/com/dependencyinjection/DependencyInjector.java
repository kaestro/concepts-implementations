package com.dependencyinjection;

import com.dependencyinjection.Interfaces.DependencyController;
import com.dependencyinjection.Interfaces.DependencyLifeCycleManager;

import java.util.HashMap;
import java.util.Map;

public abstract class DependencyInjector<T> implements DependencyController<T>, DependencyLifeCycleManager<T> {
    /**
     * 의존성을 관리하기 위한 맵입니다.
     * 키는 의존성의 이름이며, 값은 의존성 객체입니다.
     */
    private Map<String, T> dependencies = new HashMap<>();
    /**
     * 객체의 생명 주기를 관리하기 위한 맵입니다.
     * 같은 스코프 내의 객체들은 같은 생명 주기를 가지게 됩니다
     */
    private Map<String, String> scopes = new HashMap<>();

    /**
     * 주어진 키와 의존성을 이용하여 의존성을 주입합니다.
     *
     * @param key        의존성의 키
     * @param dependency 주입할 의존성
     * @throws IllegalArgumentException 키 또는 의존성이 null인 경우 발생
     */
   @Override
    public void injectDependency(String key, T dependency) {
        if (key == null || dependency == null) {
            throw new IllegalArgumentException("key or Dependency cannot be null");
        }
        dependencies.put(key, dependency);
    }

    /**
     * 주어진 키에 해당하는 의존성을 제거합니다.
     *
     * @param key 의존성의 키
     * @throws IllegalArgumentException 주어진 키에 해당하는 의존성이 없는 경우 발생
     */
    @Override
    public boolean isDependencyInjected(String key) {
        return dependencies.containsKey(key);
    }

    /**
     * 주어진 키와 의존성을 이용하여 의존성을 주입합니다.
     *
     * @param key        의존성의 키
     * @param dependency 주입할 의존성
     * @throws IllegalArgumentException 키 또는 의존성이 null인 경우 발생
     */
    @Override
    public T getDependency(String key) {
        if (!dependencies.containsKey(key)) {
            throw new IllegalArgumentException("No such dependency: " + key);
        }
        return dependencies.get(key);
    }

    /**
     * 주어진 키에 해당하는 의존성을 제거합니다.
     *
     * @param key 의존성의 키
     * @return 제거된 의존성. 키에 해당하는 의존성이 없는 경우 null을 반환합니다.
     */
    @Override
    public T removeDependency(String key) {
        return dependencies.remove(key);
    }

    /**
     * 주어진 키와 범위를 이용하여 범위를 설정합니다.
     *
     * @param key   범위의 키
     * @param scope 설정할 범위
     * @throws IllegalArgumentException 키 또는 범위가 null인 경우 발생
     * 
     * TODO
     * scope를 자동화할 필요성이 있는가?
     */
    public void setScope(String key, String scope) {
        if (key == null || scope == null) {
            throw new IllegalArgumentException("Key or scope cannot be null");
        }
        scopes.put(key, scope);
    }

    /**
     * 주어진 키에 해당하는 범위를 반환합니다.
     *
     * @param key 범위의 키
     * @return 주어진 키에 해당하는 범위
     */
    public String getScope(String key) {
        return scopes.get(key);
    }

    /**
     * 주어진 키를 이용하여 객체를 생성합니다.
     * 이 메서드는 추상 메서드로, 하위 클래스에서 구현해야 합니다.
     *
     * @param key 객체의 키
     */
    public abstract void create(String key); 

    /**
     * 주어진 키에 해당하는 의존성을 제거합니다.
     *
     * @param key 의존성의 키
     * @throws IllegalArgumentException 주어진 키에 해당하는 의존성이 없는 경우 발생
     */
    public void dispose(String key) {
        if (!dependencies.containsKey(key)) {
            throw new IllegalArgumentException("No such dependency: " + key);
        }
        dependencies.remove(key);
    }

    /**
     * 모든 의존성을 제거합니다.
     */
    @Override
    public void clearDependencies() {
        dependencies.clear();
    }

    /**
     * 현재 주입된 모든 의존성을 반환합니다.
     *
     * @return 현재 주입된 모든 의존성
     */
    @Override
    public Map<String, T> getDependencies() {
        return dependencies;
    }

    /**
     * 현재 주입된 의존성의 수를 반환합니다.
     *
     * @return 현재 주입된 의존성의 수
     */
    @Override
    public int getDependencyCount() {
        return dependencies.size();
    }

    /**
     * 주어진 키와 관련된 의존성을 생성합니다.
     *
     * @param key 의존성의 키
     * @return 생성된 의존성
     */
    @Override
    public abstract T createDependency(String key); 
    /**
     * 주어진 키와 관련된 의존성을 소멸시킵니다.
     *
     * @param key 소멸시킬 의존성의 키
     */
    @Override
    public void destroyDependency(String key) {
        dependencies.remove(key);
        scopes.put(key, "Destroyed");
    }

    /**
     * 주어진 키와 관련된 의존성의 상태를 반환합니다.
     *
     * @param key 상태를 확인할 의존성의 키
     * @return 의존성의 상태
     */
    @Override
    public String getDependencyStatus(String key) {
        // 의존성 상태 반환 로직 구현
        if (!scopes.containsKey(key)) {
            throw new IllegalArgumentException("No such dependency: " + key);
        }
        return scopes.get(key);
    }
}