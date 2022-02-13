import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom")
    val kotlinVersion: String by System.getProperties()
    kotlin("jvm").version(kotlinVersion)
}
base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}
val modVersion: String by project
version = modVersion

val mavenGroup: String by project
group = mavenGroup

minecraft {}

loom {
    this.accessWidenerPath.set {
        file("src/main/resources/pony_bookshelf.accesswidener")
    }
}

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.nucleoid.xyz")
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://ladysnake.jfrog.io/artifactory/mods")
    mavenCentral()
}

dependencies {
    val minecraftVersion: String by project
    minecraft("com.mojang:minecraft:$minecraftVersion")

    val yarnMappings: String by project
    mappings("net.fabricmc:yarn:$yarnMappings:v2")

    val loaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")

    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")

    val clothConfigVersion: String by project
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion")

    val polymerVersion: String by project
    modImplementation("eu.pb4:polymer:$polymerVersion")


    val palVersion: String by project
    modImplementation("io.github.ladysnake:PlayerAbilityLib:$palVersion")
    include("io.github.ladysnake:PlayerAbilityLib:$palVersion")

    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}
tasks {
    val javaVersion = JavaVersion.VERSION_17
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<KotlinCompile> {
        kotlinOptions { jvmTarget = javaVersion.toString() }
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") { expand(mutableMapOf("version" to project.version)) }
    }
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}