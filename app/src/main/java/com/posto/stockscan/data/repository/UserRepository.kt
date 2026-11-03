package com.posto.stockscan.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.posto.stockscan.data.db.Role
import com.posto.stockscan.data.db.User
import com.posto.stockscan.data.db.UserDao
import com.posto.stockscan.domain.UserValidator
import com.posto.stockscan.security.PasswordHasher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(private val dao: UserDao) {

    suspend fun hasAnyUser(): Boolean = dao.count() > 0

    fun observeUsers(): Flow<List<User>> = dao.observeAll()

    /** Devolve o utilizador se o nome e a senha estiverem corretos e a conta estiver ativa. */
    suspend fun authenticate(username: String, password: String): User? {
        val user = dao.findByUsername(UserValidator.normalizeUsername(username)) ?: return null
        if (!user.active) return null
        val ok = withContext(Dispatchers.Default) { PasswordHasher.verify(password, user.passwordHash) }
        return if (ok) user else null
    }

    suspend fun createUser(
        name: String,
        username: String,
        password: String,
        confirmation: String,
        role: Role,
    ): CreateUserResult {
        UserValidator.validate(name, username, password, confirmation)?.let { return CreateUserResult.Error(it) }
        val normalized = UserValidator.normalizeUsername(username)
        if (dao.findByUsername(normalized) != null) {
            return CreateUserResult.Error("Já existe um utilizador \"$normalized\".")
        }
        val hash = withContext(Dispatchers.Default) { PasswordHasher.hash(password) }
        val user = User(name = name.trim(), username = normalized, passwordHash = hash, role = role)
        return try {
            CreateUserResult.Success(user.copy(id = dao.insert(user)))
        } catch (e: SQLiteConstraintException) {
            CreateUserResult.Error("Já existe um utilizador \"$normalized\".")
        }
    }

    /**
     * Ativa ou desativa uma conta (US01). Uma conta desativada deixa de poder entrar,
     * mas o registo continua na base de dados (e o nome nos relatórios antigos).
     * Devolve a mensagem de erro, ou `null` se correu bem.
     */
    suspend fun setActive(user: User, active: Boolean, currentUserId: Long): String? {
        if (!active) {
            UserValidator.deactivationError(user.id, user.role == Role.ADMIN, currentUserId, dao.countActiveAdmins())
                ?.let { return it }
        }
        dao.update(user.copy(active = active))
        return null
    }
}
