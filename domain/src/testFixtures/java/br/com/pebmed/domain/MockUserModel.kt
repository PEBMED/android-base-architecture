package br.com.pebmed.domain

import br.com.pebmed.domain.entities.UserModel
/**
 * https://issuetracker.google.com/issues/139438142
 * Bug na IDE que não encontra dependências importadas
 * O build continua funcionando normalmente mesmo com esse problema
 * gerado ao utilizar o java-test-fixtures plugin
 */
import io.mockk.*

object MockUserModel {

    fun mock() : UserModel {
        val mockedUserModel = mockk<UserModel>()

        every {
            mockedUserModel.login
        } returns "luis.fernandez"

        every {
            mockedUserModel.avatarUrl
        } returns "http://avatar.url"

        return mockedUserModel
    }
}