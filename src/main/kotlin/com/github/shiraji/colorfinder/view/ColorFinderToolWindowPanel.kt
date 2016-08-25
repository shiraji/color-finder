package com.github.shiraji.colorfinder.view

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.util.ui.JBUI
import javax.swing.JPanel

class ColorFinderToolWindowPanel(val project: Project) : SimpleToolWindowPanel(false, true), DataProvider, Disposable {

    init {
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