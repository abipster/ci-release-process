stage('nightly'){

    def yumUser = "root"
    def yumHost = "172.28.23.224"
    
    def remoteExecutor = new RemoteExecutor(yumUser, yumHost)  
    remoteExecutor.runRemoteCommand("mkdir /datacentre/0.1.0.1234/ingg")

}



class RemoteExecutor{

    String user
    String host

    RemoteExecutor(user, host){
        this.user = user
        this.host = host
    }

    def runRemoteCommand(command){
        echo "ssh -o StrictHostKeyChecking=no ${user}@${host} ${command}"
    }
}