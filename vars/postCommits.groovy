def call(Map config = [:]){
    sh 'touch commit_log'
    sh 'git log --pretty=format:"%H %ad" --date=iso $GIT_PREVIOUS_COMMIT..$GIT_COMMIT > commit_log'
    sh 'echo >> commit_log'
    sh 'cat commit_log'
    sh """
    #!/bin/bash

    _AUTOMATION_NAME=${config.automationName}
    _DEPLOY_ID=${config.deployId}
    _CONNECTALL_UA_URL=${config.CONNECTALL_API_URL}
    _CONNECTALL_API_KEY=${config.CONNECTALL_API_KEY}

    #echo 'Automation Name : \$_AUTOMATION_NAME'

    # Reading each line from the file
    while IFS= read -r input_text; do
        # Splitting the input text by space to separate commit ID and timestamp
        read -r commit_id timestamp <<< \$input_text

        formatted_date=\$(date -d \"\$timestamp\" +'%Y-%m-%dT%H:%M:%S%z')
        #formatted_date=\$(date -j -f '%Y-%m-%d %H:%M:%S %z' '\$timestamp' +'%Y-%m-%dT%H:%M:%S%z')

        json='{\'appLinkName\':\'\$_AUTOMATION_NAME\',\'fields\': {\'CommitId\':\'\$commit_id\',\'CommitTimestamp\':\'\$formatted_date\',\'DeployId\': \'\$_DEPLOY_ID\'}}'
        
        # Post to connectall
        curl --header 'Content-Type: application/json;charset=UTF-8' -X POST -d \"\$json\" \$_CONNECTALL_UA_URL/connectall/api/2/postRecord?apikey=\$_CONNECTALL_API_KEY
      
      
    done < commit_log
    """
    sh 'echo Completed'
}   