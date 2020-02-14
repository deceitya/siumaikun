package me.deceitya.siumaikun.listener

import cn.nukkit.event.Listener
import cn.nukkit.event.EventHandler
import cn.nukkit.event.player.PlayerChatEvent
import me.deceitya.siumaikun.utils.Database

class PlayerChatListener : Listener {
    @EventHandler
    fun insertMessage2DB(event: PlayerChatEvent) {
        Database.insertChatData(event.player.name, event.message)
    }
}