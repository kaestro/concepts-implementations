package com.dependencyinjection.Interfaces;

/**
 * 이 인터페이스는 의존성의 생명주기를 관리하는 메서드를 정의합니다.
 * 이는 의존성의 생성, 소멸, 그리고 상태 확인을 위한 메서드를 포함합니다.
 *
 * @param <T> 관리할 의존성의 타입
 */
public interface DependencyLifeCycleManager<T> {
    /**
     * 주어진 키와 관련된 의존성을 생성합니다.
     *
     * @param key 의존성의 키
     * @return 생성된 의존성
     */
    T createDependency(String key);

    /**
     * 주어진 키와 관련된 의존성을 소멸시킵니다.
     *
     * @param key 소멸시킬 의존성의 키
     */
    void destroyDependency(String key);

    /**
     * 주어진 키와 관련된 의존성의 상태를 반환합니다.
     *
     * @param key 상태를 확인할 의존성의 키
     * @return 의존성의 상태
     */
    String getDependencyStatus(String key);
}