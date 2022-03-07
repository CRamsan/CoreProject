package com.cramsan.blog

import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Main method. Starts the process.
 *
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

    if (!output.exists() && !output.mkdirs()) {
        throw RuntimeException("Could not create output directory: $output")
    }

    root.listFiles()?.forEach {
        process(it, output)
    }
}

/**
 * Process the provided [file]. This [file] can be either a directory or a regular file. The file will be processed
 * and the resulting file will be saved in the [outputFolder].
 */
fun process(file: File, outputFolder: File) {
    if (!file.exists()) {
        throw RuntimeException("Folder does not exist: $file")
    }

    if (file.isDirectory) {
        val newFolder = File(outputFolder, file.name)
        if (!newFolder.exists()) {
            if (newFolder.mkdir()) {
                println("Folder: ${file.absolutePath} -> ${newFolder.absolutePath}")
            } else {
                throw RuntimeException("Could not create folder: $newFolder")
            }
        }
        file.listFiles()?.forEach {
            process(it, newFolder)
        }
    } else if (file.isFile) {
        processFile(file, outputFolder)
    } else {
        throw RuntimeException("File is not a directory or file: $file")
    }
}

/**
 * Process [file] that is a regular file. The file will be processed
 * and the resulting file will be saved in the [outputFolder].
 */
fun processFile(file: File, outputFolder: File) {
    when (file.extension.lowercase()) {
        "md" -> processMarkdown(file, outputFolder)
        else -> {
            val newFile = file.copyTo(outputFolder, overwrite = true)
            println("File: ${file.absolutePath} -> ${newFile.absolutePath}")
        }
    }
}

/**
 * Process [file] that is of type markdown. The file will be processed
 * and the resulting file will be saved in the [outputFolder].
 */
fun processMarkdown(file: File, outputFolder: File) {
    val reader = FileReader(file)

    val parser: Parser = Parser.builder().build()
    val renderer = HtmlRenderer.builder().build()

    val document: Node = parser.parseReader(reader)
    val processedFile = File(outputFolder, "${file.nameWithoutExtension}.html")

    val fileWriter = FileWriter(processedFile)
    fileWriter.use {
        renderer.render(document, fileWriter)
    }

    println("File: ${file.absolutePath} -> ${processedFile.absolutePath}")
}
