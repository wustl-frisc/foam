Foam: Feature Oriented Automaton Machines
=========================================

# Adding Foam to your project

To `project/plugins.sbt` add: 

```scala
addSbtPlugin("com.codecommit" % "sbt-github-packages" % "0.5.2")
```
To `build.sbt` add:

```sh
githubTokenSource := TokenSource.GitConfig("github.token")
resolvers += Resolver.githubPackages("jdeters", "foam")
libraryDependencies ++= Seq("edu.wustl.sbs" %% "foam" % "<verson>")
```
Replace `<verson>` with the version you are targeting.

You'll now have access to the following:
```scala
import foam._
import foam.aspects._
import foam.examples._
```
- `import foam._` contains the basic classes for creating NFAs, converting to DFAs, interfacing with Chisel and generating Graphvis.
- `import foam.aspects._` contains the aspect oriented fucntionality used for creating features.
- `import foam.examples._` contains a few examples of feature oriented finite state machines.
