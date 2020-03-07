package com.cesarandres.ps2link.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView

import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.VolleyError
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.DBGCensus.Verb
import com.cesarandres.ps2link.dbg.content.CharacterDirective
import com.cesarandres.ps2link.dbg.content.CharacterDirectiveObjective
import com.cesarandres.ps2link.dbg.content.CharacterDirectiveTier
import com.cesarandres.ps2link.dbg.content.CharacterDirectiveTree
import com.cesarandres.ps2link.dbg.content.Directive
import com.cesarandres.ps2link.dbg.content.DirectiveTier
import com.cesarandres.ps2link.dbg.content.DirectiveTreeCategory
import com.cesarandres.ps2link.dbg.content.response.Characters_directive_list
import com.cesarandres.ps2link.dbg.content.response.Characters_directive_objective_list
import com.cesarandres.ps2link.dbg.content.response.Characters_directive_tier_list
import com.cesarandres.ps2link.dbg.content.response.Characters_directive_tree_list
import com.cesarandres.ps2link.dbg.content.response.Directive_list
import com.cesarandres.ps2link.dbg.content.response.Directive_tier_list
import com.cesarandres.ps2link.dbg.content.world.Name_Multi
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.util.QueryString
import com.cesarandres.ps2link.dbg.util.QueryString.SearchModifier
import com.cesarandres.ps2link.dbg.view.DirectiveTreeCategoryListAdapter

import java.util.ArrayList
import java.util.HashMap

/**
 * This fragment will display the directives of a given user. This fragment is
 * designed to be part of a profile pager.
 */
class FragmentDirectiveList : BaseFragment() {

    private var profileId: String? = null
    private var adapter: DirectiveTreeCategoryListAdapter? = null
    private var expandableListView: ExpandableListView? = null

