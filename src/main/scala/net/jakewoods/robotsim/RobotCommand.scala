package net.jakewoods.robotsim

import scala.util.Try

/** Represents a command that can be executed in the simulation
  *
  * Each command has a difference effect. Consult [[Simulation.step]] for more information.
  */
sealed abstract class RobotCommand
case class Place(x: Int, y: Int, facing: Facing) extends RobotCommand
case object Move extends RobotCommand
case object Left extends RobotCommand
case object Right extends RobotCommand
case object Report extends RobotCommand

object RobotCommand {
  /** Maps a single string to a [[RobotCommand]].
    *
    * If the string is valid this function returns the corresponding [[RobotCommand]]
    * in an Option.
    *
    * If the string is invalid None is returned.
    *
    * Valid command strings are:
    *
    * - PLACE X,Y,F
    * - MOVE
    * - LEFT
    * - RIGHT
    * - REPORT
    *
    * Where X and Y are valid positive integers and F is one of: NORTH, SOUTH, EAST or WEST. For example:
    *
    *     > PLACE 5,2,SOUTH
    *
    * Whitespace other then the gap between PLACE and it's arguments will be ignored. For example all
    * of the following are valid commands:
    *
    *     > PLACE 5, 2, SOUTH
    *     >     PLACE 5,2,SOUTH
    *     >   MOVE
    *
    * Commands **are not case insensitive**. `Move` is not a valid command.
    *
    * @param s the string to pars
    * @return the parsed robot command or None
    */
  def string2command(s: String): Option[RobotCommand] = {
    // We're going to make the assumption that commands follow the form:
    //
    //     COMMAND_NAME CSV_ARG_LIST
    //
    // Where CSV_ARG_LIST is required/optional/disallowed on a per-command basis.
    // As such we'll do the parsing once (including our documented whitespace rules)
    // and then just give the interesting bits to the individual command parsers.
    val commandParts = s.trim().split("\\s+", 2) // Split only on the first space after a word

    // We know we've always got either one or two parts (empty string splits to Array(""))
    val command = commandParts(0)
    val argString = if(commandParts.length == 2) Some(commandParts(1)) else None

    // For our arguments we want to ignore all whitespace and just split the CSV
    val arguments = argString.map(s => s.replaceAll("\\s", "").split(","))

    // We want to run each of our parsers until one of them returns a command (indicating
    // success) or none of them do (indicating failure).
    //
    // As I understand it `orElse` has it's parameter passed by *name* which I believe means
    // it's lazily evaluated. If that turns out to be untrue it would be good to find another
    // way to compose these functions as we only want to evaluate until our first successful match
    place(command, arguments)
      .orElse(move(command, arguments))
      .orElse(left(command, arguments))
      .orElse(right(command, arguments))
      .orElse(report(command, arguments))
  }

  /** Attempts to parses the PLACE command
    *
    * Expectations:
    *
    * - `command` is "PLACE"
    * - `arguments` exists and contains three elements
    * - `argument(0)` and `argument(1)` are strings that are convertible to Ints.
    * - `argument(2)` is a string parsable by [[Facing.string2facing]]
    *
    * If these expectations are not met this function returns None
    *
    * @param command the name of the command executed by the user
    * @param arguments the arguments of the command executed by the user
    * @return A [[Place]] command or None
    */
  private def place(command: String, arguments: Option[Array[String]]): Option[RobotCommand] = {
    for {
      args <- arguments
      if args.length == 3
      x <- Try(args(0).toInt).toOption
      y <- Try(args(1).toInt).toOption
      facing <- Facing.string2facing(args(2))
    } yield Place(x, y, facing)
  }

  /** Attempts to parse the MOVE command
    *
    * @param command the name of the command executed by the user
    * @param arguments the arguments of the command executed by the user
    * @return A [[Move]] command or None
    */
  private def move(command: String, arguments: Option[Array[String]]): Option[RobotCommand] =
    if(command == "MOVE" && arguments.isEmpty) Some(Move) else None

  /** Attempts to parse the LEFT command
    *
    * @param command the name of the command executed by the user
    * @param arguments the arguments of the command executed by the user
    * @return A [[Left]] command or None
    */
  private def left(command: String, arguments: Option[Array[String]]): Option[RobotCommand] =
    if(command == "LEFT" && arguments.isEmpty) Some(Left) else None

  /** Attempts to parse the RIGHT command
    *
    * @param command the name of the command executed by the user
    * @param arguments the arguments of the command executed by the user
    * @return A [[Right]] command or None
    */
  private def right(command: String, arguments: Option[Array[String]]): Option[RobotCommand] =
    if(command == "RIGHT" && arguments.isEmpty) Some(Right) else None

  /** Attempts to parse the REPORT command
    *
    * @param command the name of the command executed by the user
    * @param arguments the arguments of the command executed by the user
    * @return A [[Report]] command or None
    */
  private def report(command: String, arguments: Option[Array[String]]): Option[RobotCommand] =
    if(command == "REPORT" && arguments.isEmpty) Some(Report) else None
}
