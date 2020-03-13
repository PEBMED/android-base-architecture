package br.com.pebmed.domain.usecases

import br.com.pebmed.domain.base.ResultWrapper
import junit.framework.Assert.*
import org.junit.Test

class ResultWrapperTest {
    @Test
    fun `SHOULD call success block WHEN unwrap success ResultWrapper AND has error block`() {
        val expectedResult = "SUCCESS_RESULT"
        val resultWrapper = ResultWrapper<String, String>(success = expectedResult)

        var successBlockCalled = false
        var successBlockParamReceived = ""

        var errorBlockCalled = false

        resultWrapper.unwrap(
            successBlock = {
                successBlockCalled = true
                successBlockParamReceived = it
            },
            errorBlock = {
                errorBlockCalled = true
            }
        )

        assertTrue(successBlockCalled)
        assertEquals(expectedResult, successBlockParamReceived)
        assertFalse(errorBlockCalled)
    }

    @Test
    fun `SHOULD call success block WHEN unwrap success ResultWrapper AND has no error block`() {
        val expectedResult = "SUCCESS_RESULT"
        val resultWrapper = ResultWrapper<String, String>(success = expectedResult)

        var successBlockCalled = false
        var successBlockParamReceived = ""

        resultWrapper.unwrap(
            successBlock = {
                successBlockCalled = true
                successBlockParamReceived = it
            }
        )

        assertTrue(successBlockCalled)
        assertEquals(expectedResult, successBlockParamReceived)
    }

    @Test
    fun `SHOULD call error block WHEN unwrap error ResultWrapper AND has success block`() {
        val expectedError = "ERROR_RESULT"
        val resultWrapper = ResultWrapper<String, String>(error = expectedError)

        var successBlockCalled = false

        var errorBlockCalled = false
        var errorBlockParamReceived = ""

        resultWrapper.unwrap(
            successBlock = {
                successBlockCalled = true
            },
            errorBlock = {
                errorBlockCalled = true
                errorBlockParamReceived = it
            }
        )

        assertTrue(errorBlockCalled)
        assertEquals(expectedError, errorBlockParamReceived)
        assertFalse(successBlockCalled)
    }

    @Test
    fun `SHOULD call error block WHEN unwrap error ResultWrapper AND has no success block`() {
        val expectedError = "ERROR_RESULT"
        val resultWrapper = ResultWrapper<String, String>(error = expectedError)

        var errorBlockCalled = false
        var errorBlockParamReceived = ""

        resultWrapper.unwrap(
            errorBlock = {
                errorBlockCalled = true
                errorBlockParamReceived = it
            }
        )

        assertTrue(errorBlockCalled)
        assertEquals(expectedError, errorBlockParamReceived)
    }
}