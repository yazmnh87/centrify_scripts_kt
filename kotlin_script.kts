#!/bin/bash/env kotlin
//C:\Users\bill.violette\IdeaProjects\Scripts
import java.io.File
import java.io.FileReader

val testing = false
val pathRoot = "C:/Log_to_process/"
val path = "${pathRoot}adinfo_support.txt"
var newFileErrorAndFailure: File
var newFileSummary: File
val regexError = "error".toRegex()
val regexFailed = "failed".toRegex()
val regexFailure = "failure".toRegex()
var ticketNumber: String? = ""





if(!testing){
    println("What is the ticket#?")
    ticketNumber = readLine()
    println("$ticketNumber")
    println("${pathRoot + ticketNumber} ")
    val ticketDirectory = File("C:/Log_to_process/$ticketNumber").mkdir()
    println("hello + $ticketDirectory")
    newFileErrorAndFailure = File("${pathRoot + ticketNumber}/error_failure_line_output.txt")
    newFileSummary = File("${pathRoot + ticketNumber}/summary_output.txt")
    //buildAdinfoSummary(path, newFileSummary)
extractErrorsAndFailures(path, newFileErrorAndFailure)
}else{
    newFileErrorAndFailure = File("C:/Log_to_process/error_failure_line_output.txt")
    newFileSummary = File("C:/Log_to_process/summary_output.txt")
    buildAdinfoSummary(path, newFileSummary)
extractErrorsAndFailures(path, newFileErrorAndFailure)
}



fun buildAdinfoSummary(filePath: String, newFile: File){
    val file = FileReader(filePath)
    var count = 0
    var configLine = 0
    var startOfLogging = false

    file.forEachLine {
        count++
//        if(count in 1..15){
//            newFile.appendText("\n" + it)
//        }

        fun addBlockBlankLines(file: File){
            file.appendText("\n")
        }

        fun extractAgentConfigProperties(line: String): Boolean{
            val list = listOf("Configuration:",
                    "adclient.(.[a-z]+?).+:",
                    "addns.(.[a-z]+?).+:",
                    "adjust.(.[a-z]+?).+:",
                    "adjust.(.[a-z]+?).+:",
                    "audittrail.(.[a-z]+?).+:",
                    "adsec.(.[a-z]+?).+:",
                    "capi.(.[a-z]+?).+:",
                    "krb5.(.[a-z]+?).+:",
                    "fips.(.[a-z]+?).+:",
                    "logger.(.[a-z]+?).+:",
                    "lrpc.(.[a-z]+?).+:",
                    "queueable.(.[a-z]+?).+:",
                    "dc.(.[a-z]+?).+:",
                    "domain.(.[a-z]+?).+:",
                    "db2.(.[a-z]+?).+:",
                    "microsoft.(.[a-z]+?).+:",
                    "lam.(.[a-z]+?).+:",
                    "nisd.(.[a-z]+?).+:",
                    "aix.(.[a-z]+?).+:",
                    "adjoin.(.[a-z]+?).+:",
                    "adpassword.(.[a-z]+?).+:",
                    "rhel.(.[a-z]+?).+:",
                    "smartcard.(.[a-z]+?).+:",
                    "dz.(.[a-z]+?).+:",
                    "dzdo.(.[a-z]+?).+:",
                    "auto.(.[a-z]+?).+:",
                    "agent.(.[a-z]+?).+:",
                    "autofix.(.[a-z]+?).+:",
                    "cache.(.[a-z]+?).+:",
                    "dzsh.(.[a-z]+?).+:",
                    "dad.(.[a-z]+?).+:",
                    "dash.(.[a-z]+?).+:",
                    "event.(.[a-z]+?).+:",
                    "lrpc2.(.[a-z]+?).+:",
                    "spool.(.[a-z]+?).+:",
                    "uid.(.[a-z]+?).+:",
                    "gid.(.[a-z]+?).+:",
                    "user.(.[a-z]+?).+:",
                    "preferred.(.[a-z]+?).+:",
                    "ldapproxy.(.[a-z]+?).+:",
                    "log:",
                    "gp.(.[a-z]+?).+:",
                    "microsoft.(.[a-z]+?).+:",
                    "pam.(.[a-z]+?).+:",
                    "nss.(.[a-z]+?).+:",
                    "secedit.(.[a-z]+?).+:"
            )
            var isMatch = false

            for(word in list){
                val regex = Regex(word)
                if(regex.containsMatchIn(line)){
                    //println("$configLine,$count,${count > configLine}")
                    if(line.contains("Configuration:")){
                        configLine = count
                        println("$it, $configLine")
                        isMatch = true
                    }
                    if(configLine != 0 && count > configLine && !startOfLogging){
                        println("$configLine, $count")
                        isMatch = true
                    }

                }
            }

            return isMatch
        }

        fun extractTimeAndCentrifyMethod(line: String): Boolean{
            val time = Regex("(\\w{3}\\s\\d{2}\\s(?:\\d{2}:)(?:\\d{2}:)\\d\\d)")
            val angleBracket = Regex("<(.*?)>")
            var isMatch = false
            if(line.contains(time) && line.contains(angleBracket)){
                isMatch = true
            }
            println("$isMatch")
            return isMatch
        }

        fun returnTimeAndCentrifyMethod(line:String): String?{
            val match = Regex("(\\w{3}\\s\\d{2}\\s(?:\\d{2}:)(?:\\d{2}:)\\d\\d)").find(it,0)
            val match1 = Regex("<(.*?)>").find(it,0)
            val builder = StringBuilder()
            println(match1?.value)
            return builder.append("$count :").append("${match?.value}").append("${match1?.value}").toString()

        }

        if("System logging information last '8' hours:".toRegex().containsMatchIn(it)) startOfLogging = true
        //if(extractAgentConfigProperties(it)) println("ISMATCH + $count") else null


        when{
            (count in 1..15) -> {
                if(count == 1) {
                    newFile.appendText(it)
                }else if(count == 15){
                    newFile.appendText("\n" + it)
                    repeat(6) {addBlockBlankLines(newFile)}
                } else newFile.appendText("\n" + it) }
            extractAgentConfigProperties(it) -> newFile.appendText("\n" + it)
            extractTimeAndCentrifyMethod(it) -> newFile.appendText("\n" + returnTimeAndCentrifyMethod(it))
//            startOfLogging -> {
//                println("ADDING BLANKLINES")
//                repeat(6){addBlockBlankLines(newFile)}
//            }
            else -> null

        }
    }


}


fun extractErrorsAndFailures(filePath: String, newFile: File){

    val file = FileReader(filePath)
    var count = 0
    file.forEachLine {
        count++
        if (regexError.containsMatchIn(it) || regexFailed.containsMatchIn(it) || regexFailure.containsMatchIn(it)) {

            newFile.appendText("\n" + count + ":" + it)
        }
    }
}




//forEachLine {
//    it.matches(/python/)
////    val result = it.filter {
////        it.isDigit() }
//    println("$result")
//}