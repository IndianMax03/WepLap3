import org.apache.tools.ant.BuildException
import org.gradle.crypto.checksum.Checksum
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.validation.Validator

plugins {
    java
    id("org.gradle.crypto.checksum") version "1.4.0"
}

group = "com.labs.weblab3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.4.0")
    implementation("com.sun.faces:jsf-api:2.2.20")
    implementation("com.sun.faces:jsf-impl:2.2.20")
    implementation("org.primefaces:primefaces:12.0.0")
    implementation("net.bootsfaces:bootsfaces:1.5.0")
    implementation("org.eclipse.persistence:eclipselink:3.0.3")
    implementation("jakarta.platform:jakarta.jakartaee-api:8.0.0")
    implementation("org.mockito:mockito-inline:3.4.6")
    testImplementation("junit:junit:4.13.2")
}

/* Compile start */

tasks.compileJava {
    options.compilerArgs.add("-Xlint:unchecked")
}

val compile = tasks.register("compile") {
    group = project.property("tasksGroup").toString()
    dependsOn(tasks.compileJava, tasks.compileTestJava)
}

/* Compile end */

/* Jar start */

val build = tasks.register<Jar>("l3build") {
    dependsOn(compile)
    group = project.property("tasksGroup").toString()
    from(project.property("classesDirectory"))
    destinationDirectory.set(layout.buildDirectory.dir(project.property("libsDirectory").toString()))
    archiveBaseName.set(project.name)
}

/* Jar end */

/* Music start */

abstract class Music : AbstractExecTask<Music>(Music::class.java) {

    init {
        group = project.property("tasksGroup").toString()
    }

    override fun exec() {
        executable = "afplay"

        args(project.property("mp3File"))

        super.exec()
    }

}


val music = tasks.register<Music>("music") {
    dependsOn(build)
}

/* Music end */

/* Scp start */

open class Scp : AbstractExecTask<Scp>(Scp::class.java) {

    init {
        group = project.property("tasksGroup").toString()
    }

    override fun exec() {
        executable = "scp"

        args(listOf("-P", "2222", "build/libs/WebLab3-1.0-SNAPSHOT.jar", "s333057@helios.se.ifmo.ru:/home/studs/s333057/mispi-lab3"))

        super.exec()
    }

}

val scp = tasks.register<Scp>("scp") {
    dependsOn(build)
}

/* Scp end */

/* XML start */

abstract class Xml : AbstractExecTask<Xml>(Xml::class.java) {

    @get:SkipWhenEmpty
    @get:InputDirectory
    abstract val sourceDirectory: DirectoryProperty

    init {
        group = project.property("tasksGroup").toString()
    }

    override fun exec() {
        val files = sourceDirectory.asFileTree.map { it.path }
        val xmlFiles = mutableListOf<String>()
        val reg = """.*\.x\w*ml$""".toRegex()

        files.forEach {
            if (it.matches(reg)) {
                xmlFiles.add(it)
            }
        }

        var failures: Set<String> = mutableSetOf()
        val parser: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val validator: Validator? = null
        xmlFiles.forEach {
            var document: Document? = null
            try {
                document = parser.parse(it)
            } catch (e: Exception) {
                logger.error("Error while parsing $it: ${e.message}")
                failures = failures.plus(it)
            }
            if (document != null) {
                try {
                    validator?.validate(DOMSource(document))
                } catch (e: Exception) {
                    logger.error("Error while validating $it ${e.message}")
                    failures = failures.plus(it)
                }
            }
        }
        if (failures.isNotEmpty()) throw BuildException("xml validation failures $failures")
    }

}

val xml = tasks.register<Xml>("xml") {
    sourceDirectory.set(layout.projectDirectory)
}

/* XML end */

/* test start */

tasks.withType<Test>().configureEach {
    group = project.property("tasksGroup").toString()
    project.setProperty("testResultsDirName", "$buildDir/${project.property("testOutputDir").toString()}")
    useJUnit()
    dependsOn(build)
    reports {
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("${project.property("testOutputDir").toString()}/${project.property("testHtmlOutputDir").toString()}"))
        junitXml.outputLocation.set(layout.buildDirectory.dir("${project.property("testOutputDir").toString()}/${project.property("testXmlOutputDir").toString()}"))
    }
}

/* test end */

/* report start */

tasks.register<TestReport>("report") {
    group = project.property("tasksGroup").toString()
    dependsOn(tasks.test)
    //  todo сделать коммит
}

/* test end */

tasks.register("MORGENSHTERN") {
    group = project.property("tasksGroup").toString()
    doLast {
        ant.withGroovyBuilder {
            "exec"("executable" to "git", "output" to "out.txt") {
                "arg" ("value" to "--short")
                "arg" ("value" to "status")
            }
        }
    }
}

//tasks.register<Exec>("team") {
//    group = project.property("tasksGroup").toString()
//    commandLine("touch someinfo")
//    finalizedBy(build)
//}