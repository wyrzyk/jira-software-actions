package com.atlassian.performance.tools.jirasoftwareactions.api.page

import com.atlassian.performance.tools.jiraactions.api.page.JiraErrors
import com.atlassian.performance.tools.jiraactions.api.page.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import java.time.Duration

class BrowseBoardsPage(
    private val driver: WebDriver
) {
    fun waitForBoardsList(): BrowseBoardsPage {
        val jiraErrors = JiraErrors(driver)
        driver.wait(
            Duration.ofSeconds(30),
            ExpectedConditions.or(
                ExpectedConditions.and(
                    ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("#ghx-header h2"), "Boards"),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("#ghx-content-main table.aui"))
                ),
                jiraErrors.anyCommonError()
            )
        )
        jiraErrors.assertNoErrors()
        return this
    }

    fun getBoardIds(): Collection<String> =
        driver.findElements(By.cssSelector(".boards-list tr"))
            .map { it.getAttribute("data-board-id") }
            .filterNotNull()

    fun getScrumBoardIds(): Collection<String> {
        driver.findElement(By.cssSelector("#ghx-manage-boards-filter a")).click()
        driver.findElement(By.className("type-filter-scrum")).click()
        return getBoardIds()
    }
}