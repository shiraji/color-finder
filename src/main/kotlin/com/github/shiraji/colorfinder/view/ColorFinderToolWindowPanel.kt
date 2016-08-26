package com.github.shiraji.colorfinder.view

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.JDOMUtil
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBList
import com.intellij.util.ui.JBUI
import java.awt.Color
import javax.swing.DefaultListModel
import javax.swing.JPanel

class ColorFinderToolWindowPanel(val project: Project) : SimpleToolWindowPanel(false, true), DataProvider, Disposable {

    init {
        val colorMap = mutableMapOf<String, String>()
        FilenameIndex.getFilesByName(project, "colors.xml", GlobalSearchScope.projectScope(project)).forEach {
            colorFile ->
            JDOMUtil.loadDocument(colorFile.text).rootElement.getChildren("color").forEach {
                colorElement ->
                colorElement.getAttribute("name").value?.let {
                    colorMap.put(it, colorElement.text)
                }
            }
        }
        val listModel = DefaultListModel<ColorFinderToolWindowItem>()
        colorMap.forEach {
            val item = ColorFinderToolWindowItem()
            item.colorName.text = it.key
            item.panel.background = Color.decode(it.value)
            listModel.addElement(item)
        }
        val list = JBList(listModel)
        setContent(ScrollPaneFactory.createScrollPane(list))
        setToolbar(createToolbarPanel())
    }

    private fun createToolbarPanel(): JPanel {
        val group = DefaultActionGroup()
//        group.add(AddAction())
//        group.add(RemoveAction(IconUtil.getRemoveIcon()))
        val actionToolBar = ActionManager.getInstance().createActionToolbar("FOO", group, false)
        return JBUI.Panels.simplePanel(actionToolBar.component)
    }

    override fun dispose() {
    }
}