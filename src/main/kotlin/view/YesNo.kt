package view

import view.message.InputMessage

enum class YesNo {
    Y,
    N,
    ;

    fun isYes(): Boolean = this == Y

    companion object {
        fun from(input: String): YesNo = entries.firstOrNull { it.name == input }
            ?: throw IllegalArgumentException(InputMessage.ERROR_INVALID_YES_NO)
    }
}