    private var charactersDirective: ArrayList<CharacterDirective>? = null
    private var charactersDirectiveObjective: ArrayList<CharacterDirectiveObjective>? = null
    private var charactersDirectiveTrees: ArrayList<CharacterDirectiveTree>? = null
    private var charactersDirectiveTiers: ArrayList<CharacterDirectiveTier>? = null
    private var directiveTiers: ArrayList<DirectiveTier>? = null
    private var directives: ArrayList<Directive>? = null
    private var charactersDirectiveTreeCategories: ArrayList<DirectiveTreeCategory>? = null

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_directives_list, container, false)
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.profileId = arguments!!.getString("PARAM_0")
        this.expandableListView =
            activity!!.findViewById<View>(R.id.expandableListViewDirectiveList) as ExpandableListView
        this.adapter = DirectiveTreeCategoryListAdapter(this, expandableListView!!, dbgCensus, imageLoader)
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        downloadDirectivesList(this.profileId)
    }

    /**
     * @param character_id Character id that will be used to request the list of directives
     */
    fun downloadDirectivesList(profileId: String?) {
        this.setProgressButton(true)
        //TODO Fix this language use
        val url = dbgCensus.generateGameDataRequest(
            Verb.GET, PS2Collection.CHARACTERS_DIRECTIVE, null,
            QueryString.generateQeuryString().AddComparison(
                "character_id",
                SearchModifier.EQUALS,
                profileId!!
            ).AddComparison("c:lang", SearchModifier.EQUALS, "en").AddComparison(
                "c:limit",
                SearchModifier.EQUALS,
                "5000"
            )
        )!!.toString()

        val success = Listener<Characters_directive_list> { response ->
            charactersDirective = response.characters_directive_list
            downloadDirectivesObjectives(profileId)
        }

        val error = ErrorListener { setProgressButton(false) }

        dbgCensus.sendGsonRequest(url, Characters_directive_list::class.java, success, error, this)
    }

    /**
     * @param character_id Character id that will be used to request the list of objectives
     */
    fun downloadDirectivesObjectives(profileId: String?) {
        this.setProgressButton(true)
        val url = "http://census.soe.com/get/ps2:v2/" +
                "characters_directive_objective?character_id=" + profileId +
                "&c:lang=en&c:limit=5000&c:join=objective"


        val success = Listener<Characters_directive_objective_list> { response ->
            charactersDirectiveObjective = response.characters_directive_objective_list
            downloadDirectiveTrees(profileId)
        }

        val error = ErrorListener { setProgressButton(false) }

        dbgCensus.sendGsonRequest(
            url,
            Characters_directive_objective_list::class.java,
            success,
            error,
            this
        )
    }

    /**
     * @param character_id Character id that will be used to request the list of directive trees
     */
    fun downloadDirectiveTrees(profileId: String?) {
        this.setProgressButton(true)
        val url = "http://census.soe.com/get/ps2:v2/" +
                "characters_directive_tree?character_id=" + profileId +
                "&c:lang=en&c:limit=5000&c:join=directive_tree"

        val success = Listener<Characters_directive_tree_list> { response ->
            charactersDirectiveTrees = response.characters_directive_tree_list
            downloadAllDirectiveTiers(profileId)
        }

        val error = ErrorListener { setProgressButton(false) }

        dbgCensus.sendGsonRequest(
            url,
            Characters_directive_tree_list::class.java,
            success,
            error,
            this
        )
    }

    fun downloadAllDirectiveTiers(profileId: String?) {
        this.setProgressButton(true)
        val url = "http://census.soe.com/get/ps2:v2/" + "directive_tier?&c:lang=en&c:limit=5000"

        val success = Listener<Directive_tier_list> { response ->
            directiveTiers = response.directive_tier_list
            downloadAllDirectives(profileId)
        }

        val error = ErrorListener { setProgressButton(false) }

        dbgCensus.sendGsonRequest(url, Directive_tier_list::class.java, success, error, this)
    }

    fun downloadAllDirectives(profileId: String?) {
        this.setProgressButton(true)
        val url = "http://census.soe.com/get/ps2:v2/" + "directive?c:limit=5000&c:lang=en"

        val success = Listener<Directive_list> { response ->
            directives = response.directive_list
            downloadDirectiveTiers(profileId)
        }

        val error = ErrorListener { setProgressButton(false) }

        dbgCensus.sendGsonRequest(url, Directive_list::class.java, success, error, this)
    }

    /**
     * @param character_id Character id that will be used to request the list of directive tiers
     */
    fun downloadDirectiveTiers(profileId: String?) {
        this.setProgressButton(true)
        val url = "http://census.soe.com/get/ps2:v2/" +
                "characters_directive_tier?character_id=" + profileId +
                "&c:lang=en&c:limit=5000"

        val success = Listener<Characters_directive_tier_list> { response ->
            charactersDirectiveTiers = response.characters_directive_tier_list
            generateDirectiveMap()
            adapter!!.setCategories(this!!.charactersDirectiveTreeCategories!!)
            expandableListView!!.setAdapter(adapter)
            setProgressButton(false)
        }

        val error = ErrorListener { setProgressButton(false) }

        dbgCensus.sendGsonRequest(
            url,
            Characters_directive_tier_list::class.java,
            success,
            error,
            this
        )
    }

    fun generateDirectiveMap(): Boolean {
        //TODO Completely refactor this method
        this.charactersDirectiveTreeCategories = ArrayList()

        val treeMap = HashMap<String, CharacterDirectiveTree>()
        val characterTierMap = HashMap<String, CharacterDirectiveTier>()
        val tierMap = HashMap<String, DirectiveTier>()
        val directiveMap = HashMap<String, Directive>()
        val categoryMap = HashMap<String, DirectiveTreeCategory>()

        //Generate TreeMap
        for (directiveTree in charactersDirectiveTrees!!) {
            val newCategoryId =
                directiveTree.directive_tree_id_join_directive_tree!!.directiveTreeCategoryId
            val newDirectiveTreeId = directiveTree.directive_tree_id

            if (!categoryMap.containsKey(newCategoryId)) {
                val name = Name_Multi()

                val newDirectiveTreeCategory = DirectiveTreeCategory()
                if (newCategoryId!!.equals("3", ignoreCase = true)) {
                    name.en = "Infantry"
                } else if (newCategoryId.equals("4", ignoreCase = true)) {
                    name.en = "Vehicle"
                } else if (newCategoryId.equals("5", ignoreCase = true)) {
                    name.en = "Strategic"
                } else if (newCategoryId.equals("6", ignoreCase = true)) {
                    name.en = "Prestige"
                } else if (newCategoryId.equals("7", ignoreCase = true)) {
                    name.en = "Weapons"
                } else if (newCategoryId.equals("8", ignoreCase = true)) {
                    name.en = "Events"
                } else {
                    name.en = "Others"
                }
                newDirectiveTreeCategory.name = name
                newDirectiveTreeCategory.directiveTreeCategoryId = newCategoryId
                categoryMap[newCategoryId] = newDirectiveTreeCategory
                this.charactersDirectiveTreeCategories!!.add(newDirectiveTreeCategory)
            }
            categoryMap[newCategoryId]!!.registerCharacterDirectiveTreeList(directiveTree)
            treeMap[newDirectiveTreeId!!] = directiveTree
        }

        for (directiveTier in directiveTiers!!) {
            val newDirectiveTierId =
                directiveTier.directiveTierId!! + directiveTier.directiveTreeId!!
            tierMap[newDirectiveTierId] = directiveTier
        }

        for (directive in directives!!) {
            val newDirectiveTierId = directive.directiveTierId!! + directive.directiveTreeId!!
            tierMap[newDirectiveTierId]!!.registerDirective(directive)
            directiveMap[directive.directiveId!!] = directive
        }

        for (directiveTier in charactersDirectiveTiers!!) {
            val newDirectiveTierId = directiveTier.directive_tier_id
            characterTierMap[newDirectiveTierId!!] = directiveTier
            val parentDirectiveTree = treeMap[directiveTier.directive_tree_id]
            val tier =
                tierMap[directiveTier.directive_tier_id!! + directiveTier.directive_tree_id!!]
            parentDirectiveTree!!.directive_tier = tier
        }

        for (directive in charactersDirective!!) {
            val dis = directiveMap[directive.directive_id]
            directive.directive_id_join_directive = dis
            for (i in charactersDirectiveObjective!!.indices) {
                if (charactersDirectiveObjective!![i].directive_id!!.equals(
                        directive.directive_id!!,
                        ignoreCase = true
                    )
                ) {
                    directive.directiveObjective = charactersDirectiveObjective!![i]
                    charactersDirectiveObjective!!.removeAt(i)
                    break
                }
            }
            val parentDirectiveTier =
                characterTierMap[directive.directive_id_join_directive!!.directiveTierId]
            parentDirectiveTier!!.registerDirective(directive)

            directiveMap[directive.directive_id]!!.directive = directive
        }

        for (category in this.charactersDirectiveTreeCategories!!) {
            category.generateValues()
            java.util.Collections.sort(category.characterDirectiveTreeList!!)
        }
        java.util.Collections.sort(this.charactersDirectiveTreeCategories!!)

        return true
    }
}