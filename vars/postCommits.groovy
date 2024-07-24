def call(Map config = [:]){
    sh 'touch commit_log'
    sh 'git log --pretty=format:"%H %ad" --date=iso $GIT_PREVIOUS_COMMIT..$GIT_COMMIT > commit_log'
    sh 'echo >> commit_log'
    sh 'cat commit_log'
    sh "export A_NAME=${config.automationName}"
    sh """
    #!/bin/bash

    echo 'Automation Name : $A_NAME'

    export _AUTOMATION_NAME=${config.automationName}
    export _DEPLOY_ID=${config.deployId}
    export _CONNECTALL_UA_URL=${config.CONNECTALL_API_URL}
    export _CONNECTALL_API_KEY=${config.CONNECTALL_API_KEY}

    #echo 'Automation Name : $_AUTOMATION_NAME'


    #export _AUTOMATION_NAME='SampleAutoamtion'
    #export _DEPLOY_ID='abc123'
    #export _CONNECTALL_UA_URL='https://connectall183.clarityrox.com/ua'
    #export _CONNECTALL_API_KEY='def123123123123098'

    # Reading each line from the file
    while IFS= read -r input_text; do
        # Splitting the input text by space to separate commit ID and timestamp
        read -r commit_id timestamp <<< '$input_text'

        formatted_date=$(date -d '$original_date' +'%Y-%m-%dT%H:%M:%S%z')
        #formatted_date=$(date -j -f '%Y-%m-%d %H:%M:%S %z' '$timestamp' +'%Y-%m-%dT%H:%M:%S%z')

        # Constructing the JSON string
        json_str='{\'commitId\': \'$commit_id\', \'date\':\'$formatted_date\'}'

        json='{\'appLinkName\':\'$_AUTOMATION_NAME\',\'fields\': {\'CommitId\':\'$commitId\',\'CommitTimestamp\':\'$formatted_date\',\'DeployId\': \'$_DEPLOY_ID\'}}'
        
        echo $json_str
        echo 'Send JSON: $json'
        echo 'via $_CONNECTALL_UA_URL/connectall/api/2/postRecord?apikey=$_CONNECTALL_API_KEY'
        
        # Post to connectall
        curl --header 'Content-Type: application/json;charset=UTF-8' -X POST -d '$json' '$_CONNECTALL_UA_URL/connectall/api/2/postRecord?apikey=$_CONNECTALL_API_KEY'
      
      
    done < commit_log
    """
    sh 'echo Completed'
}   