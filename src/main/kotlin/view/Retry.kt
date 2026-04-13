package view

import view.message.InputMessage

fun <T> retryUntilValid(action: () -> T): T {
    while (true) {
        try {
            return action()
        } catch (e: IllegalArgumentException) {
            OutputView.printError(e.message ?: InputMessage.ERROR_UNKNOWN)
        }
    }
}
