package com.dependencyinjection;

import java.util.Map;

public class DependencyInjectorTest {
    private static class TestInjector extends DependencyInjector<Object> {
        @Override
        public void injectDependency(String key, Object dependency) {
            super.injectDependency(key, dependency);
        }

        @Override
        public Object getDependency(String key) {
            return super.getDependency(key);
        }

        @Override
        public void create(String key) {
            injectDependency(key, new Object());
        }

        @Override
        public Object createDependency(String key) {
            Object dependency = new Object();
            injectDependency(key, dependency);
            setScope(key, "Created");
            return dependency;
        }


    }

    public static void testInjectAndGetDependency() {
        TestInjector injector = new TestInjector();
        Object obj = new Object();
        injector.injectDependency("test", obj);
        if (injector.getDependency("test") != obj) {
            throw new AssertionError("Test failed: getDependency did not return the object that was injected.");
        }
    }

    public static void testCreateAndDispose() {
        TestInjector injector = new TestInjector();
        String key = "test";

        // Test create
        injector.create(key);
        if (injector.getDependency(key) == null) {
            throw new AssertionError("Test failed: create did not create a dependency.");
        }

        // Test dispose
        injector.dispose(key);
        boolean exceptionThrown = true;
        try {
            injector.getDependency(key);
            throw new AssertionError("Test failed: dispose did not remove the dependency.");
        } catch (IllegalArgumentException e) {
            exceptionThrown = false;
        }

        if (exceptionThrown) {
            throw new AssertionError("Test failed: dispose did not remove the dependency."); 
        }
    }

    public static void testIsDependencyInjected() {
        TestInjector injector = new TestInjector();
        String key = "test";

        // Test after create
        injector.create(key);
        if (!injector.isDependencyInjected(key)) {
            throw new AssertionError("Test failed: isDependencyInjected returned false after create.");
        }

        // Test after dispose
        injector.dispose(key);
        if (injector.isDependencyInjected(key)) {
            throw new AssertionError("Test failed: isDependencyInjected returned true after dispose.");
        }
    }

    public static void testClearDependencies() {
        TestInjector injector = new TestInjector();
        String key1 = "test1";
        String key2 = "test2";

        // Inject dependencies
        injector.create(key1);
        injector.create(key2);
        if (injector.getDependencyCount() != 2) {
            throw new AssertionError("Test failed: getDependencyCount did not return the correct count after create.");
        }

        // Test clearDependencies
        injector.clearDependencies();
        if (injector.getDependencyCount() != 0) {
            throw new AssertionError("Test failed: getDependencyCount did not return 0 after clearDependencies.");
        }
    }

    public static void testGetDependencies() {
        TestInjector injector = new TestInjector();
        String key1 = "test1";
        String key2 = "test2";

        // Inject dependencies
        injector.create(key1);
        injector.create(key2);

        // Test getDependencies
        Map<String, Object> dependencies = injector.getDependencies();
        if (dependencies.size() != 2 || !dependencies.containsKey(key1) || !dependencies.containsKey(key2)) {
            throw new AssertionError("Test failed: getDependencies did not return the correct dependencies.");
        }
    }

    public static void testGetDependencyCount() {
        TestInjector injector = new TestInjector();
        String key1 = "test1";
        String key2 = "test2";

        // Test getDependencyCount with no dependencies
        if (injector.getDependencyCount() != 0) {
            throw new AssertionError("Test failed: getDependencyCount did not return 0 with no dependencies.");
        }

        // Inject dependencies and test getDependencyCount
        injector.create(key1);
        injector.create(key2);
        if (injector.getDependencyCount() != 2) {
            throw new AssertionError("Test failed: getDependencyCount did not return the correct count after create.");
        }
    }
    public static void testCreateAndDestroyDependency() {
        TestInjector injector = new TestInjector();
        String key = "test";

        // Test createDependency
        injector.createDependency(key);
        if (!"Created".equals(injector.getDependencyStatus(key))) {
            throw new AssertionError("Test failed: createDependency did not create a dependency.");
        }

        // Test destroyDependency
        injector.destroyDependency(key);
        if (!"Destroyed".equals(injector.getDependencyStatus(key))) {
            throw new AssertionError("Test failed: destroyDependency did not destroy the dependency.");
        }
    }

    public static void testGetDependencyStatus() {
        TestInjector injector = new TestInjector();
        String key = "test";

        boolean exceptionThrown = false;
        // Test getDependencyStatus with no dependency
        try {
            injector.getDependencyStatus(key);
            throw new AssertionError("Test failed: getDependencyStatus did not throw an exception with no dependency.");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) {
            throw new AssertionError("Test failed: getDependencyStatus did not throw an exception with no dependency.");
        }

        // Inject a dependency and test getDependencyStatus
        injector.createDependency(key);
        if (!"Created".equals(injector.getDependencyStatus(key))) {
            throw new AssertionError("Test failed: getDependencyStatus did not return the correct status after createDependency.");
        }
    }



    public static void main(String[] args) {
        testInjectAndGetDependency();
        testCreateAndDispose();
        testIsDependencyInjected();
        testClearDependencies();
        testGetDependencies();
        testGetDependencyCount();
        testCreateAndDestroyDependency();
        testGetDependencyStatus();
        System.out.println("All tests passed.");
    }
}