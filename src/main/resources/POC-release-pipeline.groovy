#!groovy

/**
 * This a proof-of-concept of jenkins pipelines to enable enear release process. It is still a work in progess.
 * In this POC, I'm only considering one package-control not multiple, that will be done in the future.
 *
 * Projects/Jobs list:
 *      - demo-component (package control)
 *      - demo-component-test (robot component tests for DCLogic, Sitecontroller, ...)
 *      - demo-sanity-checks (robot integration sanity check battery)
 *      - demo-integration-tests (robot full integration battery)
 *
 *  Open Questions: 
 *      - 
*/


def promoteNightlyInput = "Yes"
def promoteStableInput = "No"
def promoteStagingInput = "No"
def releaseVersionInput = "No"

milestone()
stage('nightly build') {
    echo 'preparing nightly build'

    parallel nightlyBuildInParallel()

    parallel nightlyTestsInParallel()

    
    node{
        /* TODO: create sanity check test environment using vagrant  */ 
        build 'demo-sanity-checks'
        /* TODO: destroy sanity check test environment using vagrant  */   
    }

    /** 
     * TODO: What to do if user does not give input (after 1 day timout)?
     *      - discard build?
     *      - promote anyway?
    */
    /* ask user if build should be promoted to staging */
    try{
        timeout(time:1, unit:'MINUTES') {
            promoteNightlyInput = input id: 'nightlyPromotion', message: 'Promote this build to Stable?', ok: 'Submit', parameters: [[$class: 'ChoiceParameterDefinition', choices: 'Yes\nNo', description: '', name: 'promoteNightly']]
            echo '\u2705 Nightly Build promoted by user choice'
        }
    } catch (err) {
        def user = err.getCauses()[0].getUser()
        echo '\u2705 Nightly Build promoted by ${user}'
    }
}

if(promoteNightlyInput == 'Yes'){
            
    milestone()
    
    stage('stable build') {
        node{
            echo 'preparing stable build'
            
            build 'demo-integration-tests'
        }

        /** 
         * TODO: What to do if user does not give input (after 5 day timout)?
         *      - discard build?
         *      - promote anyway?
        */
        /* ask user if build should be promoted to staging */
        try{
            timeout(time:5, unit:'MINUTES') {
                promoteStableInput = input id: 'stablePromotion', message: 'Promote this build to Staging?', ok: 'Submit', parameters: [[$class: 'ChoiceParameterDefinition', choices: 'No\nYes', description: '', name: 'promoteStable']]
                echo '\u2705 Stable Build promoted by user choice'
            }
        } catch (err) {
            def user = err.getCauses()[0].getUser()
            echo '\u2705 Nightly Build promoted by ${user}'
        }
    }

    if(promoteStableInput == 'Yes'){
        
        milestone()
        stage('staging build') {
            node {
                echo 'preparing staging build'
                
                /* TODO: create integration test environment using vagrant  */ 
                build 'demo-integration-tests'
                /* TODO: destroy integration test environment using vagrant  */ 

                /* TODO: create manual test staging environment using vagrant  */ 
            }

            /** 
             * TODO: What to do if user does not give input (after 5 day timout)?
             *      - discard build?
             *      - promote anyway?
            */
            /* ask user if build should be promoted to release */
            timeout(time:10, unit:'MINUTES') {
                promoteStagingInput = input id: 'stagingPromotion', message: 'Promote this build to Release?', ok: 'Submit', parameters: [[$class: 'ChoiceParameterDefinition', choices: 'No\nYes', description: '', name: 'promoteStaging']]
            }
            
            /* TODO: destroy manual test staging environment using vagrant  */
        }

        if(promoteStagingInput == 'Yes'){
            echo '\u2705 Staging Build promoted by user choice'
            
            milestone()            
            stage('release build') {
                node {
                    echo 'preparing release build'

                    /* TODO: create manual test release environment using vagrant  */ 
                }
            }

            /* ask user if build should be released */
            timeout(time:15, unit:'MINUTES') {
                releaseVersionInput = input id: 'releasePromotion', message: 'Release this build?', ok: 'Submit', parameters: [[$class: 'ChoiceParameterDefinition', choices: 'No\nYes', description: '', name: 'releaseVersion']]

                if(releaseVersionInput == 'Yes'){
                    /* TODO: Rename package-control to lose the 'RC' part of the name and release it again */
                    /* TODO: copy/promote the staging yum repo to the yum release repo after the new release */ 
                    echo '\u2705 Version released by user choice'
                }else{
                    /* TODO: do nothing */
                    error '\u2717 Version discarded by user choice'
                }
            }
            /* TODO: destroy manual test release environment using vagrant  */ 
        }else{
            error '\u2717 Stable Build discarded by user choice'
        }
    }else{
        error '\u2717 Staging Build discarded by user choice'
    }
}else{
    error '\u2717 Nightly Build discarded by user choice'
}


def nightlyBuildInParallel() {
    def branches = [:]

    branches["datacentre"] = {
        node {
            echo "Build DCLogic"            
            build 'demo-component'
            
            /* TODO: perform release of package-control */ 
            echo "Release Datacentre nightly build to artifactory"

            /* TODO: build nightlies yum repo  */ 
            echo "Add Datacentre nightly to yum repo"
        }
    }
    branches["site"] = {
        node {
            echo "Build Site"
            build 'demo-component'

            /* TODO: perform release of package-control */ 
            echo "Release Site nightly build to artifactory"
            
            /* TODO: build nightlies yum repo  */ 
            echo "Add Site nightly to yum repo"
        }
    }
    branches["failFast"] = false
}

def nightlyTestsInParallel() {
    def branches = [:]

    branches["robot-dcLogic"] = {
        node{        
            echo "Component test: DCLogic"            
            /* TODO: create component test environment using vagrant  */ 
            build 'demo-component-test'
            /* TODO: destroy component test environment using vagrant  */ 
        }
    }
    branches["robot-operatorUI"] = {
        node{        
            echo "Component test: operatorUI"            
            /* TODO: create component test environment using vagrant  */ 
            build 'demo-component-test'
            /* TODO: destroy component test environment using vagrant  */ 
        }
    }
    branches["robot-ums"] = {
        node{        
            echo "Component test: ums"            
            /* TODO: create component test environment using vagrant  */ 
            build 'demo-component-test'
            /* TODO: destroy component test environment using vagrant  */ 
        }
    }
    branches["robot-sitecontroller"] = {
        node{        
            echo "Component test: sitecontroller"            
            /* TODO: create component test environment using vagrant  */ 
            build 'demo-component-test'
            /* TODO: destroy component test environment using vagrant  */ 
        }
    }
    branches["failFast"] = false
}

/**
 * EXTRA CONFIGURATIONS
 *
 * Approve the following configurations in "Manage Jenkins > In-process Script Approval" 
 *
 *  - method org.jenkinsci.plugins.workflow.steps.FlowInterruptedException getCauses
 *  - method org.jenkinsci.plugins.workflow.support.steps.input.Rejection getUser
 *  - new hudson.model.ChoiceParameterDefinition java.lang.String java.lang.String[] java.lang.String
 *
*/


/**
 * this is a comment
 * \u2776 and \u2777 are Unicode for a black dot with a 1 and 2 in it, to make “Entering stage” lines faster to find.
 * \u2600 is Unicode for a “star” icon to make frequently referenced information faster to find. 
 */