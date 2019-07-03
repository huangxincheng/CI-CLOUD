import java.util.Date
import java.text.DateFormat
import java.text.SimpleDateFormat

def version() {
    SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
    Calendar lastDate = Calendar.getInstance();
    String date = format.format(lastDate.getTime());
    def now = new Date();
    return "VS-" + date + "-" + now.time;
}


pipeline {
    agent any

    environment {
        // 总项目
        deployProject = ''
        // 部署人员名称
        deployUserName = ''
        // Git克隆地址
        cloneGitUrl  = ''
        // 发布阿里云镜像库版本
        dockerVersion = ''
        // 部署工程名称
        deployAppName = ''
        // 部署工程版本
        deployAppVersion = ''
        // 部署所用分支
        cloneGitBranch = ''
    }

    tools {
            //工具名称必须在Jenkins 管理Jenkins → 全局工具配置中预配置,配置构建工具,例如：maven,gradle
            maven 'maven'
            jdk 'java-1.8.0-openjdk'
    }

    post {
        // 部署失败
        failure {
            mail to: '249270087@qq.com',
                 subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                 body: "Something is wrong with ${env.BUILD_URL}"
        }
        // 部署成功
        success {
            script {
                echo 'deploy success'
            }
            mail to: 'limaila@163.com',
                             subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                             body: "Something is wrong with ${env.BUILD_URL}"
        }
    }

    stages {
        stage('初始化参数') {
            steps {
                script {
                    deployProject = 'CI-CLOUD'
                    deployUserName = 'huangxincheng'
                    cloneGitUrl =  'https://github.com/huangxincheng/CI-CLOUD'
                    cloneGitBranch = params.branch
                    deployAppName = params.project
                    deployAppVersion = "1.1"
                    dockerVersion = version()
                }
            }
        }

        stage('代码拉取') {
            steps {
                script {
                    echo '本地已拉取代码'
                    // 远程 k8s-master进行拉取代码
                    // sh "ssh root@47.106.95.198 sh /root/ci-cloud/step1-pullCode.sh ${cloneGitBranch} ${cloneGitUrl} ${deployProject}"
                }
            }
        }
        stage('编译代码') {
            steps {
                script {
                    echo '本地编译代码'
                    sh "mvn clean install -Dmaven.test.skip=true"
                    // 远程 k8s-master进行打包
                    // sh "ssh root@47.106.95.198 sh /root/ci-cloud/step2-compileCode.sh ${deployProject}"
                }
            }
        }
        stage('生成镜像') {
            steps {
                script {
                    echo '本地生成镜像';
                    sh "cp ./${deployAppName}/target/${deployAppName}.jar ./${deployAppName}/docker; cd ${deployAppName}/docker; docker build -f ./Dockerfile -t hxc/$deployAppName:$deployAppVersion .";
                    // 远程 k8s-master生成镜像
                    // sh "ssh root@47.106.95.198 sh /root/ci-cloud/step3-buildDockerImage.sh ${deployAppName} ${deployAppVersion} ${deployProject}"
                }
            }
        }
        stage('上传本地镜像到阿里云仓库') {
            steps {
                script {
                    echo '上传本地镜像到阿里云仓库';
                    sh "docker tag `docker images -q hxc/${deployAppName}:${deployAppVersion}` registry.cn-shenzhen.aliyuncs.com/huangxincheng/${deployAppName}:${dockerVersion}; docker push registry.cn-shenzhen.aliyuncs.com/huangxincheng/${deployAppName}:${dockerVersion}"
                    // 远程k8s-master上传镜像
                    // sh "ssh root@47.106.95.198 sh /root/ci-cloud/step4-uploadDockerImage.sh ${deployAppName} ${deployAppVersion} ${dockerVersion}"
                }
            }
        }
        stage('K8S部署项目') {
            steps {
                script {
                    echo 'K8S部署项目';
                    sh "pwd;pwd;"
                    // 远程k8s-master部署项目
                    sh "ssh root@47.106.95.198 sh /root/ci-cloud/step5-k8sDeploy.sh ${deployAppName} ${dockerVersion} ${deployProject}"
                }
            }
        }
        stage('检查项目是否正常') {
            steps {
                script {
                    echo '检查项目是否正常';
                    sh "pwd;pwd;"
                    // 远程k8s-master检查项目
                    // sh "ssh root@47.106.95.198 sh /root/ci-cloud/step6-checkProjectStatus.sh"
                }
            }
        }
        stage('回收镜像') {
            steps {
                script {
                     echo '回收镜像';
                     sh "pwd;pwd;"
                    // 远程k8s-master回收镜像
                    // sh "ssh root@47.106.95.198 sh /root/ci-cloud/step7-recycleImage.sh ${deployAppName} ${dockerVersion}"
                }
            }
        }
    }
}