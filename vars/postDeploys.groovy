def call(Map config = [:]){
    sh """
    #!/bin/bash

    
    _AUTOMATION_NAME=${config.AutomationName}
    
    _DEPLOY_ID=${config.DeployId}
    _IS_SUCCESSFUL=${config.BuildResult}
    
    _BUILD_COMPONENT=${config.BuildComponent}
    _BUILD_START_TIME=${config.BuildStartTime}
    _BUILD_END_TIME=${config.BuildFinishTime}
    
    _CONNECTALL_UA_URL=${config.ConnectALL_Api_Url}
    _CONNECTALL_API_KEY=${config.ConnectALL_Api_Key}
    

    #echo 'Automation Name : \$_AUTOMATION_NAME'
    json="{&quot;appLinkName&quot;:&quot;\$_AUTOMATION_NAME&quot;,&quot;fields&quot;: {&quot;IsSuccessful&quot;:&quot;\$_IS_SUCCESSFUL&quot;,&quot;TimeCreated&quot;:&quot;\$_BUILD_START_TIME&quot;,&quot;TimeDeployed&quot;:&quot;\$_BUILD_END_TIME&quot;,&quot;Component&quot;:&quot;\$_BUILD_COMPONENT&quot;,&quot;Id&quot;: &quot;\$_DEPLOY_ID&quot;}}"

    json_str=\$(echo \$json | sed 's/&quot;/"/g')
    
    echo "Json : \$json"
    echo "Json_Formatted : \$json_str"
    # Post to connectall
    curl --header 'Content-Type: application/json;charset=UTF-8' -X POST -d \"\$json_str\" \$_CONNECTALL_UA_URL/connectall/api/2/postRecord?apikey=\$_CONNECTALL_API_KEY
         
    """
    sh 'echo Completed'
}