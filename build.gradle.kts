plugins {
    id("java")
}

group = "com.github.walterinkitchen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.codehaus.groovy:groovy:3.0.10")

    testImplementation("org.spockframework:spock-core:2.2-M3-groovy-3.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}