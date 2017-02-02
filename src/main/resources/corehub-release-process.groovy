#!groovy​

/**
 * this is a comment
 * \u2776 and \u2777 are Unicode for a black dot with a 1 and 2 in it, to make “Entering stage” lines faster to find.
 * \u2600 is Unicode for a “star” icon to make frequently referenced information faster to find. 
 */

stages{ 
    stage('nightly build') {
        node('corehub-ci-ui-slave') {
            echo 'preparing nightly build'
            build 'corehub-api-dclogic-core'
            timeout(time:5, unit:'DAYS') {
                input id: 'NightlyStaging', message: 'Promote this build to staging?', parameters: [[$class: 'PromotedBuildParameterDefinition', description: '', jobName: 'corehub-api-dclogic-core', name: 'promoteNightly', process: '']]
            }
        }
    }

    stage('staging build') {
        node('corehub-ci-ui-slave') {
            echo 'preparing stable build'
        }
    }
    stage('release build') {
        node('corehub-ci-ui-slave') {
            echo 'preparing release build'
        }
    }
}
