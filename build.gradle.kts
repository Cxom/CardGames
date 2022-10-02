plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.6"
}

group = "net.punchtree"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://ci.mg-dev.eu/plugin/repository/everything")
    }
    maven {
        url = uri("https://repo.inventivetalent.org/content/groups/public/")
    }
}

dependencies {
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.19.2-R0.1-SNAPSHOT")

    implementation("net.punchtree:punchtree-util:0.0.1-SNAPSHOT")
    compileOnly("org.inventivetalent:mapmanager:1.8.8-SNAPSHOT")
    compileOnly("com.bergerkiller.bukkit:BKCommonLib:1.19-v2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks {

    build {
        dependsOn(reobfJar)
        dependsOn("copyToTestServerPluginFolder")
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

tasks.register<Copy>("copyToTestServerPluginFolder") {
    from("build/libs/CardGames-1.0-SNAPSHOT.jar")
    into("C:/ScratchTester3/plugins");
}


tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}