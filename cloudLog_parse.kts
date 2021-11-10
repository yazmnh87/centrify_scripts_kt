#!/bin/bash/env kotlin
//C:\Users\bill.violette\IdeaProjects\Scripts
import java.io.File
import java.io.FileReader
import kotlin.time.Duration
import java.util.Date

val startTime = Date()
val pathRoot = "C:/Log_to_process/cloud_logs_to_process"
var tenantId: String? = ""
var lineNumber = 0
var tenantIdMatch = 0
println("What is the tenant ID?")
tenantId = readLine()
val fileToProcess = "$pathRoot/Custom Logs.log"
val file = FileReader(fileToProcess)
var newFile = File("$pathRoot/${tenantId}_1.txt")
var newErrorFile = File("$pathRoot/${tenantId}_Errors.txt")
var newSummaryFile = File("$pathRoot/${tenantId}_summary.txt")
var newTestFile = File("$pathRoot/${tenantId}_test.txt")
val regexError = "error".toRegex()
val regexFailed = "failed".toRegex()
val regexFailure = "failure".toRegex()



    file.forEachLine {
        lineNumber++
        if(it.contains("$tenantId")){
            if(it.contains(regexError) || it.contains(regexFailed) || it.contains(regexFailure)){
                newErrorFile.appendText("\n"+"$lineNumber:" + " " + it)
            }
            val splitText = it.split(" ")
            if (lineNumber in 1..1500) newTestFile.appendText("\n"+"$lineNumber:" + " " + it)
                //val tenantRegex: Regex? = Regex(tenantId)
                if(it.contains("ABE0945")){
                    if (lineNumber in 1..1500) println(lineNumber)
                    if (lineNumber in 1..1500) println(tenantId)
                    tenantIdMatch++
            }
            newFile.appendText("\n"+it)
        }
    }

fun addBlockBlankLines(file: File){
    file.appendText("\n")
}

//repeat(6){addBlockBlankLines(newFile)}

val summaryText = "Number of tenant Id matches: $tenantIdMatch \n Number of total lines: $lineNumber \n Start Time: $startTime \n End Time: ${Date()}"


newSummaryFile.appendText(summaryText)
