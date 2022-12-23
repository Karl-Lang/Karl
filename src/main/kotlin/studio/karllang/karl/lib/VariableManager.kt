package studio.karllang.karl.lib

import studio.karllang.karl.parser.ast.values.Value
import java.util.HashMap

object VariableManager {
    private var currentScope: Scope = Scope(null)

    @JvmStatic
    fun getVariable(name: String): Value? {
        val variablesMap = HashMap(currentScope.variables)
        variablesMap.putAll(currentScope.finalVariables)
        return variablesMap[name]
    }

    @JvmStatic
    fun isFinal(name: String): Boolean {
        return currentScope.finalVariables.containsKey(name)
    }

    @JvmStatic
    fun setVariable(name: String, value: Value, isFinal: Boolean) {
        if (isFinal) {
            currentScope.finalVariables[name] = value
        } else {
            currentScope.variables[name] = value
        }
    }

    @JvmStatic
    fun newScope() {
        val newScope = Scope(currentScope)
        for (key in currentScope.variables.keys) {
            newScope.variables[key] = currentScope.variables[key]!!
        }
        currentScope = newScope
    }

    @JvmStatic
    fun getScope(): Scope {
        return currentScope
    }

    @JvmStatic
    fun setScope(scope: Scope) {
        currentScope = scope
    }

    @JvmStatic
    fun clear() {
        currentScope = Scope(null)
    }

    class Scope(val parent: Scope?) {
        val variables: HashMap<String, Value> = HashMap()
        val finalVariables: HashMap<String, Value> = HashMap()
    }
}
