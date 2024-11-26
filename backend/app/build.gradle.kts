plugins {
    application
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.diffplug.spotless") version "6.25.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")


    implementation(libs.guava)
    implementation("org.postgresql:postgresql:42.7.1")

    testImplementation(libs.junit)
    testImplementation("io.cucumber:cucumber-java:7.20.1")
    testImplementation("io.cucumber:cucumber-junit:7.20.1")
    testImplementation("io.cucumber:cucumber-picocontainer:7.20.1")
    testImplementation("org.mockito:mockito-core:4.11.0")
    testImplementation("org.mockito:mockito-inline:4.11.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

application {
    mainClass = "psa.cargahoras.App"
}

spotless {
    java {
        googleJavaFormat()
    }
}

tasks.named<Test>("test") {
    useJUnit()
    exclude("**/*CucumberTest*")
}

tasks.register<Test>("cucumberTest") {
    useJUnit()
    include("**/*CucumberTest*")
}

tasks.named("build") {
    dependsOn("spotlessApply")
}

tasks.register("format") {
    dependsOn("spotlessApply")
}
