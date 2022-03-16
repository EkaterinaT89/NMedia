package ru.netology.nmedia.exception

import ru.netology.nmedia.R
import java.lang.RuntimeException

sealed class AppError(val code: Int, val info: String): RuntimeException(info)

class ApiException(code: Int, message: String) : AppError(code, message)

object NetWorkException: AppError(-1, (R.string.no_network).toString())
object UnknownException: AppError(-1, (R.string.unknown_exception).toString())