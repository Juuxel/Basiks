import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    java
    kotlin("jvm") version "1.2.60"
    maven
    id("org.jetbrains.dokka") version "0.9.17"
}

group = "juuxel.basiks"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.2.0")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.2.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<DokkaTask> {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
