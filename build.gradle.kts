plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.11.0")
    implementation("org.junit.jupiter:junit-jupiter-params:5.11.0")
    implementation("io.github.bonigarcia:webdrivermanager:6.1.0")
    //implementation("org.seleniumhq.selenium:selenium-java:4.32.0")
    implementation("com.codeborne:selenide-appium:7.9.2")
}

tasks.test {
    useJUnitPlatform()
}