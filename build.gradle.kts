plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.hadimhz.particles"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.triumphteam.dev/artifactory/public/")

}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("dev.triumphteam:triumph-gui:3.1.5")
}

tasks.shadowJar {
    minimize()
}