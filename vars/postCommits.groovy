def call(Map config = [:]){
    sh 'touch commit_log'
    sh 'git log --pretty=format:"%H %ad" --date=iso $GIT_PREVIOUS_COMMIT..$GIT_COMMIT > commit_log'
    sh 'echo >> commit_log'
    sh 'cat commit_log'
    sh '''
    #!/bin/bash

    # Reading each line from the file
    while IFS= read -r input_text; do
        # Splitting the input text by space to separate commit ID and timestamp
        read -r commit_id timestamp <<< "$input_text"

        formatted_date=$(date -d "$original_date" +"%Y-%m-%dT%H:%M:%S%z")
        #formatted_date=$(date -j -f "%Y-%m-%d %H:%M:%S %z" "$timestamp" +"%Y-%m-%dT%H:%M:%S%z")

        # Constructing the JSON string
        json_str="{\"commitId\": \"$commit_id\", \"date\":\"$formatted_date\"}"

        echo $json_str
    done < commit_log
    '''
    sh 'echo Completed'
}   