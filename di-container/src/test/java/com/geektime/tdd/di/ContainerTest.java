package com.geektime.tdd.di;

import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 容器测试
 *
 * @author tengxq
 */
public class ContainerTest {

    Context context;



    @BeforeEach
    void setup() {
        context = new Context();
    }

    @Nested
    class ComponentConstruction {
        // instance
        @Test
        void should_bind_type_to_a_specific_instance() {
            Component instance = new Component() {};
            context.bind(Component.class, instance);
            assertSame(instance, context.get(Component.class));
        }
        // TODO abstract class
        // TODO interface

        @Nested
        class ConstructorInjection {
            // No args constructor
            @Test
            void should_bind_type_to_a_class_with_default_constructor() {
                context.bind(Component.class, ComponentWithDefaultConstructor.class);
                Component instance = context.get(Component.class);
                assertNotNull(instance);
                assertTrue(instance instanceof ComponentWithDefaultConstructor);
            }
            //  with dependencies
            @Test
            void should_bind_type_to_a_class_with_inject_constructor() {
                Dependency dependence = new Dependency() {};
                context.bind(Component.class, ComponentWithInjectConstructor.class);
                context.bind(Dependency.class, dependence);

                Component instance = context.get(Component.class);
                assertNotNull(instance);
                assertSame(dependence, ((ComponentWithInjectConstructor)instance).dependency);
            }
            // A -> B -> C
            @Test
            void should_bind_type_to_a_class_with_transitive_dependencies() {
                context.bind(Component.class, ComponentWithInjectConstructor.class);
                context.bind(Dependency.class, DependencyWithInjectConsturctor.class);
                context.bind(String.class, "indirect dependency");
                Component instance = context.get(Component.class);
                assertNotNull(instance);
                Dependency dependency = ((ComponentWithInjectConstructor) instance).dependency;
                assertNotNull(dependency);
                assertEquals("indirect dependency", ((DependencyWithInjectConsturctor)dependency).getDependency());
            }
        }

        @Nested
        public class FiledInjection {

        }

        @Nested
        public class MethodInjection {

        }
    }

    @Nested
    public class DependenciesSelection {

    }

    @Nested
    public class LifecycleManagement {

    }

    interface Component {

    }

    interface Dependency {

    }

    static class ComponentWithDefaultConstructor implements Component {
        public ComponentWithDefaultConstructor() {
        }
    }

    static class ComponentWithInjectConstructor implements Component {

        public Dependency dependency;

        @Inject
        public ComponentWithInjectConstructor(Dependency dependency) {
            this.dependency = dependency;
        }
    }

    static class DependencyWithInjectConsturctor implements Dependency {
        private String dependency;

        @Inject
        public DependencyWithInjectConsturctor(String dependency) {
            this.dependency = dependency;
        }

        public String getDependency() {
            return dependency;
        }
    }
}
