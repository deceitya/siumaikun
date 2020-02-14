package me.deceitya.siumaikun.utils

import cn.nukkit.Server
import java.lang.Exception
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Timestamp

object Database {
    private var file: String? = null

    /**
     * @param file String DBファイル
     */
    fun init(file: String) {
        this.file = file

        // JDBCドライバのロード
        try {
            Class.forName("org.sqlite.JDBC")
        } catch (e: Exception) {
            Server.getInstance().logger.logException(e)
        }

        try {
            DriverManager.getConnection("jdbc:sqlite:$file").use { connection ->
                val sql =
                    "CREATE TABLE IF NOT EXISTS chat (player TEXT NOT NULL, message TEXT NOT NULL, time TIMESTAMP NOT NULL)"
                connection.prepareStatement(sql).use { statement ->
                    statement.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            Server.getInstance().logger.logException(e)
        }
    }

    /**
     * チャットデータ挿入
     * @param player String プレイヤー
     * @param message String チャットのメッセージ
     * @param timestamp Timestamp タイムスタンプ
     */
    fun insertChatData(player: String, message: String, timestamp: Timestamp = Timestamp(System.currentTimeMillis())) {
        DriverManager.getConnection("jdbc:sqlite:$file").use { connection ->
            val sql = "INSERT INTO chat (player, message, time) VALUES (?, ?, ?)"
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, player.toLowerCase())
                statement.setString(2, message)
                statement.setTimestamp(3, timestamp)
                statement.executeUpdate()
            }
        }
    }

    /**
     * プレイヤーのチャットデータ取得
     * @param player String? プレイヤー nullの場合は全プレイヤーが対象
     * @return List<String>
     */
    fun getChatMessages(player: String? = null): List<String> {
        val messages = mutableListOf<String>()
        DriverManager.getConnection("jdbc:sqlite:$file").use { connection ->
            val sql = if (player == null) "SELECT message FROM chat" else "SELECT message FROM chat WHERE player = ?"
            connection.prepareStatement(sql).use { statement ->
                if (player != null) statement.setString(1, player.toLowerCase())
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        messages.add(resultSet.getString("message"))
                    }
                }
            }
        }

        return messages.toList()
    }
}