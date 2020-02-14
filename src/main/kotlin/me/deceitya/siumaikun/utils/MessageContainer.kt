package me.deceitya.siumaikun.utils

import cn.nukkit.utils.TextFormat
import me.deceitya.siumaikun.SiumaikunPlugin
import java.nio.charset.StandardCharsets
import java.util.*

object MessageContainer {
    private val messages = mutableMapOf<String, String>()

    /**
     * @param plugin SiumaikunPlugin
     */
    fun load(plugin: SiumaikunPlugin) {
        // useでtry-with-resources
        plugin.getResource("message.properties").bufferedReader(StandardCharsets.UTF_8).use { reader ->
            val properties = Properties()
            properties.load(reader)
            properties.forEach { key, value ->
                messages[key.toString()] = value.toString()
            }
        }
    }

    /**
     * メッセージ取得
     * @param key String メッセージキー
     * @param params Array<String> 置換用の文字列
     * @return String
     */
    fun get(key: String, params: Array<String> = emptyArray()): String {
        var message = messages[key] ?: key
        if (params.isNotEmpty()) {
            for (i in params.indices) {
                message = message.replace("%${i + 1}", params[i])
            }
        }

        return TextFormat.colorize(message)
    }
}