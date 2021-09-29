/*
 * Copyright 2021, TeamDev. All rights reserved.
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

package io.spine.tools.javadoc.style.formatting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@DisplayName("`RemovePreTags` should")
class RemovePreTagsTest {

    private final Formatting formatting = new RemovePreTags();

    @Test
    @DisplayName("remove tags generated by proto compiler")
    void removeTags() {
        String result = formatting.apply(
                "/** <pre> Doc goes here </pre> <code>string field = 1;</code> */"
        );
        assertThat(result).isEqualTo(
                "/**  Doc goes here  <code>string field = 1;</code> */"
        );
    }

    @Test
    @DisplayName("not remove user pre tags from doc")
    void notRemoveTags() {
        String rawDocWithPreTag =
                "/** Doc header <pre> Preformated doc </pre> <code>string field = 1;</code> */";

        String formatted = formatting.apply(rawDocWithPreTag);
        assertThat(formatted)
                .isEqualTo(rawDocWithPreTag);
    }

    @Test
    @DisplayName("not format javadoc without pre tags")
    void notFormatWithoutPre() {
        String javadoc = "/** smth */";
        String formatted = formatting.apply(javadoc);
        assertThat(formatted)
                .isEqualTo(javadoc);
    }
}