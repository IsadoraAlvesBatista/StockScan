package com.posto.stockscan

import com.posto.stockscan.security.PasswordHasher
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordHasherTest {
    @Test fun `senha correta e verificada`() {
        val stored = PasswordHasher.hash("segredo123")
        assertTrue(PasswordHasher.verify("segredo123", stored))
    }

    @Test fun `senha errada e rejeitada`() {
        val stored = PasswordHasher.hash("segredo123")
        assertFalse(PasswordHasher.verify("segredo124", stored))
    }

    @Test fun `mesma senha gera hashes diferentes (salt)`() {
        assertNotEquals(PasswordHasher.hash("abcdef"), PasswordHasher.hash("abcdef"))
    }

    @Test fun `hash nao contem a senha em texto`() {
        assertFalse(PasswordHasher.hash("minhasenha").contains("minhasenha"))
    }

    @Test fun `valor guardado invalido e rejeitado sem excecao`() {
        assertFalse(PasswordHasher.verify("x", ""))
        assertFalse(PasswordHasher.verify("x", "pbkdf2\$abc\$zz\$zz"))
        assertFalse(PasswordHasher.verify("x", "pbkdf2\$1000\$!!!\$!!!"))
    }
}
