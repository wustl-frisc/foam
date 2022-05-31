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

# Creating an NFA
For this portion of the tutorial, we will be demostrating the Vending Machine example. To create an NFA all we need to do is create a new NFA object.

```scala
val start = SimpleStateFactory(false)
val fsm = (new NFA(start))
```
The NFA class takes the start state as a parameter. We can build up the NFA using the `addTransition` function.

```scala
val newFSM = fsm.addTransition((start, Lambda), TotalState(0, true))
```

`addTransition` takes a `Transition Key` which is a `State` and `Token` tuple and a destination `State`. `addTransition` returns a new NFA. **Foam is designed to produce immutable objects, so you must store the result!** Conceptually, we are adding a new entry to a [state-transition table](https://en.wikipedia.org/wiki/State-transition_table).

## Components of an NFA
NFAs are made of `State` and `Tokens` objects which both inherit from `Component`.

```scala
trait Component {
  override def toString = ""
}
```

```scala
trait State extends Component {
    def isAccept: Boolean
}
```

```scala
trait Token extends Component {
    def isLamda: Boolean = false
}
```

All `State` and `Token` objects should extend these two traits. 

**Note: We provide a Lambda token. We consider this to be a clock step in the the FSM. Thus, lambda transitions will not result in the combination of states.**

# Converting to DFA
To convert an NFA to a DFA, all we need to do is create a new DFA object.
```scala
val error = SimpleStateFactory(false)
val vendDFA = new DFA(vendFSM, error)
```
The class takes a NFA and an error state. The DFA conversion will make sure that the the resulting DFA is complete. All undefined transitions for state-token pairs will go to the error state provided.
