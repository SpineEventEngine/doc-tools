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

import com.sun.source.util.DocTrees;
import jdk.javadoc.doclet.DocletEnvironment;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.util.Set;

final class FilteringDocletEnvironment implements DocletEnvironment {
    private final Filter filter;
    private final DocletEnvironment docletEnvironment;

    FilteringDocletEnvironment(DocletEnvironment environment) {
        this.filter = new Filter(environment);
        docletEnvironment = environment;
    }

    @Override
    public Set<? extends Element> getSpecifiedElements() {
        return docletEnvironment.getSpecifiedElements();
    }

    @Override
    public Set<? extends Element> getIncludedElements() {
        return docletEnvironment.getIncludedElements();
    }

    @Override
    public DocTrees getDocTrees() {
        return docletEnvironment.getDocTrees();
    }

    @Override
    public Elements getElementUtils() {
        return docletEnvironment.getElementUtils();
    }

    @Override
    public Types getTypeUtils() {
        return docletEnvironment.getTypeUtils();
    }

    @Override
    public boolean isIncluded(Element el) {
        return docletEnvironment.isIncluded(el) && !filter.test(el);
    }

    @Override
    public boolean isSelected(Element el) {
        return docletEnvironment.isSelected(el);
    }

    @Override
    public JavaFileManager getJavaFileManager() {
        return docletEnvironment.getJavaFileManager();
    }

    @Override
    public SourceVersion getSourceVersion() {
        return docletEnvironment.getSourceVersion();
    }

    @Override
    public ModuleMode getModuleMode() {
        return docletEnvironment.getModuleMode();
    }

    @Override
    public JavaFileObject.Kind getFileKind(TypeElement type) {
        return docletEnvironment.getFileKind(type);
    }
}
