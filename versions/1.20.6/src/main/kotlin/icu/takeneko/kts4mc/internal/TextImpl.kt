package icu.takeneko.kts4mc.internal

import icu.takeneko.kts4mc.compat.text.Text
import net.minecraft.text.Style
import net.minecraft.text.TextColor

class TextImpl : Text {

    private var backingText: net.minecraft.text.Text

    constructor() {
        backingText = net.minecraft.text.Text.empty()
    }

    constructor(content: String) {
        backingText = net.minecraft.text.Text.literal(content)
    }

    constructor(backing: net.minecraft.text.Text) {
        backingText = backing
    }

    override fun getString(): String {
        return backingText.string
    }

    override fun color(color: TextColor): Text {
        return TextImpl(backingText.copy().setStyle(Style.EMPTY.withColor(color)))
    }

    override fun bold(bold: Boolean): Text {
        return TextImpl(backingText.copy().setStyle(Style.EMPTY.withBold(bold)))
    }

    override fun strikethrough(strikethrough: Boolean): Text {
        return TextImpl(backingText.copy().setStyle(Style.EMPTY.withStrikethrough(strikethrough)))
    }

    override fun underline(underline: Boolean): Text {
        return TextImpl(backingText.copy().setStyle(Style.EMPTY.withUnderline(underline)))
    }

    override fun plus(text: Text): Text {
        return TextImpl(
            net.minecraft.text.Texts.join(
                listOf(backingText, (text as TextImpl).backingText),
                net.minecraft.text.Text.empty()
            )
        )
    }

    override fun newLiteral(content: String): Text {
        return TextImpl(content)
    }

    override fun newTranslatable(key: String, vararg values: Any): Text {
        return TextImpl(net.minecraft.text.Text.translatable(key, *values))
    }
}