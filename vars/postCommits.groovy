def call(Map config = [:]){
    sh "touch ${config.GitRepoLoc}/commit_log"
    sh "git --git-dir=${config.GitRepoLoc}/.git log --pretty=format:'%H %ad' --date=iso ${config.PrevSuccessBuildCommit}..${config.CurrentBuildCommit} > ${config.GitRepoLoc}/commit_log"
    sh "echo >> ${config.GitRepoLoc}/commit_log"
    sh "cat ${config.GitRepoLoc}/commit_log"
    sh """
    #!/bin/bash

    _AUTOMATION_NAME=${config.AutomationName}
    _DEPLOY_ID=${config.DeployId}

    _GIT_REPO=${config.GitRepoLoc}
    _GIT_PREV_COMMIT=${config.PrevSuccessBuildCommit}
    _GIT_CURR_COMMIT=${config.CurrentBuildCommit}

    _CONNECTALL_UA_URL=${config.ConnectALL_Api_Url}
    _CONNECTALL_API_KEY=${config.ConnectALL_Api_Key}

    #echo 'Automation Name : \$_AUTOMATION_NAME'

    # Reading each line from the file
    while IFS= read -r input_text; do
        # Splitting the input text by space to separate commit ID and timestamp
        read -r commit_id timestamp <<< \$input_text

        formatted_date=\$(date -d \"\$timestamp\" +'%Y-%m-%dT%H:%M:%S%z')
        #formatted_date=\$(date -j -f '%Y-%m-%d %H:%M:%S %z' '\$timestamp' +'%Y-%m-%dT%H:%M:%S%z')

        json="{\"appLinkName\":\"\$_AUTOMATION_NAME\",\"fields\": {\"CommitId\":\"\$commit_id\",\"CommitTimestamp\":\"\$formatted_date\",\"DeployId\": \"\$_DEPLOY_ID\"}}"
        
        # Post to connectall
        curl --header 'Content-Type: application/json;charset=UTF-8' -X POST -d \"\$json\" \$_CONNECTALL_UA_URL/connectall/api/2/postRecord?apikey=\$_CONNECTALL_API_KEY
      
      
    done < ${config.GitRepoLoc}/commit_log
    """
    sh 'echo Completed'
}   