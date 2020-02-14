package me.deceitya.siumaikun

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.permission.Permission
import cn.nukkit.plugin.PluginBase
import me.deceitya.siumaikun.command.SiumaiCommand
import me.deceitya.siumaikun.listener.PlayerChatListener
import me.deceitya.siumaikun.utils.Database
import me.deceitya.siumaikun.utils.MessageContainer

class SiumaikunPlugin : PluginBase() {
    override fun onEnable() {
        dataFolder.mkdir()
        Database.init(dataFolder.absolutePath + "/chat.db")
        MessageContainer.load(this)
        addPermission()
        registerCommand()
        server.pluginManager.registerEvents(PlayerChatListener(), this)
    }

    override fun onCommand(
        sender: CommandSender?,
        command: Command?,
        label: String?,
        args: Array<out String>?
    ): Boolean {
        return true
    }

    private fun addPermission() {
        server.pluginManager.addPermission(
            Permission(
                "siumaikun.command.siumai",
                "Allows the user to run the siumai command",
                Permission.DEFAULT_TRUE
            )
        )
    }

    private fun registerCommand() {
        server.commandMap.register("siumai", SiumaiCommand(this))
    }
}