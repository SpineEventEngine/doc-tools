plugins {
    `maven-publish`
}

repositories {
    mavenCentral()
}

val dokkaVersion = "1.6.10"

dependencies {
    val spine = io.spine.internal.dependency.Spine(project)
    implementation(spine.base)

    compileOnly("org.jetbrains.dokka:dokka-core:$dokkaVersion")
    implementation("org.jetbrains.dokka:dokka-base:$dokkaVersion")
}
