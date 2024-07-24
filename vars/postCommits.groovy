def call(Map config = [:]){
    sh "touch ${config.GitRepoLoc}/commit_log"
    sh "git --git-dir=${config.GitRepoLoc}/.git log --pretty=format:'%H %ad' --date=iso ${config.PrevSuccessBuildCommit}..${config.CurrentBuildCommit} > ${config.GitRepoLoc}/commit_log"
    sh "echo >> ${config.GitRepoLoc}/commit_log"
    sh "cat ${config.GitRepoLoc}/commit_log"
    sh '''\
    #!/bin/bash

    _AUTOMATION_NAME="${config.AutomationName}"
    #echo "Automation Name :$_AUTOMATION_NAME"
    
    '''
    sh "echo Completed"
}   