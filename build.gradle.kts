plugins {
    `java-library`
    id("org.checkerframework") version "0.6.25" apply true
    `maven-publish`
}

group = "me.ryguy.paperanno"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("org.yaml:snakeyaml:1.33")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/TheRealRyGuy/paper-annotations")
            credentials {
                username = "TheRealRyGuy"
                password = project.findProperty("gpr.token") as String
            }
        }
    }
    publications {
        register<MavenPublication>("github") {
            groupId = "me.ryguy"
            artifactId = "paper-annotations"
            version = "0.1-SNAPSHOT"
            pom {
                name.set("Paper Annotations")
                description.set("Automatically create your paper-plugin.yml file through annotations!")
                url.set("http://ryguy.me")
                licenses {
                    license {
                        name.set("GPL-v3.0")
                        url.set("http://www.gnu.org/licenses/gpl-3.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("TheRealRyGuy")
                        name.set("RyGuy")
                    }
                }
            }
            from(components["java"])
        }
    }
}

