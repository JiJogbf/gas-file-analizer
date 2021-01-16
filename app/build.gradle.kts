plugins {
    kotlin("jvm") version "1.4.21"
    application
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    // для поддержки тестов
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application{
    // тут незабываем указывать путь до главного 
    // файла приложения.

    // Если файл в котором есть ф-ия main именуется main.kt,
    // то имя файла именуется MainKt  (filename.kt -> FilenameKt)
    mainClass.set("gas.MainKt")
}
