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

import com.google.common.collect.ImmutableSet;
import io.spine.annotation.Internal;
import jdk.javadoc.doclet.DocletEnvironment;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Tests if a {@code ProgramElementDoc} represents a Java code annotated
 * with {@link Internal} annotation.
 *
 * <p>Excludes all program elements including packages and their subpackages.
 */
final class Filter implements Predicate<Element> {

    private final AnnotationCheck<Class<Internal>> internalAnnotation =
            new AnnotationCheck<>(Internal.class);

    /**
     * Packages to be excluded in the passed documentation root.
     */
    private final Set<PackageElement> excludedPackages;

    Filter(DocletEnvironment root) {
        PackageCollector packageCollector = new PackageCollector(internalAnnotation);
        Set<PackageElement> collected = packageCollector.collect(root);
        this.excludedPackages = ImmutableSet.copyOf(collected);
    }

    @Override
    public boolean test(Element element) {
        return internalAnnotation.test(element) || inExclusions(element);
    }

    /**
     * Tells if a package of the passed element is one of the {@link #excludedPackages},
     * or is a sub-package of one of them.
     */
    private boolean inExclusions(Element element) {
        String packageName = containingPackage(element).getQualifiedName()
                                                       .toString();
        for (PackageElement exclusion : excludedPackages) {
            if (packageName.startsWith(exclusion.getQualifiedName()
                                                .toString())) {
                return true;
            }
        }
        return false;
    }

    private PackageElement containingPackage(Element el) {
        Element p = el;

        while (p.getKind() != ElementKind.PACKAGE) {
            p = p.getEnclosingElement();
        }

        return (PackageElement) p;
    }
}
