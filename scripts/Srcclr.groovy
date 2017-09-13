includeTargets << grailsScript("_GrailsInit")

target(srcclr: "SourceClear analysis of dependencies") {
    Map<String, String> gradleScopeMap = [
            runtime: 'runtime',
            compile: 'compile',
            test   : 'textCompile'
    ]
    List<String> scopes = ['compile', 'runtime']
    def g = grailsSettings
    def dependencyManager = grailsSettings.dependencyManager
    def dependenciesByScope = scopes.inject([:]) { map, scope ->
        List currentScope = map[scope] ?: []
        currentScope.addAll(dependencyManager.grailsDependenciesByScope[scope] ?: [])
        currentScope.addAll(dependencyManager.grailsPluginDependenciesByScope[scope] ?: [])
        map[scope] = currentScope
        return map
    }
    def gradleFile = new File(grailsSettings.baseDir, 'scbuild.gradle')

    def dependenciesString = dependenciesByScope.collect { String scope, List dependencies ->
        dependencies.collect { dependency ->

            "           ${gradleScopeMap[scope]}('${dependency}')"
        }
    }.flatten()

    def buildString = """
        plugins {
            id "com.srcclr.gradle" version "2.2.7"
        }
        apply plugin: "groovy"
        
        dependencies {
            ${dependenciesString.join('\n')}
        }
        
        srcclr {
            apiToken = project.srcclrApiToken
        }
    """.stripIndent()

    gradleFile.text = buildString
    def gradleHome = System.getenv('GRADLE_HOME')
    def exec = "${gradleHome}/bin/gradle -b ${gradleFile.absolutePath} srcclr"
    def proc = exec.execute()
    proc.waitForProcessOutput(System.out, System.err)
    gradleFile.deleteOnExit()

}

setDefaultTarget(srcclr)
