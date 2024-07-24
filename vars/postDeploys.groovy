def call(Map config = [:]){
    sh """
    #!/bin/bash

    
    _CONNECTALL_UA_URL=${config.CONNECTALL_API_URL}
    _CONNECTALL_API_KEY=${config.CONNECTALL_API_KEY}
    _AUTOMATION_NAME=${config.AutomationName}
    _DEPLOY_ID=${config.DeployId}
    _IS_SUCCESSFUL=${config.Build_Result}
    _BUILD_START_TIME=${config.Build_Start_Time}
    _BUILD_END_TIME=${config.Build_Finish_Time}

    #echo 'Automation Name : \$_AUTOMATION_NAME'
    json='{\'appLinkName\':\'\$_AUTOMATION_NAME\',\'fields\': {\'IsSuccessful\':\'\$_IS_SUCCESSFUL\',\'TimeCreated\':\'\$_BUILD_START_TIME\',\'TimeDeployed\':\'\$_BUILD_END_TIME\',\'Id\': \'\$_DEPLOY_ID\'}}'

     # Post to connectall
    curl --header 'Content-Type: application/json;charset=UTF-8' -X POST -d \"\$json\" \$_CONNECTALL_UA_URL/connectall/api/2/postRecord?apikey=\$_CONNECTALL_API_KEY
         
    """
    sh 'echo Completed'