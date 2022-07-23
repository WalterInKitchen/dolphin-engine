plugins {
    id("java")
    id("groovy")
}

group = "com.github.walterinkitchen"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("commons-io:commons-io:2.11.0")

    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    testImplementation("net.bytebuddy:byte-buddy:1.12.12")
    testImplementation("org.codehaus.groovy:groovy:3.0.11")
    testImplementation("org.spockframework:spock-core:2.2-M3-groovy-3.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}