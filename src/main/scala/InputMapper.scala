/** Maps user input into commands
  *
  */
object InputMapper {
  def string2command(s: String): Option[RobotCommand] = {
    // We want to run each of our parsers until one of them returns a command (indicating
    // success) or none of them do (indicating failure).
    //
    // As I understand it `orElse` has it's parameter passed by *name* which I believe means
    // it's lazily evaluated. If that turns out to be untrue it would be good to find another
    // way to compose these functions as we only want to evaluate until our first successful match
    place(s)
      .orElse(move(s))
      .orElse(left(s))
      .orElse(right(s))
      .orElse(report(s))
  }

  private def place(s: String): Option[RobotCommand] = None
  private def move(s: String): Option[RobotCommand] = None
  private def left(s: String): Option[RobotCommand] = None
  private def right(s: String): Option[RobotCommand] = None
  private def report(s: String): Option[RobotCommand] = None
}
