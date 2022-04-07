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
import jdk.javadoc.doclet.DocletEnvironment;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;

/**
 * Collects {@linkplain PackageElement}s that match the specified {@linkplain AnnotationCheck}.
 */
@Immutable
final class PackagesCollector {

    private final AnnotationCheck<?> check;

    PackagesCollector(AnnotationCheck<?> check) {
        this.check = check;
    }

    /**
     * Collects {@linkplain PackageElement package documentation}s about
     * {@linkplain DocletEnvironment#getSpecifiedElements() specified}  packages and
     * packages of the specified classes from passed doclet environment. Only packages that
     * pass {@linkplain AnnotationCheck the check} are included in the result.
     *
     * @param docletEnv
     *         The doclet environment from which packages should be collected
     * @return a {@link Packages} object, which keeps a sorted set of packages based on their
     *         {@linkplain PackageElement#getQualifiedName() fully qualified names}
     */
    Packages collect(DocletEnvironment docletEnv) {
        var packages = new Packages();
        var utils = docletEnv.getElementUtils();

        for (var el : docletEnv.getSpecifiedElements()) {
            PackageElement currentPackage = null;

            if (isPackage(el)) {
                currentPackage = (PackageElement) el;
            }

            if (isClass(el)) {
                currentPackage = utils.getPackageOf(el);
            }

            if (currentPackage != null &&
                    (check.test(currentPackage) || packages.contains(currentPackage))) {
                packages.add(currentPackage);
            }
        }

        return packages;
    }

    private boolean isClass(Element el) {
        return el.getKind()
                 .isClass();
    }

    private boolean isPackage(Element el) {
        return el.getKind() == ElementKind.PACKAGE;
    }
}
