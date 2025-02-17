#!/usr/bin/env kotlin

@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.11.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV4
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV3
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.WorkflowCall
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Fix workflow",
    on = listOf(
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(id = "fix-branch", runsOn = UbuntuLatest, `if` = "github.ref_name != github.event.repository.default_branch") {
        uses(name = "Check out", action = CheckoutV4())
        uses(
            name = "Setup Java",
            action = SetupJavaV4(distribution = SetupJavaV4.Distribution.Adopt, javaVersion = "17")
        )
        uses(
            name = "Record Screenshots",
            action = GradleBuildActionV3(
                arguments = "recordRoborazziDebug",
            )
        )
    }
}.writeToFile()
