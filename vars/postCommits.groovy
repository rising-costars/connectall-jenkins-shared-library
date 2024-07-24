def call(Map config = [:]){
    sh 'touch commit_log'
    sh 'git log --pretty=format:"%H %ad %s" --date=iso8601-strict $GIT_PREVIOUS_COMMIT..$GIT_COMMIT > commit_log'
    sh 'cat commit_log'
    sh '''
    #!/bin/bash

    # Reading each line from the file
    while IFS= read -r input_text; do
        # Splitting the input text by space to separate commit ID and timestamp
        read -r commit_id timestamp <<< "$input_text"

        # Extracting date and time
        date="${timestamp%%T*}"
        time="${timestamp##*T}"

        # Extracting month and day from date
        month_day=$(echo $date | cut -d'-' -f2,3 | tr '-' '-')

        # Constructing the JSON string
        json_str="{\"commitId\": \"$commit_id\", \"date\":\"$month_day'T'$time\"}"

        echo $json_str
    done < "commit_log"
    '''
    sh 'echo Completed'
}   