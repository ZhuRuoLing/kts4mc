package icu.takeneko.kts4mc.file

import java.nio.file.Path
import kotlin.io.path.writeText

object Data {
    val client = DataDirectory("client") {
        generateExampleScript(this, it)
    }
    val server = DataDirectory("server") {
        generateExampleScript(this, it)
    }
    val common = DataDirectory("common") {
        generateExampleScript(this, it)
    }

    private fun generateExampleScript(dir: DataDirectory, path: Path) {
        path.resolve("example.kts").apply {
            this.toFile().createNewFile()
            this.writeText("println(\"Hello World! (${dir.baseDir} script executed)\")")
        }
    }

    fun init() {

    }
}

