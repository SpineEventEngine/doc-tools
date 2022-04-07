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

import javax.lang.model.element.PackageElement;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Keeps a set of {@linkplain PackageElement packages} sorted based on their
 * {@linkplain PackageElement#getQualifiedName() fully qualified names} and provides the necessary
 * method to check whether a package is in the set.
 */
final class Packages {

    private static final class PackageElementComparator
            implements Comparator<PackageElement>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(PackageElement p1, PackageElement p2) {
            return p1.getQualifiedName()
                     .toString()
                     .compareTo(p2.getQualifiedName()
                                  .toString());
        }
    }

    private final Set<PackageElement> packages;

    Packages() {
        this.packages = new TreeSet<>(new PackageElementComparator());
    }

    /**
     * Adds the package to the set.
     *
     * @param packageElement
     *         package to be added to the set
     * @throws NullPointerException
     *         in case the provided package is null
     */
    void add(PackageElement packageElement) {
        checkNotNull(packageElement, "The package cannot be null");

        this.packages.add(packageElement);
    }

    /**
     * Tells if the package is present in the set or is a subpackage of any in the set.
     *
     * <p>This is done by comparing the fully qualified name of the package provided with
     * those in the set. If there is a package whose name is a prefix of the provided package's
     * {@linkplain PackageElement#getQualifiedName() fully qualified name} then it is considered
     * that the provided package is a subpackage, so naturally is present.
     *
     * @param packageElement
     *         package to be checked for presence in the set
     * @return {@code true} if the package is present or is a subpackage of any in the set.
     *         Otherwise, returns {@code false}
     * @throws NullPointerException
     *         in case the provided package is null
     */
    boolean contains(PackageElement packageElement) {
        checkNotNull(packageElement, "The package cannot be null");

        var targetName = packageElement.getQualifiedName()
                                       .toString();

        for (var p : packages) {
            if (targetName.startsWith(p.getQualifiedName()
                                       .toString())) {
                return true;
            }
        }

        return false;
    }
}
