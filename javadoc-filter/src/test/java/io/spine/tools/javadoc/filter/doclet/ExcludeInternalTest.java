///*
// * Copyright 2022, TeamDev. All rights reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Redistribution and use in source and/or binary forms, with or without
// * modification, must retain the above copyright notice and the following
// * disclaimer.
// *
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// */
//
//package io.spine.tools.javadoc.filter.doclet;
//
//import com.sun.javadoc.ClassDoc;
//import com.sun.javadoc.MethodDoc;
//import com.sun.javadoc.RootDoc;
//import io.spine.testing.logging.mute.MuteLogging;
//import org.checkerframework.checker.nullness.qual.NonNull;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.io.TempDir;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import static com.google.common.truth.Truth.assertThat;
//import static io.spine.tools.javadoc.filter.doclet.RootDocProxyReceiver.rootDocFor;
//
///**
// * Tests {@link ExcludeInternal}.
// *
// * <p>The source code used by this test suite is located under {@code resources/testsources}.
// */
//@MuteLogging
//@DisplayName("`ExcludeInternal` should")
//class ExcludeInternalTest {
//
//    /** The current directory of the user is where the build is executed. */
//    @SuppressWarnings("AccessOfSystemProperties") // Need to know project path
//    private static final String CURRENT_DIR = System.getProperty("user.dir");
//
//    /** The directory under the source tree with the source code to be used in the tests. */
//    private static final String RESOURCES_DIR = CURRENT_DIR + "/src/test/resources/";
//
//    /** The directory and the package for the source code used for the tests. */
//    private static final String TEST_SOURCES_PACKAGE = "testsources";
//
//    private static final String INTERNAL_PACKAGE = TEST_SOURCES_PACKAGE + ".internal";
//    private static final String INTERNAL_METHOD_CLASS_FILENAME = "InternalMethodClass.java";
//    private static final String INTERNAL_CLASS_FILENAME = "InternalClass.java";
//    private static final String DERIVED_FROM_INTERNAL_CLASS_FILENAME =
//            "DerivedFromInternalClass.java";
//    private static final String NOT_INTERNAL_CLASS_FILENAME = "/notinternal/NotInternalClass.java";
//
//    private Path sourcePath;
//    private Path destination;
//
//    @BeforeEach
//    void createTempDir(@TempDir Path tempDir) {
//        sourcePath = Paths.get(RESOURCES_DIR);
//        destination = tempDir.resolve("javadoc");
//    }
//
//    @NonNull
//    private JavadocArgs createArgs() {
//        JavadocArgs args = new JavadocArgs();
//        args.setSourcePath(sourcePath)
//            .setDestination(destination);
//        return args;
//    }
//
//    /**
//     * Prepends the given source file reference with the
//     * {@linkplain #TEST_SOURCES_PACKAGE package name} used for the source code in these tests.
//     */
//    private static String src(String sourceFile) {
//        return TEST_SOURCES_PACKAGE + '/' + sourceFile;
//    }
//
//    @Test
//    @DisplayName("run standard doclet")
//    void runStandardDoclet() {
//        String[] args = createArgs()
//                .addSource(src(NOT_INTERNAL_CLASS_FILENAME))
//                .build();
//
//        ExcludeInternal.main(args);
//
//        assertThat(Files.exists(destination))
//                .isTrue();
//    }
//
//    @Nested
//    @DisplayName("exclude internal")
//    class Excluding {
//
//        @Test
//        @DisplayName("annotated annotations")
//        void annotatedAnnotations() {
//            String[] args = createArgs()
//                    .addSource(src("InternalAnnotatedAnnotation.java"))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            assertThat(rootDoc.specifiedClasses())
//                    .isEmpty();
//        }
//
//        @Test
//        @DisplayName("internal constructors")
//        void ctors() {
//            String[] args = createArgs()
//                    .addSource(src("InternalCtorClass.java"))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            ClassDoc classDoc = rootDoc.specifiedClasses()[0];
//            assertThat(classDoc.constructors())
//                    .isEmpty();
//        }
//
//        @Test
//        @DisplayName("fields")
//        void fields() {
//            String[] args = createArgs()
//                    .addSource(src("InternalFieldClass.java"))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            ClassDoc classDoc = rootDoc.specifiedClasses()[0];
//            assertThat(classDoc.fields())
//                    .isEmpty();
//        }
//
//        @Test
//        @DisplayName("methods")
//        void methods() {
//            String[] args = createArgs()
//                    .addSource(src(INTERNAL_METHOD_CLASS_FILENAME))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            ClassDoc classDoc = rootDoc.specifiedClasses()[0];
//            assertThat(classDoc.methods())
//                    .isEmpty();
//        }
//
//        /**
//         * Tests that if a package is marked {@code @Internal} its content is also
//         * deemed {@code @Internal}, and hence would be excluded.
//         */
//        @Test
//        @DisplayName("package content")
//        void packageContent() {
//            String[] args = createArgs()
//                    .addSource(src("/internal/InternalPackageClass.java"))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            assertThat(rootDoc.specifiedClasses())
//                    .isEmpty();
//        }
//
//        @Test
//        @DisplayName("classes")
//        void classes() {
//            String[] args = createArgs()
//                    .addSource(src(INTERNAL_CLASS_FILENAME))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            assertThat(rootDoc.specifiedClasses())
//                    .isEmpty();
//        }
//
//        @Test
//        @DisplayName("interfaces")
//        void interfaces() {
//            String[] args = createArgs()
//                    .addSource(src("InternalAnnotatedInterface.java"))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            assertThat(rootDoc.specifiedClasses())
//                    .isEmpty();
//        }
//
//        @Test
//        @DisplayName("enums")
//        void enums() {
//            String[] args = createArgs()
//                    .addSource(src("InternalEnum.java"))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            assertThat(rootDoc.specifiedClasses())
//                    .isEmpty();
//        }
//    }
//
//    @Test
//    @DisplayName("exclude only from internal subpackages")
//    void excludeOnlyFromInternalSubpackages() {
//        String[] args = createArgs()
//                .addSource(src("/internal/subinternal/SubInternalPackageClass.java"))
//                .addSource(src(NOT_INTERNAL_CLASS_FILENAME))
//                .addPackage(INTERNAL_PACKAGE)
//                .addPackage(TEST_SOURCES_PACKAGE + ".notinternal")
//                .build();
//
//        RootDoc rootDoc = rootDocFor(args);
//
//        assertThat(rootDoc.specifiedClasses())
//                .hasLength(1);
//    }
//
//    /**
//     * Tests that a class annotated as {@code io.grpc.Internal} (which is foreign
//     * annotation), is not excluded from our documentation.
//     */
//    @Test
//    @DisplayName("not use `@Internal` annotation from other libraries or frameworks")
//    void foreignAnnotation() {
//        String[] args = createArgs()
//                .addSource(src("GrpcInternalAnnotatedClass.java"))
//                .build();
//
//        RootDoc rootDoc = rootDocFor(args);
//
//        assertThat(rootDoc.specifiedClasses())
//                .hasLength(1);
//    }
//
//    @Nested
//    @DisplayName("should handle")
//    class Handle {
//
//        @Test
//        @DisplayName("compareTo() on a class derived from internal")
//        void handleDerivedClasses() {
//            String[] args = createArgs()
//                    .addSource(src(INTERNAL_CLASS_FILENAME))
//                    .addSource(src(DERIVED_FROM_INTERNAL_CLASS_FILENAME))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            // invoke compareTo to be sure, that proxy unwrapping
//            // doest not expose object passed to compareTo method
//            ClassDoc classDoc = rootDoc.specifiedClasses()[0];
//            ClassDoc anotherClassDoc = classDoc.superclass();
//            classDoc.compareTo(anotherClassDoc);
//
//            assertThat(rootDoc.specifiedClasses())
//                    .hasLength(1);
//        }
//
//        @Test
//        @DisplayName("overridden methods")
//        void handleOverriddenMethod() {
//            String[] args = createArgs()
//                    .addSource(src(INTERNAL_METHOD_CLASS_FILENAME))
//                    .addSource(src("OverridesInternalMethod.java"))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            // Invoke overrides to be sure, that proxy unwrapping
//            // doest not expose overridden method.
//            ClassDoc overridesInternalMethod =
//                    rootDoc.classNamed(TEST_SOURCES_PACKAGE + ".OverridesInternalMethod");
//            MethodDoc methodDoc = overridesInternalMethod.methods()[0];
//            MethodDoc overriddenMethod = methodDoc.overriddenMethod();
//            methodDoc.overrides(overriddenMethod);
//
//            ClassDoc classDoc = rootDoc.classNamed(TEST_SOURCES_PACKAGE + ".InternalMethodClass");
//            assertThat(classDoc.methods())
//                    .isEmpty();
//        }
//
//        @Test
//        @DisplayName("subclassOf() invocation")
//        void subclassOf() {
//            String[] args = createArgs()
//                    .addSource(src(INTERNAL_CLASS_FILENAME))
//                    .addSource(src(DERIVED_FROM_INTERNAL_CLASS_FILENAME))
//                    .build();
//
//            RootDoc rootDoc = rootDocFor(args);
//
//            // Invoke subclassOf to be sure, that proxy unwrapping
//            // doest not expose parent internal class.
//            ClassDoc classDoc = rootDoc.specifiedClasses()[0];
//            ClassDoc superclass = classDoc.superclass();
//            classDoc.subclassOf(superclass);
//
//            assertThat(rootDoc.specifiedClasses())
//                    .hasLength(1);
//        }
//    }
//}
