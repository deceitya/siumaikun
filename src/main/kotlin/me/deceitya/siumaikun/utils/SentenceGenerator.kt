package me.deceitya.siumaikun.utils

import com.atilika.kuromoji.ipadic.Tokenizer
import kotlin.random.Random

class SentenceGenerator {
    private val tokenizer = Tokenizer.Builder().build()
    private val stringData = mutableMapOf<String, MutableList<String>>()

    /**
     * 元の文章を追加する。
     * @param sentence 元の文章
     */
    fun addSentence(sentence: String) {
        val tokens = tokenizer.tokenize(sentence + TERMINATOR)
            .filter { it.partOfSpeechLevel2 != "空白" } // 不要なトークンは除くため

        var nowSurface = tokens.first().surface
        stringData[nowSurface] = mutableListOf()
        tokens.forEach {
            val tmpSurface = it.surface
            if (nowSurface != tmpSurface) { // シャルル化(嫌嫌嫌みたいな)防止のための比較
                stringData[nowSurface]?.add(tmpSurface)
            }

            if (!stringData.contains(tmpSurface)) {
                stringData[tmpSurface] = mutableListOf()
            }

            nowSurface = tmpSurface
        }
    }

    /**
     * 文章を生成する
     * @return String
     */
    fun generateSentence(): String {
        var result = findHead() // 最初を名詞にしないと文章がおかしくなるため
        var tmp = result
        while (!result.contains(TERMINATOR)) {
            val next = stringData[tmp]
            tmp = next!![Random.nextInt(next.size)]
            result += tmp
        }

        return result.removeSuffix(TERMINATOR)
    }

    /**
     * 名詞をランダムで1個返す
     * @return String
     */
    private fun findHead(): String {
        var head: String
        do {
            head = stringData.keys.shuffled().first()
        } while (tokenizer.tokenize(head).first().partOfSpeechLevel1 != "名詞")

        return head
    }

    companion object {
        const val TERMINATOR = "\\"
    }
}