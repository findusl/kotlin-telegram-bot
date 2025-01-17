package com.github.kotlintelegrambot.dispatcher.handlers

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.Update

data class CommandHandlerEnvironment internal constructor(
    override val bot: Bot,
    override val update: Update,
    override val message: Message,
    val args: List<String>
) : IMessageHandlerEnvironment

internal class CommandHandler(
    private val command: String,
    private val handleCommand: CommandHandlerEnvironment.() -> Unit
) : Handler {

    override fun checkUpdate(update: Update): Boolean {
        return (
            update.message?.text != null && update.message.text.startsWith("/") &&
                update.message.text.drop(1).split(" ")[0].split("@")[0] == command
            )
    }

    override fun handleUpdate(bot: Bot, update: Update) {
        checkNotNull(update.message)
        handleCommand(CommandHandlerEnvironment(bot, update, update.message, update.getCommandArgs()))
    }

    private fun Update.getCommandArgs(): List<String> =
        message?.text?.split("\\s+".toRegex())?.drop(1) ?: emptyList()
}
