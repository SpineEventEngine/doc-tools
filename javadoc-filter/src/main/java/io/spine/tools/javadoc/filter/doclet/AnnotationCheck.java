/*
 * Copyright 2022, TeamDev. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.tools.javadoc.filter.doclet;

import com.google.errorprone.annotations.Immutable;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.QualifiedNameable;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Provides the method to check if a program element has specified annotation.
 *
 * @param <C>
 *         the class of an annotation to be checked against
 */
@Immutable
final class AnnotationCheck<C extends Class<? extends Annotation>> {

    private final C annotationClass;

    AnnotationCheck(C annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * Tests if the piece of code represented by the provided object is annotated with
     * the {@link C annotation}.
     *
     * @return {@code true} if provided object is annotated with the annotation. Otherwise,
     *         returns {@code false}.
     */
    boolean test(AnnotatedConstruct annotatedConstruct) {
        return isAnnotationPresent(annotatedConstruct.getAnnotationMirrors());
    }

    private boolean isAnnotationPresent(Collection<? extends AnnotationMirror> annotations) {
        return annotations.stream()
                .anyMatch(this::matchesName);
    }

    private boolean matchesName(AnnotationMirror annotationMirror) {
        var annotation = (QualifiedNameable) annotationMirror.getAnnotationType()
                                                             .asElement();
        return annotation.getQualifiedName()
                         .toString()
                         .equals(annotationClass.getName());
    }
}

