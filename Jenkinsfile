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
            jdk 'jdk'
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
                    deployUserName = 'huangxincheng'
                    cloneGitUrl =  'https://github.com/huangxincheng/CI'
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
                    sh "ssh root@47.106.95.198 clone.sh ${cloneGitBranch} ${cloneGitUrl}"
                }
            }
        }
        stage('编译代码') {
            steps {
                script {
                    // 远程 k8s-master进行打包
                    sh "ssh root@47.106.95.198 compile.sh"
                }
            }
        }
        stage('生成镜像') {
            steps {
                script {
                    sh "ssh root@47.106.95.198 buildImage.sh ${deployAppName} ${deployAppVersion}"
                }
            }
        }
        stage('上传本地镜像到阿里云仓库') {
            steps {
                script {
                    sh "ssh root@47.106.95.198 uploadLocalDockerImageToAliYun.sh ${deployAppName} ${deployAppVersion} ${dockerVersion}"
                }
            }
        }
        stage('K8S部署项目') {
            steps {
                script {
                    sh "ssh root@47.106.95.198 deployAppByAliYunImage.sh ${deployAppName} ${dockerVersion}"
                }
            }
        }
        stage('检查项目是否正常') {
            steps {
                script {
                    // 待补充
                    echo "OK"
                }
            }
        }
    }
}