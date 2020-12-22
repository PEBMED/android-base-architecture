package br.com.pebmed.domain

import br.com.pebmed.domain.entities.OwnerModel
/**
 * https://issuetracker.google.com/issues/139438142
 * Bug na IDE que não encontra dependências importadas
 * O build continua funcionando normalmente mesmo com esse problema
 * gerado ao utilizar o java-test-fixtures plugin
 */
import io.mockk.*

class MockOwnerModel {
    
    companion object {
        fun mock() : OwnerModel {
            val mockedOwnerModel = mockk<OwnerModel>()

            every {
                mockedOwnerModel.id
            } returns 1

            return mockedOwnerModel
        }
    }
}