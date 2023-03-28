plugins {
    id("java")
    id("org.checkerframework") version "0.6.25" apply true
}

group = "me.ryguy"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.yaml:snakeyaml:1.33")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}