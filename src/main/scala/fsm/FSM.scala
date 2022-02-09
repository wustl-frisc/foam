package fsm

class FSM {

    var start = new SimpleState();
    var accept = new SimpleState();
    start.transitions += new Transition(new Lambda(), accept);
    val states = Map[String, State]("start" -> start, "accept" -> accept);

    def addState(s: State) = {
        if (!states.valuesIterator.contains(s)) {
            if (s.getName()) {
                states += s.getName() -> s;
            } else {
                states += states.size -> s;
            }
        }
    }

    // Adds both states to machine if needed
    def addTransition(s: State, t: Transition) = {
        addState(s);
        addState(t.destination);
        s.transitions += t;
    }

    // Adds dest to machine if needed
    def moveTransition(t: Transition, dest: State) = {
        addState(dest);
        t.destination = dest;
    }

    def addCode(s: State, f: Token => Unit) = {
        s.body += f;
    }

    def changeToken(t: Transition, token: Token) = {
        t.token = token;
    }

    // Adds s to machine
    def insertFsm(s: State, fsm: FSM) = {
        addState(s);

        val transitions = s.transitions;
        s.transitions = Set[Transition]();

        s = fsm.start;
        fsm.accept.transitions ++ transitions;
        states ++ fsm.states;
    }

}
