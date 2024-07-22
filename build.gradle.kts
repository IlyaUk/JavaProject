plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    implementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
    implementation("io.github.bonigarcia:webdrivermanager:5.9.1")
    implementation("org.seleniumhq.selenium:selenium-java:4.22.0")
}

tasks.test {
    useJUnitPlatform()
}