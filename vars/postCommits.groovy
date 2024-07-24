def call(Map config = [:]){
    sh "touch ${config.GitRepoLoc}/commit_log"
    sh "git --git-dir=${config.GitRepoLoc}/.git log --pretty=format:'%H %ad' --date=iso ${config.PrevSuccessBuildCommit}..${config.CurrentBuildCommit} > ${config.GitRepoLoc}/commit_log"
    sh "echo >> ${config.GitRepoLoc}/commit_log"
    sh "cat ${config.GitRepoLoc}/commit_log"
    sh """#!/bin/bash
    _AUTOMATION_NAME="${config.AutomationName}"
    _DEPLOY_ID="${config.DeployId}"
    echo "Automation Name: \$_AUTOMATION_NAME"
    echo "Deploy ID: \$_DEPLOY_ID"

    commit_id="abc123"
    formatted_date="2024-01-01"

    json="{
        \\"appLinkName\\": \"\$_AUTOMATION_NAME\",
        &quot;fields&quot;": {
            \"CommitId\": \"\$commit_id\",
            \"CommitTimestamp\": \"\$formatted_date\",
            \"DeployId\": \"\$_DEPLOY_ID\"
        }
    }"

    echo "Json : \$json"
    
    """
    sh "echo Completed"
}   