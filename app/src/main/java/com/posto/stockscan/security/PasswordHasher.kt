package com.posto.stockscan.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Hash de senhas com PBKDF2-HMAC-SHA256 e salt aleatório.
 * Formato guardado: `pbkdf2$<iterações>$<salt base64>$<hash base64>`.
 */
object PasswordHasher {
    private const val ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val PREFIX = "pbkdf2"
    private const val ITERATIONS = 100_000
    private const val KEY_LENGTH_BITS = 256
    private const val SALT_BYTES = 16

    private val random = SecureRandom()

    fun hash(password: String): String {
        val salt = ByteArray(SALT_BYTES).also(random::nextBytes)
        val derived = derive(password, salt, ITERATIONS)
        val enc = Base64.getEncoder()
        return "$PREFIX\$$ITERATIONS\$${enc.encodeToString(salt)}\$${enc.encodeToString(derived)}"
    }

    fun verify(password: String, stored: String): Boolean {
        val parts = stored.split('$')
        if (parts.size != 4 || parts[0] != PREFIX) return false
        val iterations = parts[1].toIntOrNull() ?: return false
        return try {
            val dec = Base64.getDecoder()
            val salt = dec.decode(parts[2])
            val expected = dec.decode(parts[3])
            MessageDigest.isEqual(expected, derive(password, salt, iterations))
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun derive(password: String, salt: ByteArray, iterations: Int): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH_BITS)
        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }
}
