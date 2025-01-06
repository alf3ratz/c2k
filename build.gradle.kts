plugins {
    kotlin("jvm") version "2.0.20"
    antlr
}

group = "org.alf3ratz"
version = "0.0.1"
repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    antlr("org.antlr:antlr4:4.10.1")
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-visitor", "-long-messages")
    outputs.dir(file("build/generated-src/antlr/main"))
}

tasks {
    compileJava {
        dependsOn(generateGrammarSource)
    }
    compileKotlin {
        dependsOn(generateGrammarSource)
    }

    val fatJar by creating(Jar::class) {
        dependsOn(build)
        archiveBaseName.set("c2k")
        archiveVersion.set("0.0.1")
        from(sourceSets.main.get().output)
        configurations["runtimeClasspath"].forEach {
            from(zipTree(it.absoluteFile))
        }
    }
    test {
        useJUnitPlatform()
    }
}