package net.jakewoods.robotsim

object Main {
  def main(args: Array[String]): Unit = {
    val inputs = Iterator.continually(io.StdIn.readLine).takeWhile(_.nonEmpty)
    val commands = InputMapper.strings2commands(inputs)
    val simulation = Simulation.create(5, 5)

    commands.foldLeft(simulation)(commandStep)
  }

  /** Executes each command on the simulation and prints the results.
    *
    * commandStep forms the heart of our Read-Eval-Print-Loop. It forms the output half
    * of our real world interaction.
    */
  private def commandStep(simulation: Simulation, command: RobotCommand): Simulation = {
    val nextSimulation = simulation.step(command)

    nextSimulation.messages.foreach(println)

    nextSimulation
  }
}
