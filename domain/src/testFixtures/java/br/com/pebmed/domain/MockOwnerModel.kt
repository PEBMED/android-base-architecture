package br.com.pebmed.domain

import br.com.pebmed.domain.entities.OwnerModel

class MockOwnerModel {
    
    companion object {
        fun mock() = OwnerModel(
            id = 1
        )
    }
}