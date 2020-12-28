package br.com.pebmed.domain
import java.util.*

object MockDate {

    private val date = Date()

    fun today() : Date {
        return date
    }
}