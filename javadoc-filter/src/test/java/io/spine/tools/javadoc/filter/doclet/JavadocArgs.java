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

import com.google.common.collect.Iterables;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * Helps to compose arguments passed to a doclet.
 */
final class JavadocArgs {

    private final List<String> classes = new ArrayList<>();
    private final List<String> packages = new ArrayList<>();

    private Path sourcePath;
    private Path destination;

    private final List<String> arguments = new ArrayList<>();

    @CanIgnoreReturnValue
    JavadocArgs setSourcePath(Path sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    @CanIgnoreReturnValue
    JavadocArgs setDestination(Path directory) {
        this.destination = directory;
        return this;
    }

    @CanIgnoreReturnValue
    JavadocArgs addSource(String sourceFile) {
        Path srcFile = this.sourcePath.resolve(sourceFile);
        classes.add(srcFile.toString());
        return this;
    }

    @CanIgnoreReturnValue
    JavadocArgs addPackage(String packageName) {
        packages.add(packageName);
        return this;
    }

    String[] build() {
        checkState(
                sourcePath != null,
                "Source code directory must be specified. Please call `setSourcePath(Path)`."
        );
        checkState(
                destination != null,
                "Destination directory must be specified. Please call `setDestination(Path)`."
        );
        addDestination();
        addSourcePath();
        arguments.addAll(packages);
        arguments.addAll(classes);

        String[] result = Iterables.toArray(arguments, String.class);
        return result;
    }

    private void addSourcePath() {
        arguments.add("-sourcepath");
        arguments.add(sourcePath.toString());
    }

    private void addDestination() {
        arguments.add("-d");
        arguments.add(destination.toString());
    }
}
