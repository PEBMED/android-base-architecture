package com.example.basearch.data

interface BaseMapper<FROM, TO> {
    fun map(from: FROM): TO
}