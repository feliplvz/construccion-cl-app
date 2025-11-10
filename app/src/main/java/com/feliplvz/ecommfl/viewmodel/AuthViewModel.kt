package com.feliplvz.ecommfl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feliplvz.ecommfl.data.network.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val userRole: UserRole = UserRole.GUEST,
    val userId: String? = null
)

enum class UserRole {
    GUEST,
    USER,
    ADMIN
}

class AuthViewModel : ViewModel() {
    private val supabase = SupabaseClient.client

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            try {
                val session = supabase.auth.currentSessionOrNull()
                if (session != null) {
                    val user = supabase.auth.currentUserOrNull()
                    val role = user?.userMetadata?.get("role")?.toString() ?: "user"
                    _authState.value = AuthState(
                        isAuthenticated = true,
                        userRole = if (role == "admin") UserRole.ADMIN else UserRole.USER,
                        userId = user?.id
                    )
                } else {
                    _authState.value = AuthState(isAuthenticated = false, userRole = UserRole.GUEST)
                }
            } catch (e: Exception) {
                _authState.value = AuthState(isAuthenticated = false, userRole = UserRole.GUEST)
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState(isLoading = true)

                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                val user = supabase.auth.currentUserOrNull()
                val role = user?.userMetadata?.get("role")?.toString() ?: "user"

                _authState.value = AuthState(
                    isAuthenticated = true,
                    userRole = if (role == "admin") UserRole.ADMIN else UserRole.USER,
                    userId = user?.id
                )
            } catch (e: Exception) {
                _authState.value = AuthState(
                    isAuthenticated = false,
                    userRole = UserRole.GUEST,
                    error = when {
                        e.message?.contains("Invalid login credentials") == true ->
                            "Credenciales incorrectas"
                        e.message?.contains("Email not confirmed") == true ->
                            "Email no confirmado"
                        else -> "Error al iniciar sesión: ${e.message}"
                    }
                )
            }
        }
    }

    fun signUp(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState(isLoading = true)

                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    data = mapOf(
                        "name" to name,
                        "role" to "user"
                    )
                }

                _authState.value = AuthState(
                    isAuthenticated = false,
                    error = "Cuenta creada. Revisa tu email para confirmarla"
                )
            } catch (e: Exception) {
                _authState.value = AuthState(
                    isAuthenticated = false,
                    error = when {
                        e.message?.contains("already registered") == true ->
                            "Este email ya está registrado"
                        else -> "Error al registrarse: ${e.message}"
                    }
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                _authState.value = AuthState(isAuthenticated = false, userRole = UserRole.GUEST)
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    error = "Error al cerrar sesión: ${e.message}"
                )
            }
        }
    }

    fun isAdmin(): Boolean = _authState.value.userRole == UserRole.ADMIN

    fun isUser(): Boolean = _authState.value.userRole == UserRole.USER

    fun getCurrentUserId(): String? = _authState.value.userId
}

