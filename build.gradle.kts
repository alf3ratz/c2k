plugins {
    kotlin("jvm") version "2.0.20"
    antlr
    jacoco
}

group = "org.alf3ratz"
version = "0.0.1"
repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    antlr("org.antlr:antlr4:4.10.1")
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-visitor", "-long-messages")
    outputs.dir(file("build/generated-src/antlr/main"))
}
tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
jacoco {
    toolVersion = "0.8.10"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(false)
        html.required.set(true)
        csv.required.set(false)
    }

    classDirectories.setFrom(
        fileTree("$buildDir/classes/kotlin/main").matching {
            exclude("**/generated/**")
        }
    )
    sourceDirectories.setFrom(files("src/main/kotlin"))
    executionData.setFrom(fileTree(buildDir).include("jacoco/test.exec"))
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)

    violationRules {
        rule {
            limit {
                minimum = "0.05".toBigDecimal()
            }
        }
    }
}

tasks.register<JacocoReport>("jacocoMergeReport") {
    dependsOn(subprojects.map { it.tasks.named("jacocoTestReport") })

    executionData.setFrom(fileTree(project.rootDir) {
        include("**/build/jacoco/test.exec")
    })

    sourceDirectories.setFrom(files(subprojects.flatMap { it.extensions.getByType<SourceSetContainer>()["main"].allSource.srcDirs }))
    classDirectories.setFrom(
        fileTree(project.rootDir) {
            include("**/build/classes/kotlin/main")
            exclude("**/generated/**")
        }
    )

    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}

tasks.named("compileTestKotlin") {
    dependsOn("generateTestGrammarSource")
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
        manifest {
            attributes["Main-Class"] = "org.alf3ratz.MainKt"
        }
    }
    test {
        useJUnitPlatform()
        dependsOn(generateGrammarSource)
    }
}