package com.github.shiraji.colorfinder.view

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.JDOMUtil
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.ListSpeedSearch
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBList
import com.intellij.util.ui.JBUI
import java.awt.Color
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JPanel

class ColorFinderToolWindowPanel(val project: Project) : SimpleToolWindowPanel(false, true), DataProvider, Disposable {

    init {
        setContent()
        setToolbar(createToolbarPanel())
    }

    private fun setContent() {
        val colorMap = mutableMapOf<String, String>()
        FilenameIndex.getFilesByName(project, "colors.xml", GlobalSearchScope.projectScope(project)).forEach {
            colorFile ->
            JDOMUtil.loadDocument(colorFile.text).rootElement.getChildren("color").forEach {
                colorElement ->
                colorElement.getAttribute("name").value?.let {
                    colorMap.put("R.color.$it", colorElement.text)
                }
            }
        }
        val listModel = DefaultListModel<String>()
        colorMap.forEach {
            listModel.addElement(it.key)
        }
        val list = JBList(listModel)
        list.cellRenderer = object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(list: JList<*>?, value: Any?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
                val component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                colorMap[list?.model?.getElementAt(index)]?.let {
                    if (it.length == 7) {
                        background = Color.decode(it)
                    } else if (it.length == 9) {
                        background = Color(java.lang.Long.decode(it).toInt(), true)
                    }
                }
                foreground = Color.BLACK
                return component
            }
        }
        // Google recommended height!!!
        list.fixedCellHeight = 48

        ListSpeedSearch(list)
        setContent(ScrollPaneFactory.createScrollPane(list))
    }

    private fun createToolbarPanel(): JPanel {
        val group = DefaultActionGroup()
        group.add(RefreshAction())
        val actionToolBar = ActionManager.getInstance().createActionToolbar("ColorFinder", group, true)
        return JBUI.Panels.simplePanel(actionToolBar.component)
    }

    override fun dispose() {
    }

    inner class RefreshAction() : AnAction("Reload colors.xml", "Reload colors.xml", AllIcons.Actions.Refresh) {
        override fun actionPerformed(e: AnActionEvent?) {
            setContent()
        }
    }
}