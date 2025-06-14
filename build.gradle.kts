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
     implementation("com.codeborne:selenide-grid:7.9.3")
    implementation("com.codeborne:selenide-appium:7.9.3")
    implementation("org.assertj:assertj-core:3.27.3")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.17")

    // Needed on Mac ARM (because the latest "seleniarm" version is 124)
    implementation("org.seleniumhq.selenium:selenium-devtools-v124:4.22.0")
}

tasks.test {
    useJUnitPlatform()
}