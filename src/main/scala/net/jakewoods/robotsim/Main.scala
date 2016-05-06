package net.jakewoods.robotsim

object Main {
  def main(args: Array[String]): Unit = {
    val commands = getCommands()
    val simulation = Simulation.create(5, 5)

    commands.foldLeft(simulation)(commandStep)
  }

  /** Gathers user inputs from the console and returns a lazy sequence of commands
    *
    * getCommands is the input boundary between the impure real world and our nice pure
    * InputMapper.
    */
  private def getCommands(): Iterator[RobotCommand] =
    Iterator.continually(io.StdIn.readLine)
      .takeWhile(_.nonEmpty)
      .map(InputMapper.string2command) // Convert inputs to Option[RobotCommand]
      .filter(_.isDefined) // Eject invalid commands from the stream
      .map(_.get) // Convert the survivors from Option[RobotCommand] -> RobotCommand

  /** Executes each command on the simulation and prints the results.
    *
    * commandStep forms the heart of our Read-Eval-Print-Loop. It forms the output half
    * of our real world interaction.
    */
  private def commandStep(simulation: Simulation, command: RobotCommand): Simulation = {
    val result = Simulation.step(simulation, command)

    result.messages.foreach(println)

    result.simulation
  }
}
