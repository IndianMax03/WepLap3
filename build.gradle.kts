import org.apache.tools.ant.BuildException
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.validation.Validator

plugins {
    java
}

group = "com.labs.weblab3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

