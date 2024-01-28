#!/usr/bin/env groovy

@Library('enlibs@main')_

pipeline {
    agent { node { label 'agent1' } }
    stages {
        stage("Prepare") {
            steps {
              script {
                enlibs.sayHello ""
              }
            }
        }
    }
}
