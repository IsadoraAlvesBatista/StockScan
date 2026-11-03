package com.posto.stockscan

import com.posto.stockscan.domain.UserValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class UserValidatorTest {
    @Test fun `dados validos`() {
        assertNull(UserValidator.validate("Maria", "maria.silva", "123456", "123456"))
    }

    @Test fun `normaliza usuario`() {
        assertEquals("joao", UserValidator.normalizeUsername("  JoAo "))
    }

    @Test fun `rejeita dados invalidos`() {
        assertNotNull(UserValidator.validate(" ", "maria", "123456", "123456"))
        assertNotNull(UserValidator.validate("Maria", "ma", "123456", "123456"))
        assertNotNull(UserValidator.validate("Maria", "maria silva", "123456", "123456"))
        assertNotNull(UserValidator.validate("Maria", "joão", "123456", "123456"))
        assertNotNull(UserValidator.validate("Maria", "maria", "12345", "12345"))
        assertNotNull(UserValidator.validate("Maria", "maria", "123456", "654321"))
    }

    @Test fun `nao pode desativar a propria conta`() {
        assertNotNull(UserValidator.deactivationError(targetId = 1, targetIsAdmin = false, currentUserId = 1, activeAdmins = 2))
    }

    @Test fun `nao pode desativar o ultimo administrador`() {
        assertNotNull(UserValidator.deactivationError(targetId = 2, targetIsAdmin = true, currentUserId = 1, activeAdmins = 1))
    }

    @Test fun `pode desativar funcionario ou administrador extra`() {
        assertNull(UserValidator.deactivationError(targetId = 2, targetIsAdmin = false, currentUserId = 1, activeAdmins = 1))
        assertNull(UserValidator.deactivationError(targetId = 2, targetIsAdmin = true, currentUserId = 1, activeAdmins = 2))
    }
}
