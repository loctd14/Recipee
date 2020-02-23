package com.tdl.recipee.support

import android.content.Context
import com.tdl.recipee.data.model.Ingredient
import com.tdl.recipee.data.model.Recipe
import com.tdl.recipee.data.model.Step
import io.reactivex.Observable
import io.reactivex.Single
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by loc.ta on 2/22/2020.
 */
class AssetManager(private val context: Context) {

    private fun readFromXML(): Document {
        val xml = context.assets.open("recipetypes.xml")
        val builderFactory = DocumentBuilderFactory.newInstance()
        val docBuilder = builderFactory.newDocumentBuilder()
        return docBuilder.parse(xml)
    }

    fun loadFromAsset(): Observable<List<Recipe>> {
        val document = readFromXML()
        val nodeList = document.getElementsByTagName("recipe")
        val result = mutableListOf<Recipe>()

        for (i in 0 until nodeList.length) {
            if (nodeList.item(0).nodeType == Node.ELEMENT_NODE) {
                val element = nodeList.item(i) as Element

                val ingredients = mutableListOf<Ingredient>()
                val ingredientNodeLists = element.getElementsByTagName("ingredient")
                for (j in 0 until ingredientNodeLists.length) {
                    if (ingredientNodeLists.item(0).nodeType == Node.ELEMENT_NODE) {
                        val ingredientElement = ingredientNodeLists.item(j) as Element
                        ingredients.add(
                            Ingredient(
                                name = getNodeValue("name", ingredientElement),
                                amount = getNodeValue("amount", ingredientElement)
                            )
                        )
                    }
                }

                val steps = mutableListOf<Step>()
                val stepNodeList = element.getElementsByTagName("step")
                for (j in 0 until stepNodeList.length) {
                    if (stepNodeList.item(0).nodeType == Node.ELEMENT_NODE) {
                        val stepElement = stepNodeList.item(j) as Element
                        steps.add(
                            Step(
                                name = getNodeValue("name", stepElement),
                                description = getNodeValue("description", stepElement)
                            )
                        )
                    }
                }

                val recipe = Recipe(
                    recipeId = getNodeValue("recipeID", element),
                    name = getNodeValue("name", element),
                    category = getNodeValue("category", element),
                    ingredients = ingredients,
                    steps = steps,
                    imageUrl = getNodeValue("url", element)
                )
                result.add(recipe)
            }
        }
        return Observable.just(result)
    }

    private fun getNodeValue(tag: String, element: Element): String {
        val nodeList = element.getElementsByTagName(tag)
        val node = nodeList.item(0)
        if (node != null) {
            if (node.hasChildNodes()) {
                val child = node.firstChild
                while (child != null) {
                    if (child.nodeType == Node.TEXT_NODE) {
                        return child.nodeValue
                    }
                }
            }
        }
        return ""
    }
}
