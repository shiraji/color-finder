package com.github.shiraji.colorfinder

import com.intellij.codeInsight.CodeInsightActionHandler
import com.intellij.codeInsight.actions.CodeInsightAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

class ColorFinderAction : CodeInsightAction() {
    override fun getHandler(): CodeInsightActionHandler {
        return object : CodeInsightActionHandler {
            override fun startInWriteAction() = true

            override fun invoke(project: Project, editor: Editor, file: PsiFile) {
            }
        }
    }

    override fun actionPerformed(e: AnActionEvent?) {
        val project = e?.getData(CommonDataKeys.PROJECT) ?: return
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

    }
}