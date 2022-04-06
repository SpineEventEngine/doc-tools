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

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.QualifiedNameable;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Provides the methods to check if a program element has specified annotation.
 *
 * @param <C>
 *         the type of an annotation to check
 */
@Immutable
final class AnnotationCheck<C extends Class<? extends Annotation>> {

    private final C annotationClass;

    AnnotationCheck(C annotationClass) {
        this.annotationClass = annotationClass;
    }

    boolean test(Element doc) {
        return isAnnotationPresent(doc.getAnnotationMirrors());
    }

    boolean test(PackageElement doc) {
        return isAnnotationPresent(doc.getAnnotationMirrors());
    }

    private boolean isAnnotationPresent(Collection<? extends AnnotationMirror> annotations) {
        return annotations.stream()
                .anyMatch(this::matchesName);
    }

    private boolean matchesName(AnnotationMirror annotation) {
        var annotationElement = (QualifiedNameable) annotation.getAnnotationType()
                                                              .asElement();
        return annotationElement.getQualifiedName()
                                .toString()
                                .equals(annotationClass.getName());
    }
}

