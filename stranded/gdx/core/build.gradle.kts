plugins {
    kotlin("plugin.serialization")
    kotlin("jvm")
}

apply(from = "$rootDir/gradle/kotlin-jvm-lib.gradle")

dependencies {
    implementation("com.badlogicgames.gdx:gdx:_")
    implementation("io.github.libktx:ktx-actors:_")
    implementation("io.github.libktx:ktx-app:_")
    implementation("io.github.libktx:ktx-ashley:_")
    implementation("io.github.libktx:ktx-assets:_")
    implementation("io.github.libktx:ktx-assets-async:_")
    implementation("io.github.libktx:ktx-async:_")
    implementation("io.github.libktx:ktx-box2d:_")
    implementation("io.github.libktx:ktx-collections:_")
    implementation("io.github.libktx:ktx-freetype:_")
    implementation("io.github.libktx:ktx-freetype-async:_")
    implementation("io.github.libktx:ktx-graphics:_")
    implementation("io.github.libktx:ktx-i18n:_")
    implementation("io.github.libktx:ktx-inject:_")
    implementation("io.github.libktx:ktx-json:_")
    implementation("io.github.libktx:ktx-log:_")
    implementation("io.github.libktx:ktx-math:_")
    implementation("io.github.libktx:ktx-preferences:_")
    implementation("io.github.libktx:ktx-scene2d:_")
    implementation("io.github.libktx:ktx-style:_")
    implementation("io.github.libktx:ktx-tiled:_")
    implementation("io.github.libktx:ktx-vis:_")
    implementation("io.github.libktx:ktx-vis-style:_")

    implementation(project(":stranded:lib"))
    implementation(project(":stranded:server"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
}