plugins {
    id("java")
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "2.7.8"

}

group = "tnews"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.3.5"))
    testImplementation(platform("org.junit:junit-bom"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

//    // https://mvnrepository.com/artifact/org.telegram/telegrambots
//    implementation("org.telegram:telegrambots:6.9.7.1")

//    implementation("org.springframework.boot:spring-boot-starter")
//    // https://mvnrepository.com/artifact/org.springframework/spring-core
//    implementation("org.springframework:spring-core")
//    // https://mvnrepository.com/artifact/org.springframework/spring-beans
//    implementation("org.springframework:spring-beans")
//    // https://mvnrepository.com/artifact/org.springframework/spring-context
//    implementation("org.springframework:spring-context")
//    // https://mvnrepository.com/artifact/org.springframework/spring-webmvc
//    implementation("org.springframework:spring-webmvc")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
    implementation("org.springframework.boot:spring-boot-starter-web")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql")

    // https://mvnrepository.com/artifact/org.telegram/telegrambots-spring-boot-starter
    implementation("org.telegram:telegrambots-spring-boot-starter")

//    implementation("jakarta.xml.bind:jakarta.xml.bind-api")
//    implementation("org.glassfish.jaxb:jaxb-runtime")
}

tasks.test {
    useJUnitPlatform()
}