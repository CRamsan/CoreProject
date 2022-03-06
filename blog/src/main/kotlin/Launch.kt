package com.cramsan.blog

import org.commonmark.renderer.html.HtmlRenderer
import org.commonmark.node.*
import org.commonmark.parser.Parser
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * @author cramsan
 */

fun main(args: Array<String>) {

    if (args.size != 2) {
        throw RuntimeException("Only two arguments required. Found ${args.size} arguments: $args")
    }

    val inputFolder = args[0]
    val outputFolder = args[1]

    val root = File(inputFolder)
    val output = File(outputFolder)

    if (!root.exists()) {
        throw RuntimeException("Input folder does not exist: $root")
    } else if (!root.isDirectory) {
        throw RuntimeException("Input folder is not a directory: $root")
    }

    root.listFiles()?.forEach {
        process(it, output)
    }
}

fun process(file: File, outputFolder: File) {
    if (!file.exists()) {
        throw RuntimeException("Folder does not exist: $file")
    }

    if (file.isDirectory) {
        val newFolder = File(outputFolder, file.name)
        if (newFolder.mkdir()) {
            throw RuntimeException("Could not create folder: $newFolder")
        }
    } else if (file.isFile) {
        processFile(file, outputFolder)
    } else {
        throw RuntimeException("File is not a directory or file: $file")
    }
}

fun processFile(file: File, outputFolder: File) {
    when (file.extension.lowercase()) {
        "md" -> processMarkdown(file, outputFolder)
        else -> file.copyTo(outputFolder, overwrite = false)
    }
}

fun processMarkdown(file: File, outputFolder: File) {
    val reader = FileReader(file)

    val parser: Parser = Parser.builder().build()
    val renderer = HtmlRenderer.builder().build()

    val document: Node = parser.parseReader(reader)
    val processedFile = File(outputFolder, "${file.nameWithoutExtension}.md")

    val fileWriter = FileWriter(processedFile)
    renderer.render(document, fileWriter)
}