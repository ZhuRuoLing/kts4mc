package icu.takeneko.kts4mc.file

import java.io.File
import java.nio.file.Path
import kotlin.io.path.*

class DataDirectory(
    directory: String,
    val baseDir: String = "kts4mc",
    private val onDirectoryCreate: DataDirectory.(Path) -> Unit = {}
) {
    private val root = Path(baseDir).resolve(directory)

    private fun ensureDirectoryExists() {
        if (root.notExists()) {
            root.createParentDirectories()
            root.createDirectory()
            this.onDirectoryCreate(root)
        }
    }

    init {
        ensureDirectoryExists()
    }

    fun file(path: String): File {
        return root.resolve(path).toFile()
    }

    fun allFiles(): List<File> {
        return root.listDirectoryEntries().map { it.toFile() }
    }

    operator fun get(path: String): File {
        return root.resolve(path).toFile()
    }

    fun exists(fileName: String): Boolean {
        return root.resolve(fileName).exists()
    }
}