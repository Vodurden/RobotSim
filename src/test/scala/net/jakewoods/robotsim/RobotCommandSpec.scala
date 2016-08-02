package net.jakewoods.robotsim

class RobotCommandSpec extends UnitSpec {
  describe("string2command") {
    it("should return Some(Place(x,y,f)) when given PLACE with correct args") {
      assert(RobotCommand.string2command("PLACE 1,2,NORTH") == Some(Place(1,2,North)))
    }

    it("should return None when given PLACE with a non-integer x") {
      assert(RobotCommand.string2command("PLACE f,2,NORTH") == None)
    }

    it("should return None when given PLACE with a non-integer y") {
      assert(RobotCommand.string2command("PLACE 1,foo,NORTH") == None)
    }

    it("should return None when given PLACE with an invalid facing") {
      assert(RobotCommand.string2command("PLACE 1,2,BACKWARDS") == None)
    }

    it("should return None when given PLACE without args") {
      assert(RobotCommand.string2command("PLACE") == None)
    }

    it("should return None when given PLACE with the wrong number of args") {
      assert(RobotCommand.string2command("PLACE 1") == None)
      assert(RobotCommand.string2command("PLACE 1,2") == None)
    }

    it("should return None when given PLACE with a comma for args") {
      assert(RobotCommand.string2command("PLACE ,") == None)
    }

    it("should return None when given PLACE with two commas for args") {
      assert(RobotCommand.string2command("PLACE ,,") == None)
    }

    it("should return None when given PLACE with no separator between args") {
      assert(RobotCommand.string2command("PLACE1,2,NORTH") == None)
    }

    it("should return None when given SOMETHING with three valid PLACE arguments") {
      assert(RobotCommand.string2command("SOMETHING 1,2,NORTH") == None)
    }

    it("should return Some(PlaceObject) when given PLACE_OBJECT") {
      assert(RobotCommand.string2command("PLACE_OBJECT") == Some(PlaceObject))
    }

    it("should return None when given PLACE_OBJECT with arguments") {
      assert(RobotCommand.string2command("PLACE_OBJECT arg") == None)
    }

    it("should return Some(Map) when given MAP") {
      assert(RobotCommand.string2command("MAP") == Some(Map))
    }

    it("should return None when given MAP with arguments") {
      assert(RobotCommand.string2command("MAP arg") == None)
    }

    it("should return Some(Move) when given MOVE") {
      assert(RobotCommand.string2command("MOVE") == Some(Move))
    }

    it("should return None when given MOVE with arguments") {
      assert(RobotCommand.string2command("MOVE arg") == None)
    }

    it("should return Some(Left) when given LEFT") {
      assert(RobotCommand.string2command("LEFT") == Some(Left))
    }

    it("should return None when given LEFT with arguments") {
      assert(RobotCommand.string2command("LEFT arg") == None)
    }

    it("should return Some(Right) when given RIGHT") {
      assert(RobotCommand.string2command("RIGHT") == Some(Right))
    }

    it("should return None when given RIGHT with arguments") {
      assert(RobotCommand.string2command("RIGHT arg") == None)
    }

    it("should return Some(Report) when given REPORT") {
      assert(RobotCommand.string2command("REPORT") == Some(Report))
    }

    it("should return None when given REPORT with arguments") {
      assert(RobotCommand.string2command("REPORT arg") == None)
    }

    it("should return None when given an empty string") {
      assert(RobotCommand.string2command("") == None)
    }

    it("should return None when given input that matches no commands") {
      assert(RobotCommand.string2command("fakeinput") == None)
    }

    it("should return None when given bad input with spaces") {
      assert(RobotCommand.string2command("fake input with spaces") == None)
    }

    it("should ignore whitespace on commands with no args") {
      val commands = List(
        " MOVE ",
        "  LEFT",
        "RIGHT  ",
        "   REPORT    "
      )

      val results = commands.map(RobotCommand.string2command)
      results.foreach(result => assert(result.isDefined))
    }

    it("should ignore whitespace for commands with args excepting the command/argument separator") {
      val commands = List(
        "PLACE 1,2,NORTH",
        "  PLACE 1,2,NORTH",
        "PLACE 1,2,NORTH  ",
        "PLACE 1, 2, NORTH",
        "PLACE 1 , 2 , NORTH",
        "  PLACE 1 , 2 , NORTH  "
      )

      val results = commands.map(RobotCommand.string2command)
      results.foreach(result => assert(result.isDefined))
    }
  }
}
