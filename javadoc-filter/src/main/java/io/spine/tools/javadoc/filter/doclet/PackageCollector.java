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
import javax.lang.model.element.TypeElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Collects {@linkplain jdk.javadoc.doclet.DocletEnvironment}s that match the specified {@linkplain
 * AnnotationCheck}.
 */
@Immutable
final class PackageCollector {

    private final AnnotationCheck<?> check;

    PackageCollector(AnnotationCheck<?> check) {
        this.check = check;
    }

    /**
     * Collects {@linkplain PackageElement package documentation}s from
     * the {@linkplain DocletEnvironment#getSpecifiedElements()}  packages} of the passed
     * documentation root,
     * and packages of the {@linkplain DocletEnvironment#getSpecifiedElements()}  classes} of this
     * root.
     *
     * @return a set sorted by the package {@linkplain PackageElement#getQualifiedName()}  names}
     */
    Set<PackageElement> collect(DocletEnvironment root) {
        var packages = newSortedSet();
        for (var el : root.getSpecifiedElements()) {
            PackageElement currentPackage = null;

            if (el.getKind() == ElementKind.PACKAGE) {
                currentPackage = (PackageElement) el;
            }

            if (el.getKind() == ElementKind.CLASS) {
                currentPackage = Packages.enclosingPackage(el);
            }

            if (currentPackage != null &&
                    (check.test(currentPackage) || isSubpackage(currentPackage, packages))) {
                packages.add(currentPackage);
            }

            if (check.test(containingPackage(currentPackage))) {
                packages.add(containingPackage(currentPackage));
            }
        }

        return packages;
    }

    /**
     * Creates a new sorted set for storing the gathered data.
     */
    private static Set<PackageElement> newSortedSet() {
        return new TreeSet<>(new PackageElementComparator());
    }

    /**
     * Creates a new sorted set initialized with the passed content.
     */
    private static Set<PackageElement> newSortedSet(Set<PackageElement> content) {
        Set<PackageElement> result = newSortedSet();
        result.addAll(content);
        return result;
    }

    private Set<PackageElement> collect(TypeElement[] classes) {
        Set<PackageElement> packages = packagesOf(classes);
        Set<PackageElement> allCollected = newSortedSet(packages);
        for (TypeElement cls : classes) {
            if (isSubpackage(containingPackage(cls), packages)) {
                allCollected.add(containingPackage(cls));
            }
        }
        return allCollected;
    }

    private Collection<PackageElement> collect(PackageElement[] packages) {
        Set<PackageElement> basePackages = packagesOf(packages);
        Set<PackageElement> allCollected = newSortedSet(basePackages);
        for (PackageElement pckg : packages) {
            if (isSubpackage(pckg, basePackages)) {
                allCollected.add(pckg);
            }
        }
        return allCollected;
    }

    private Set<PackageElement> packagesOf(PackageElement[] packages) {
        Set<PackageElement> result = newSortedSet();
        for (PackageElement packageDoc : packages) {
            if (check.test(packageDoc)) {
                result.add(packageDoc);
            }
        }
        return result;
    }

    private Set<PackageElement> packagesOf(TypeElement[] classes) {
        Set<PackageElement> result = newSortedSet();
        for (TypeElement cls : classes) {
            if (check.test(containingPackage(cls))) {
                result.add(containingPackage(cls));
            }
        }
        return result;
    }

    private PackageElement containingPackage(Element el) {
        Element p = el;

        while (p.getKind() != ElementKind.PACKAGE) {
            p = p.getEnclosingElement();
        }

        return (PackageElement) p;
    }

    private static boolean isSubpackage(PackageElement target, Iterable<PackageElement> packages) {
        String targetName = target.getQualifiedName()
                                  .toString();
        for (PackageElement pckg : packages) {
            if (targetName.startsWith(pckg.getQualifiedName()
                                          .toString())) {
                return true;
            }
        }
        return false;
    }

    private static class PackageElementComparator implements Comparator<PackageElement>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(PackageElement o1, PackageElement o2) {
            return o1.getQualifiedName()
                     .toString()
                     .compareTo(o2.getQualifiedName()
                                  .toString());
        }
    }
}
