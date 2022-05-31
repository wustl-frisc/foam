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
For this portion of the tutorial, we will be demostrating the Vending Machine example. To create an NFA all we need to do is create a new `NFA` object.

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
To convert an NFA to a DFA, all we need to do is create a new `DFA` object.

```scala
val error = SimpleStateFactory(false)
val vendDFA = new DFA(vendFSM, error)
```
The class takes a NFA and an error state. The DFA conversion will make sure that the the resulting DFA is complete. All undefined transitions for state-token pairs will go to the error state provided.

# Converting to Chisel
To convert a DFA into Chisel structures, all we need to do is create a new `ChiselFSM` object within a Chisel module.

```scala
class ExampleConversion(dfa: DFA) extends Module {
  val chiselDFA = Module(new ChiselFSM(dfa))
}
```

# Emitting NFAs and DFAs
The `Emitter` object provides functionality to emit either Graphvis or Verilog.

## Graphvis
`emitGV` can emit either an `NFA` or `DFA`, but requres the user to provide names[^1] for all the componets.

[^1]: Overriding the `toString` method is very useful here.

```scala
val namer: Any => String = (element) => element match {
    case s: State if(s == start) => "Start"
    case other => other.toString
}

Emitter.emitGV(vendDFA, namer)
``` 

## Verilog
The Verilog emitter only accepts a `DFA`. However, that is the only thing that needs to be provided.

```scala
Emitter.emitVerilog(vendDFA)
```

# Aspects
Features can be built out of aspects. Aspects are always applied to NFAs. Aspects are composed of a _pointcut_ and _advice_. Simply, a pointcut is a _set_ of joinpoints (components of the NFA) in the NFA where the implmentation information in the advice is applied.

## Pointcuts
To simplify the process of creating pointcuts, we provide the `Pointcutter` object.

```scala
val statePointcut = Pointcutter[State, TotalState](nfa.states, state => state match {
      case s: TotalState if(s.value + coin.value <= threshold) => true
      case _ => false
})
```
The two type parameters allow us to designate the type of the components going into the `Pointcutter` and the type of the components in the set that result.

## Advice
Advice can be applied to either `State` objects or `Token` objects.

### Anatomy of Advice
All advice has the following form:

```scala
Advice[Component](pointcut, nfa)((thisJoinPoint: Joinpoint[Component], thisNFA: NFA) => {
  //Advice Body
  (thisJoinPoint.point, thisNFA)
})
```
Advice takes in a `pointcut`, an `NFA`, and an advice function. All advice functions must return a tuple containing a valid path for the NFA and the NFA the advice will be applied to. This could be a sinlge state, single token, state-token, or token-state path, depending on the advice. **Note: If any transitions are added inside the advice body. The resulting NFA must be passed in the tuple otherwise the changes will be lost.** 

For states, the advice is called _for each transition in to and out of the state_. For tokens, the advice is called _for each state where the token is defined in a transition and every desitnation of that transition_.

### Reflexive Access
The parameters `thisJoinPoint` and `thisNFA`[^2] give refexive access to both the current joinpoint and the NFA. The `Joinpoint` class has three instance varables for reflexive access.

- `point` direct reflexive access to the joinpoint.
- `in` The current transition leading into the state in the case of `State` advice. The state before the token in the case of `Token` advice.
- `out` The current trasition leading out of the state in the case of `State` advice. The state after the token in the case of `Token` advice.

This reflexive access allows to apply different advice depending on which `in` or `out` the advice is being applied with. For example:
```scala
AfterToken[Coin](tokenPointcut, nfa)((thisJoinPoint: TokenJoinpoint[Coin], thisNFA: NFA) => {
      var value = thisJoinPoint.out.asInstanceOf[ValueState].value
      thisJoinPoint.out match {
        case s: TotalState => (Some((PrinterState("Insufficient Funds!", s.value, false), Lambda)), thisNFA)
        case _ => (None, thisNFA)
      }
})
```

[^2]: Technically, these can be named whatever you want, but we recommend using this naming to keep things straight.

## Applying Features
To apply features, we provide the `Weaver` object. Features will be repeatedly applied until the resulting NFA no longer changes.

```scala
val finalFSM = Weaver[NFA](features, nfa, (before: NFA, after: NFA) => before.isEqual(after))
```
The `Weaver` object takes in a set of features, an NFAs, and a function to test the equality of two NFAs.

# Contributors
Justin Deters
Max Camp-Oberhauser
