plugins {
    id("java")
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.freefair.lombok") version "8.6"
}

repositories {
    mavenLocal()
    mavenCentral()
}

tasks.wrapper {
    gradleVersion = "8.7"
}

tasks.test {
    useJUnit()
}

dependencies {
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-web", version = "3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("net.datafaker:datafaker:2.1.0")
    implementation("org.instancio:instancio-core:4.3.2")
    implementation("com.thedeanda:lorem:2.2")

    implementation("com.opencsv:opencsv:5.9") {
        exclude(group = "commons-collections", module = "commons-collections")
    }

    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
    testImplementation("org.hamcrest:hamcrest:2.2")
}
