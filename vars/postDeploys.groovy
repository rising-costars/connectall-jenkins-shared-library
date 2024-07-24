def call(Map config = [:]){
    git log --pretty=format:"%H %ad %s" --date=format:"%Y-%m-%d %H:%M:%S" $env.GIT_PREVIOUS_COMMIT..$env.GIT_COMMIT > commit_log
    cat commit_log
}