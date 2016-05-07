package net.jakewoods.robotsim

/** Deals with mapping user inputs (strings) to RobotCommands
  */
object InputMapper {
  /** Maps a sequence of strings to a sequence of commands.
    *
    * Strings are parsed according to the rules outlined in [[RobotCommand.string2command]]
    *
    * Only valid commands will be produced in the output. Invalid commands will be ignored.
    *
    * This function will strip any newlines that appear in it's input as
    * it is designed to deal with line-by-line input.
    *
    * @param inputs the inputs to process
    * @return a sequence of valid commands
    */
  def strings2commands(inputs: Iterator[String]): Iterator[RobotCommand] = {
    inputs
      .map(RobotCommand.string2command) // Convert our strings to commands
      .filter(_.isDefined) // Eject invalid commands from the stream
      .map(_.get) // Convert the survivors from Option[RobotCommand] -> RobotCommand
  }

}
