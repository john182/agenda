package com.chronos.agenda.web

import java.io.IOException
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


/**
 * Created by John Vanderson M L on 25/02/2018.
 */
class WebClient {

    fun post(json: String): String {
        try {
            val url = URL("https://www.caelum.com.br/mobile")
            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("Content-type", "application/json")
            connection.setRequestProperty("Accept", "application/json")

            connection.doOutput = true

            val output = PrintStream(connection.outputStream)
            output.println(json)

            connection.connect()

            val scanner = Scanner(connection.inputStream)
            return scanner.next()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ""
    }
}