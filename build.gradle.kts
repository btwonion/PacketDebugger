@file:Suppress("SpellCheckingInspection")
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("fabric-loom") version "1.1-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.8.0"

    id("com.modrinth.minotaur") version "2.7.2"
    id("com.github.breadmoirai.github-release") version "2.4.1"

    `maven-publish`
}

group = "dev.nyon"
val majorVersion = "1.0.0"
version = "$majorVersion-1.19.3"
val authors = listOf("btwonion")
val githubRepo = "btwonion/PacketDebugger"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com")
    maven("https://maven.parchmentmc.org")
    maven("https://maven.isxander.dev/releases")
}

dependencies {
    minecraft("com.mojang:minecraft:1.19.3")
    mappings(loom.layered {
        parchment("org.parchmentmc.data:parchment-1.19.3:2023.02.05@zip")
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.14")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.74.0+1.19.3")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.1+kotlin.1.8.10")
    modImplementation("dev.isxander:yet-another-config-lib:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    modApi("com.terraformersmc:modmenu:5.0.2")
}

tasks {
    processResources {
        val modId = "packet-debugger"
        val modName = "PacketDebugger"
        val modDescription = "Mod for debugging packets sent and received"

        inputs.property("id", modId)
        inputs.property("group", project.group)
        inputs.property("name", modName)
        inputs.property("description", modDescription)
        inputs.property("version", project.version)
        inputs.property("github", githubRepo)

        filesMatching("fabric.mod.json") {
            expand(
                "id" to modId,
                "group" to project.group,
                "name" to modName,
                "description" to modDescription,
                "version" to project.version,
                "github" to githubRepo,
            )
        }
    }

    register("releaseMod") {
        group = "publishing"

        dependsOn("modrinthSyncBody")
        dependsOn("modrinth")
        dependsOn("githubRelease")
        dependsOn("publish")
    }
}

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("DhzL69QG")
    versionNumber.set("${project.version}")
    versionType.set("release")
    uploadFile.set(tasks["remapJar"])
    gameVersions.set(listOf("1.19.3"))
    loaders.set(listOf("fabric", "quilt"))
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
        optional.project("modmenu")
    }
    syncBodyFrom.set(file("README.md").readText())
}

githubRelease {
    token(findProperty("github.token")?.toString())

    val (owner, repo) = githubRepo.split("/")
    owner(owner)
    repo(repo)
    tagName("v${project.version}")
    overwrite(true)
    releaseAssets(tasks["remapJar"].outputs.files)
    targetCommitish("master")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
    kotlinOptions.freeCompilerArgs += "-Xskip-prerelease-check"
}

publishing {
    repositories {
        maven {
            name = "nyon"
            url = uri("https://repo.nyon.dev/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.nyon"
            artifactId = "packetdebugger"
            version = project.version.toString()
            from(components["java"])
        }
    }
}

java {
    withSourcesJar()
}