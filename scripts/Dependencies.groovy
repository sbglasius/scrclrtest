
includeTargets << grailsScript("_GrailsInit")

target(dependencies: "The description of the script goes here!") {
    def dependenciesReport =  grailsSettings.dependencyManager.resolve('runtime')
    dependenciesReport.resolvedArtifacts.each { dependency ->
        println "runtime('${dependency.dependency.pattern}')"
    }
}

setDefaultTarget(dependencies)
