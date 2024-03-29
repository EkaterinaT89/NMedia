package ru.netology.nmedia.exception

import ru.netology.nmedia.R
import java.io.IOException
import java.lang.RuntimeException
import java.sql.SQLException

sealed class AppError(val code: Int, val info: String): RuntimeException(info) {
    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is IOException -> NetWorkException
            else -> UnknownException
        }
    }
}

class ApiException(code: Int, message: String) : AppError(code, message)

object NetWorkException: AppError(-1, (R.string.no_network).toString())
object UnknownException: AppError(-1, (R.string.unknown_exception).toString())
object DbError : AppError(-1, "error_db")