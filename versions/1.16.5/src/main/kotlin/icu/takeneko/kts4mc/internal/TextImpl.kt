package icu.takeneko.kts4mc.internal
//#if MC > 11900
import icu.takeneko.kts4mc.compat.text.Text
import net.minecraft.text.Style
import net.minecraft.text.TextColor
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting

class TextImpl : Text {

    private var backingText: net.minecraft.text.Text

    constructor() {
        backingText = net.minecraft.text.Text.of("")
    }

    constructor(content: String) {
        backingText = net.minecraft.text.Text.of(content)
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
        return TextImpl(backingText.copy().setStyle(Style.EMPTY.withFormatting(Formatting.STRIKETHROUGH)))
    }

    override fun underline(underline: Boolean): Text {
        return TextImpl(backingText.copy().setStyle(Style.EMPTY.withUnderline(underline)))
    }

    override fun plus(text: Text): Text {
        return TextImpl(
            net.minecraft.text.Texts.join(
                listOf(backingText, (text as TextImpl).backingText)
            ) { it }
        )
    }

    override fun newLiteral(content: String): Text {
        return TextImpl(content)
    }

    override fun newTranslatable(key: String, vararg values: Any): Text {
        return TextImpl(TranslatableText(key, *values))
    }
}
//#endif