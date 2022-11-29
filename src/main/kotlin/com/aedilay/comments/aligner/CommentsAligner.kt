package com.aedilay.comments.aligner

import com.aedilay.comments.aligner.configuration.AmogusConfiguration
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import org.apache.http.util.TextUtils

class CommentsAligner : AnAction() {
    private val amogusConfig = AmogusConfiguration.amogusConfiguration

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        val document = editor.document
        val model = editor.selectionModel

        val text = document.text
        if (TextUtils.isEmpty(text)) {
            Messages.showMessageDialog("Selected text is empty", "Comment Formatter", Messages.getInformationIcon())
            return
        }

        val strs = text.split("\n")
        val longestChatSeq = strs.filter { it.contains("//") } .maxOf { it.substringBefore("//").length - 1 }
        val sb = StringBuilder()

        strs.withIndex().forEach {
            if (!it.value.contains("//")) {
                sb.append(it.value)
            } else if (longestChatSeq == it.value.indexOf("//")) {
                sb.append(it.value)
            } else {
                val newStr = generateFormattedLine(it.value, longestChatSeq)
                sb.append(newStr)
            }

            if (it.index != strs.size - 1)
                sb.append("\n")
        }

        val randomAmogus = amogusConfig.random().takeUnless { it == amogusConfig[0] }
            ?: amogusConfig.random()

        sb.append(randomAmogus)

        WriteCommandAction.runWriteCommandAction(project) {
            document.setText(sb.toString())
        }
        model.removeSelection()
    }

    private fun generateFormattedLine(originStr: String, longestCharSequence: Int): String {
        val start = originStr.indexOf(";")
        val (left, right) = originStr.split(";")
        val numOfSpaces = right.substringBefore("//").length
        return left.plus(";").plus(" ".repeat(longestCharSequence - start - numOfSpaces)).plus(right)
    }
}