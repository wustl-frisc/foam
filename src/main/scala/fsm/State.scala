package edu.wustl.sbs
package fsm

trait State extends Component {
    def executeCode: Unit
}
