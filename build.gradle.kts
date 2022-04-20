plugins {
    val kotlinVersion: String by System.getProperties()

    java
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("fabric-loom") version "0.12.+"
    id("io.github.juuxel.loom-quiltflower") version "1.+"

    id("com.github.johnrengelman.shadow") version "7.+"
}

group = "dev.isxander"
version = "1.1.0"

repositories {
    mavenCentral()
    maven("https://repo.sk1er.club/repository/maven-public")
    maven("https://repo.woverflow.cc")
    maven("https://maven.terraformersmc.com")
    maven("https://jitpack.io")
}

val shade by configurations.creating

dependencies {
    val kotlinVersion: String by System.getProperties()
    val minecraftVersion: String by project
    val yarnVersion: String by project
    val loaderVersion: String by project
    val fabricVersion: String by project
    val fabricKotlinVersion: String by project

    implementation(kotlin("stdlib-jdk8", kotlinVersion))

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnVersion:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion+kotlin.$kotlinVersion")

    shade(modImplementation("gg.essential:elementa-1.18-fabric:+") {
        exclude(module = "kotlin-stdlib")
        exclude(module = "kotlin-stdlib-common")
        exclude(module = "kotlin-stdlib-jdk8")
        exclude(module = "kotlin-stdlib-jdk7")
        exclude(module = "kotlin-reflect")
        exclude(module = "annotations")
        exclude(module = "fabric-loader")
    })

    include(implementation("dev.isxander:settxi:2.1.1")!!)
    include(modImplementation("dev.isxander:settxi-cloth-impl:1.+")!!)

    include(implementation("org.bundleproject:libversion:0.0.+")!!)

    modImplementation("com.terraformersmc:modmenu:3.+")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    jar {
        archiveClassifier.set("dev")
    }

    shadowJar {
        archiveClassifier.set("dev-shadow")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(shade)

        relocate("gg.essential.elementa", "dev.isxander.lib.elementa")
        relocate("gg.essential.universal", "dev.isxander.lib.universalcraft")
    }

    remapJar {
        dependsOn("shadowJar")
        inputFile.set(shadowJar.get().archiveFile)
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.version
                )
            )
        }
    }

    compileKotlin {
        kotlinOptions {
            freeCompilerArgs += "-Xjvm-default=enable"
        }
    }
}
