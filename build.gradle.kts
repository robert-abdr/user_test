plugins {
	java
    id("com.diffplug.spotless") version "6.25.0"
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

spotless {
    java {
        palantirJavaFormat("2.38.0")
        indentWithSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
        removeUnusedImports()
        importOrder()
    }
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
    gradlePluginPortal()
	mavenCentral()
}

dependencies {
    /**
     * Spring Boot Starters
     */
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.session:spring-session-jdbc")

    /**
     * Database
     */
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.liquibase:liquibase-core")

    /**
     * Util
     */
    implementation("org.modelmapper:modelmapper:3.2.0")
    implementation("com.googlecode.libphonenumber:libphonenumber:8.12.47")

    /**
     * Cache
     */
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    /**
     * Lombok
     */
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    /**
     * Mapstruct
     */
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    /**
     * Dev
     */
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    /**
     * Test
     */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.bootJar {
    archiveFileName.set("service.jar")
}

    tasks.withType<Test> {
        useJUnitPlatform()
    }