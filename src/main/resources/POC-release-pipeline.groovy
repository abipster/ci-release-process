#!groovy

/**
 * this is a comment
 * \u2776 and \u2777 are Unicode for a black dot with a 1 and 2 in it, to make “Entering stage” lines faster to find.
 * \u2600 is Unicode for a “star” icon to make frequently referenced information faster to find. 
 */

/* stages{ */
    def promoteNightlyInput = "Yes"
    def promoteStagingInput = "No"
    def releaseVersionInput = "No"

    milestone()
    stage('nightly build') {
        node {
            echo 'preparing nightly build'
            build 'demo-component'
            /*
            build 'demo-component-test'
            build 'demo-sanity-checks'
            */
            timeout(time:1, unit:'DAYS') {
                /*
                input id: 'nightlyStaging', message: 'Promote this build to staging?', parameters: [choice(choices: ['Yes\nNo'], description: '', name: 'promoteNightly')]
                */
                promoteNightlyInput = input id: 'nightlyPromotion', message: 'Promote this build to Staging?', ok: 'Submit', parameters: [[$class: 'ChoiceParameterDefinition', choices: 'Yes\nNo', description: '', name: 'promoteNightly']]
            }
        }
    }

    if(promoteNightlyInput == 'Yes'){
        echo '\u2705 Nightly Build promoted by user choice'
                
        milestone()
        stage('staging build') {
            node {
                echo 'preparing stable build'
                build 'demo-integration-tests'

                timeout(time:5, unit:'DAYS') {
                    promoteStagingInput = input id: 'stagingPromotion', message: 'Promote this build to Release?', ok: 'Submit', parameters: [[$class: 'ChoiceParameterDefinition', choices: 'No\nYes', description: '', name: 'promoteStaging']]
                }
            }
        }

        if(promoteStagingInput == 'Yes'){
            echo '\u2705 Staging Build promoted by user choice'
        
            milestone()            
            stage('release build') {
                node {
                    echo 'preparing release build'

                    timeout(time:10, unit:'DAYS') {
                        releaseVersionInput = input id: 'releasePromotion', message: 'Release this build?', ok: 'Submit', parameters: [[$class: 'ChoiceParameterDefinition', choices: 'No\nYes', description: '', name: 'releaseVersion']]

                        if(releaseVersionInput == 'Yes'){
                            echo '\u2705 Version released by user choice'
                        }else{
                            error '\u2717 Version discarded by user choice'
                        }
                    }
                }
            }
        }else{
            error '\u2717 Staging Build discarded by user choice'
        }
    }else{
        error '\u2717 Nightly Build discarded by user choice'
    }
/*} */
