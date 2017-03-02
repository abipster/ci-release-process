parallel datacentre: {
        node {
            echo "Build DCLogic"            
            build 'demo-component'
            /* TODO: perform release of package-control */ 
            /* TODO: build nightlies yum repo  */ 
        }
    }, site: {
        node {
            echo "Build Site"
            build 'demo-component'
            /* TODO: perform release of package-control */ 
            /* TODO: build nightlies yum repo  */ 
        }
    }, failFast: false

node{        
        /* TODO: create component test environment using vagrant  */ 
        build 'demo-component-test'
        /* TODO: destroy component test environment using vagrant  */ 
    }
