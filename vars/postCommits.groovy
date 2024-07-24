def call(Map config = [:]){
    sh 'touch commit_log'
    sh 'git log --pretty=format:"%H %ad %s" --date=iso $GIT_PREVIOUS_COMMIT..$GIT_COMMIT > commit_log'
    sh 'cat commit_log'
}