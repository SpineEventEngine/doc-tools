import io.spine.internal.dependency.Dokka

plugins {
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    val spine = io.spine.internal.dependency.Spine(project)
    implementation(spine.base)

    compileOnly("org.jetbrains.dokka:dokka-core:${Dokka.version}")
    implementation("org.jetbrains.dokka:dokka-base:${Dokka.version}")
}
