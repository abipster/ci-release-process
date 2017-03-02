stage('nightly'){

    def yumUser = "root"
    def yumHost = "172.28.23.224"
    def svnRevision = "0.1.0.1234"

    def remoteExecutor = new RemoteYumServer(yumUser, yumHost)  
    echo remoteExecutor.getRunnableCommand("mkdir -p /var/www/CoreHub/nightly/${svnRevision}/datacentre/ingg")

    setupYumDirs(remoteExecutor, svnRevision)
}

def setupYumDirs(remoteYumServer, svnRevision){

    for(String module in remoteYumServer.dirStructure){
        echo "module.key = ${module.key}, module.value = ${module.value}"

        for(String dir in remoteYumServer.dirStructure[module.key]){
            echo remoteYumServer.getRunnableCommand("mkdir -p /var/www/CoreHub/nightly/${svnRevision}/${module.key}/${dir}")
        }
    }
}

enum YumModule {datacentre, site, stub}

class RemoteYumServer{

    String user
    String host
    Map<String, List> dirStructure = [:]

    RemoteYumServer(user, host){
        this.user = user
        this.host = host

        def datacentreDirs = ["ingg", "thirdparty", "database"]
        def siteDirs = ["ingg", "thirdparty"]
        def stubDirs = ["ingg", "thirdparty", "stub"]
        dirStructure.'datacentre'= datacentreDirs
        dirStructure.'site'= siteDirs
        dirStructure.'stub'= stubDirs
        
    }

    def getRunnableCommand(command){
        return "ssh -o StrictHostKeyChecking=no ${user}@${host} ${command}"
    }
}
