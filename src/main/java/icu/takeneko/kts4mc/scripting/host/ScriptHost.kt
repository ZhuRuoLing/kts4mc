package icu.takeneko.kts4mc.scripting.host

import icu.takeneko.kts4mc.scripting.ScriptManager
import icu.takeneko.kts4mc.scripting.def.ScriptDef
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

class ScriptHost {
    private val commonCompileResults: MutableMap<String, ResultWithDiagnostics<EvaluationResult>> = mutableMapOf()
    private val serverCompileResults: MutableMap<String, ResultWithDiagnostics<EvaluationResult>> = mutableMapOf()

    fun evalFile(category: ScriptManager.Category, scriptFile: String): ResultWithDiagnostics<EvaluationResult> {
        val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<ScriptDef>()
        return BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), compilationConfiguration, null)
            .also {
                when (category) {
                    ScriptManager.Category.COMMON -> commonCompileResults += scriptFile to it
                    ScriptManager.Category.SERVER -> serverCompileResults += scriptFile to it
                }
            }
    }

    fun findResultFor(category: ScriptManager.Category, scriptFile: String): ResultWithDiagnostics<EvaluationResult>? {
        return when (category) {
            ScriptManager.Category.COMMON -> commonCompileResults[scriptFile]
            ScriptManager.Category.SERVER -> serverCompileResults[scriptFile]
        }
    }
}