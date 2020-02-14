package me.deceitya.siumaikun.command

import cn.nukkit.command.CommandSender
import cn.nukkit.command.PluginCommand
import kotlinx.coroutines.*
import me.deceitya.siumaikun.SiumaikunPlugin
import me.deceitya.siumaikun.utils.Database
import me.deceitya.siumaikun.utils.MessageContainer
import me.deceitya.siumaikun.utils.SentenceGenerator

class SiumaiCommand(owner: SiumaikunPlugin) : PluginCommand<SiumaikunPlugin>("siumai", owner) {
    init {
        permission = "siumaikun.command.siumai"
        description = MessageContainer.get("command.siumai.description")
        usage = MessageContainer.get("command.siumai.usage")
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!super.execute(sender, commandLabel, args)) return false

        val player = kotlin.runCatching { args[0] }.fold(onSuccess = { it }, onFailure = { null })

        // async await
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                val generator = SentenceGenerator()
                Database.getChatMessages(player).forEach {
                    generator.addSentence(it)
                }

                var sentence: String
                do {
                    // runCatchingでtry-catchみたいな
                    sentence = kotlin.runCatching { generator.generateSentence() }
                        .fold(onSuccess = { it }, onFailure = { "&cThere are no data." })
                } while (sentence == "")
                sentence
            }.let {
                sender.server.broadcastMessage(MessageContainer.get("siumaikun.chat", arrayOf(it)))
            }
        }

        return true
    }
}