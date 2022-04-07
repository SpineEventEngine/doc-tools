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

import io.spine.annotation.Internal;
import jdk.javadoc.doclet.DocletEnvironment;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import java.util.function.Predicate;

/**
 * Tests if an {@link Element} represents a program element annotated with {@link Internal}
 * annotation.
 *
 * Excludes all program elements including packages and their subpackages.
 */
final class Filter implements Predicate<Element> {

    private final AnnotationCheck<Class<Internal>> internalAnnotation =
            new AnnotationCheck<>(Internal.class);

    private final Packages excludedPackages;
    private final Elements elementUtils;

    Filter(DocletEnvironment docletEnv) {
        var packagesCollector = new PackagesCollector(internalAnnotation);

        this.excludedPackages = packagesCollector.collect(docletEnv);
        this.elementUtils = docletEnv.getElementUtils();
    }

    @Override
    public boolean test(Element el) {
        return internalAnnotation.test(el) || inExclusions(el);
    }

    /**
     * Tells if the package of the passed element is one of the {@link #excludedPackages},
     * or is a sub-package of one of them.
     */
    private boolean inExclusions(Element el) {
        var enclosingPackage = elementUtils.getPackageOf(el);

        if (enclosingPackage == null) {
            return false;
        }

        return excludedPackages.contains(enclosingPackage);
    }
}
