package com.cramsan.ps2link.appcore.dbg.util

/**
 * This class will handle the creation of queries.
 */
class QueryString {
    private val listtOfParameters = mutableListOf<SearchParameter>()

    fun AddComparison(key: String, modifier: SearchModifier, value: String): QueryString {
        this.listtOfParameters.add(Condition(key, modifier, value))
        return this
    }

    fun AddCommand(command: QueryCommand, value: String): QueryString {
        this.listtOfParameters.add(Command(command, value))
        return this
    }

    override fun toString(): String {
        if (this.listtOfParameters.size == 0) {
            return ""
        } else {
            val builder = StringBuilder()
            for (param in listtOfParameters) {
                builder.append("$param&")
            }
            return builder.toString()
        }
    }

    enum class SearchModifier private constructor(private val mod: String) {
        EQUALS("="), LESSTHAN("=<"), GREATERTHAN("=>"), LESSEQUALTHAN("=["), GREATEREQUALTHAN("=]"), STARTSWITH(
            "=^"
        ),
        NOTCONTAIN("=!");

        override fun toString(): String {
            return this.mod
        }
    }

    enum class QueryCommand private constructor(private val command: String) {
        SHOW("c:show"), HIDE("c:hide"), SORT("c:sort"), HAS("c:has"), JOIN("c:join"), RESOLVE("c:resolve"), CASE(
            "c:case"
        ),
        LIMIT("c:limit"), LIMITPERDB(
            "c:limitPerDB"
        ),
        INCLUDENULL("c:includeNull"), START("c:start");

        override fun toString(): String {
            return this.command
        }
    }

    private open inner class SearchParameter {
        protected var value: String? = null
    }

    private inner class Condition(
        private val key: String,
        private val modifier: SearchModifier,
        value: String
    ) : SearchParameter() {

        init {
            this.value = value
        }

        override fun toString(): String {
            return this.key + this.modifier.toString() + this.value
        }
    }

    private inner class Command(private val command: QueryCommand, value: String) :
        SearchParameter() {

        init {
            this.value = value
        }

        override fun toString(): String {
            return this.command.toString() + EQUALS + this.value
        }
    }

    companion object {
        val QUESTION_MARK = "?"
        private val EQUALS = "="

        fun generateQeuryString(): QueryString {
            return QueryString()
        }
    }
}
