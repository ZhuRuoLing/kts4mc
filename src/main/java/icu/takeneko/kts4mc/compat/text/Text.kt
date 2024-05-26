package icu.takeneko.kts4mc.compat.text

import net.minecraft.text.TextColor

interface Text {

    fun getString(): String

    fun color(color: TextColor): Text

    fun bold(bold: Boolean = true): Text

    fun strikethrough(strikethrough: Boolean = true): Text

    fun underline(underline: Boolean = true): Text

    operator fun plus(text: Text): Text

    fun newLiteral(content: String): Text

    fun newTranslatable(key: String, vararg values: Any): Text

    companion object {
        private lateinit var textImpl: Text

        fun literal(content: String): Text {
            return textImpl.newLiteral(content)
        }

        fun translatable(key: String, vararg values: Any): Text {
            return textImpl.newTranslatable(key, *values)
        }

        fun init(impl:Text){
            textImpl = impl
        }
    }
}