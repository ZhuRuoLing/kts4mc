package icu.takeneko.kts4mc.scripting

import icu.takeneko.kts4mc.compat.logging.LoggerCompat
import icu.takeneko.kts4mc.file.Data
import icu.takeneko.kts4mc.scripting.host.ScriptHost
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream

object ScriptManager {

    private var frozen = false
        private set

    private val enabledCommonScripts: MutableList<String> = mutableListOf()
    private val enabledServerScripts: MutableList<String> = mutableListOf()
    private var autoFreeze = false
    private val logger = LoggerCompat.getLogger()
    private val scriptHost = ScriptHost()

    private val json = Json {
        encodeDefaults = true
        prettyPrint = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun init() {
        val config = try {
            Data.root.file("scripting.json").inputStream().run {
                json.decodeFromStream(this)
            }
        } catch (e: Exception) {
            val default = ScriptingConfig(listOf(), listOf(), false)
            writeConfig(default)
            default
        }
        writeConfig(config)
        autoFreeze = config.autoFreeze
        enabledCommonScripts += config.enabledCommonScripts
        enabledServerScripts += config.enabledServerScripts
        if (autoFreeze) {
            commitToScriptHost()
        }
    }

    private fun commitToScriptHost() {
        (enabledServerScripts.map {
            scriptHost.evalFile(Category.SERVER, it)
        } + enabledCommonScripts.map {
            scriptHost.evalFile(Category.COMMON, it)
        }).forEach {
            it.reports.forEach { diag ->
                when (diag.severity) {
                    kotlin.script.experimental.api.ScriptDiagnostic.Severity.DEBUG -> {
                    }

                    kotlin.script.experimental.api.ScriptDiagnostic.Severity.INFO -> {
                        logger.info("[SCRIPT HOST] ${diag.render(withSeverity = false)}")
                    }

                    kotlin.script.experimental.api.ScriptDiagnostic.Severity.WARNING -> {
                        logger.warning("[SCRIPT HOST] ${diag.render(withSeverity = false)}")
                    }

                    kotlin.script.experimental.api.ScriptDiagnostic.Severity.ERROR -> {
                        logger.error("[SCRIPT HOST] ${diag.render(withSeverity = false)}")
                    }

                    kotlin.script.experimental.api.ScriptDiagnostic.Severity.FATAL -> {
                        logger.error("[SCRIPT HOST] ${diag.render(withSeverity = false)}")
                    }
                }
            }
        }
    }

    private fun writeConfig(config: ScriptingConfig) {
        Data.root.file("scripting.json").apply {
            if (exists()) delete()
            createNewFile()
        }.outputStream().run {
            json.encodeToStream(config, this)
        }
    }

    fun configureAutoFreezing(b: Boolean) {
        autoFreeze = b
        writeConfig(ScriptingConfig(enabledCommonScripts, enabledServerScripts, autoFreeze))
    }

    fun freeze(permanent: Boolean) {
        if (frozen) throw IllegalStateException("Already frozen")
        frozen = true
        if (permanent) {
            writeConfig(ScriptingConfig(enabledCommonScripts, enabledServerScripts, autoFreeze))
        }
        commitToScriptHost()
    }

    fun activate(name: String) {
        if (frozen) throw IllegalStateException("Already frozen")
        val category = determineCategory(name) ?: throw IllegalArgumentException("Category does not exist.")
        val fileName = fileName(name)
        when (category) {
            Category.COMMON -> {
                if (!Data.common.exists(fileName)) throw IllegalArgumentException("Script $name does not exist.")
                if (fileName in enabledCommonScripts) throw IllegalArgumentException("Script $name already activated.")
                enabledCommonScripts += fileName
            }

            Category.SERVER -> {
                if (!Data.server.exists(fileName)) throw IllegalArgumentException("Script $name does not exist.")
                if (fileName in enabledServerScripts) throw IllegalArgumentException("Script $name already activated.")
                enabledServerScripts += fileName
            }
        }
    }

    fun deactivate(name: String) {
        if (frozen) throw IllegalStateException("Already frozen")
        val category = determineCategory(name) ?: throw IllegalArgumentException("Category does not exist.")
        val fileName = fileName(name)
        when (category) {
            Category.COMMON -> {
                if (!Data.common.exists(fileName)) throw IllegalArgumentException("Script $name does not exist.")
                if (fileName !in enabledCommonScripts) throw IllegalArgumentException("Script $name not activated.")
                enabledCommonScripts -= fileName
            }

            Category.SERVER -> {
                if (!Data.server.exists(fileName)) throw IllegalArgumentException("Script $name does not exist.")
                if (fileName !in enabledServerScripts) throw IllegalArgumentException("Script $name not activated.")
                enabledServerScripts -= fileName
            }
        }
    }

    fun determineCategory(path: String): Category? {
        return if (path.contains("/")) {
            val (namespace, file) = path.split("/").apply { this[0] to this[1] }
            if (file.isEmpty()) return null
            when (namespace) {
                "common" -> Category.COMMON
                "server" -> Category.SERVER
                else -> null
            }
        } else {
            Category.COMMON
        }
    }

    fun fileName(path: String): String {
        return if (path.contains("/")) {
            path.substring(path.indexOf("/")..path.length)
        } else {
            path
        }
    }

    fun allFiles(): List<String> {
        return buildList {
            this += Data.server.allFiles().map { "server/" + it.name }
            this += Data.common.allFiles().map { "common/" + it.name }
        }
    }

    enum class Category {
        COMMON, SERVER
    }

    @Serializable
    data class ScriptingConfig(
        val enabledCommonScripts: List<String>,
        val enabledServerScripts: List<String>,
        val autoFreeze: Boolean
    )
}